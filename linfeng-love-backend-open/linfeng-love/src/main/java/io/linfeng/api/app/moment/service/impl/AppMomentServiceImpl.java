package io.linfeng.api.app.moment.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.linfeng.api.app.moment.request.*;
import io.linfeng.api.app.moment.response.*;
import io.linfeng.api.app.moment.service.AppMomentService;
import io.linfeng.common.enums.CommonStatus;
import io.linfeng.common.enums.CountOperatorType;
import io.linfeng.common.utils.Constant;
import io.linfeng.common.utils.DateUtil;
import io.linfeng.common.utils.ObjectMapperUtil;
import io.linfeng.common.utils.StringUtil;
import io.linfeng.service.config.service.DictItemService;
import io.linfeng.service.moment.dto.response.MomentCommentResponseDTO;
import io.linfeng.service.moment.dto.response.MomentLikeResponseDTO;
import io.linfeng.service.moment.dto.response.MomentMediaResponseDTO;
import io.linfeng.service.moment.dto.response.MomentResponseDTO;
import io.linfeng.service.moment.entity.MomentCommentEntity;
import io.linfeng.service.moment.entity.MomentEntity;
import io.linfeng.service.moment.entity.MomentLikeEntity;
import io.linfeng.service.moment.entity.MomentMediaEntity;
import io.linfeng.service.moment.enums.MomentQueryType;
import io.linfeng.service.moment.service.MomentCommentService;
import io.linfeng.service.moment.service.MomentLikeService;
import io.linfeng.service.moment.service.MomentMediaService;
import io.linfeng.service.moment.service.MomentService;
import io.linfeng.service.user.entity.UserEntity;
import io.linfeng.service.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service("appMomentService")
public class AppMomentServiceImpl implements AppMomentService {

    private final MomentService momentService;

    private final UserService userService;

    private final MomentMediaService momentMediaService;

    private final MomentLikeService momentLikeService;

    private final MomentCommentService momentCommentService;

    private final DictItemService dictItemService;


    public AppMomentServiceImpl(MomentService momentService, UserService userService, MomentMediaService momentMediaService, MomentLikeService momentLikeService, MomentCommentService momentCommentService, DictItemService dictItemService) {
        this.momentService = momentService;
        this.userService = userService;
        this.momentMediaService = momentMediaService;
        this.momentLikeService = momentLikeService;
        this.momentCommentService = momentCommentService;
        this.dictItemService = dictItemService;
    }

    @Override
    public IPage<MomentResponse> getMomentList(UserEntity user, MomentQueryRequest request) {

        IPage<MomentResponseDTO> dtoPage = new Page<>(request.getPageNum(),request.getPageSize());

        if(request.getQueryType() == MomentQueryType.NEW.getValue()){
            dtoPage = momentService.selectNewestMomentPage(dtoPage, user.getUid());
        }

        if(request.getQueryType() == MomentQueryType.GUEST.getValue()){
            dtoPage = momentService.selectGuestMomentPage(dtoPage, user.getUid(), request.getUid());
        }

        if(request.getQueryType() == MomentQueryType.MY.getValue()){
            dtoPage = momentService.selectMyMomentPage(dtoPage, user.getUid());
        }

        IPage<MomentResponse> page = ObjectMapperUtil.convert(dtoPage, MomentResponse.class);
        List<MomentResponse> momentList = page.getRecords();
        momentList.forEach(moment -> {
            if (!StringUtil.isEmpty(moment.getJob())) {
                moment.setJobText(dictItemService.getItemName(Constant.DICT_JOB, moment.getJob()));
            }
            if (!StringUtil.isEmpty(moment.getBirthday())) {
                moment.setBirthdayYear(moment.getBirthday().substring(2, 4));
            }

        });

        return page;
    }

    @Override
    public void addMoment(UserEntity user, MomentRequest request) {

        //保存动态信息
        MomentEntity momentEntity = ObjectMapperUtil.convert(request, MomentEntity.class);

        momentEntity.setUid(user.getUid());
        momentEntity.setPv(0);
        momentEntity.setLv(0);
        momentEntity.setCv(0);
        momentEntity.setStatus(CommonStatus.CHECKED.getValue());
        momentEntity.setCreateTime(DateUtil.nowDateTime());
        momentService.save(momentEntity);

        //保存附件信息
        if(request.getMediaList() != null && request.getMediaList().size() != 0){
            List<MomentMediaEntity> momentMediaEntityList = new ArrayList<>();
            request.getMediaList().forEach(momentMediaRequest -> {
                MomentMediaEntity momentMediaEntity = new MomentMediaEntity();
                momentMediaEntity.setMomentId(momentEntity.getId());
                momentMediaEntity.setUid(user.getUid());
                momentMediaEntity.setMediaName(IdUtil.fastSimpleUUID());
                momentMediaEntity.setUrl(momentMediaRequest.getUrl());
                momentMediaEntity.setMediaType(momentEntity.getMediaType());
                momentMediaEntity.setCreateTime(DateUtil.nowDateTime());
                momentMediaEntityList.add(momentMediaEntity);
            });

            momentMediaService.saveBatch(momentMediaEntityList);
        }

    }

    @Override
    public MomentResponse getMomentDetail(UserEntity user, Integer momentId) {
        MomentResponseDTO momentResponseDTO = momentService.selectMomentDetail(user.getUid(), momentId);
        if(momentResponseDTO == null){
            return null;
        }
        MomentResponse momentResponse = ObjectMapperUtil.convert(momentResponseDTO, MomentResponse.class);
        if (!StringUtil.isEmpty(momentResponse.getJob())) {
            momentResponse.setJobText(dictItemService.getItemName(Constant.DICT_JOB, momentResponse.getJob()));
        }
        if (!StringUtil.isEmpty(momentResponse.getBirthday())) {
            momentResponse.setBirthdayYear(momentResponse.getBirthday().substring(2,4));
        }

        //更新浏览量
        this.updatePv(momentId);

        return momentResponse;
    }

    @Override
    @Transactional
    public void updateLv(UserEntity user, MomentUpdateRequest request) {
        if(request.getOperatorType()  == CountOperatorType.ADD.getValue()){
            //更新动态表
            momentService.updateLv(request.getMomentId(), 1);

            //之前点赞过就直接更新状态
            LambdaQueryWrapper<MomentLikeEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MomentLikeEntity::getUid, user.getUid());
            wrapper.eq(MomentLikeEntity::getMomentId, request.getMomentId());
            MomentLikeEntity momentLikeEntity = momentLikeService.getOne(wrapper);
            if(momentLikeEntity != null){
                momentLikeEntity.setStatus(CountOperatorType.ADD.getValue());
                momentLikeEntity.setUpdateTime(DateUtil.nowDateTime());
                momentLikeService.updateById(momentLikeEntity);
                return;
            }
            //首次点赞发送点赞通知
            momentLikeEntity = new MomentLikeEntity();
            momentLikeEntity.setMomentId(request.getMomentId());
            momentLikeEntity.setUid(user.getUid());
            momentLikeEntity.setStatus(CountOperatorType.ADD.getValue());
            momentLikeEntity.setCreateTime(DateUtil.nowDateTime());
            momentLikeService.save(momentLikeEntity);
        }else{
            //更新动态表
            momentService.updateLv(request.getMomentId(), -1);

            MomentLikeEntity momentLikeEntity = new MomentLikeEntity();
            momentLikeEntity.setStatus(request.getOperatorType());
            momentLikeEntity.setUpdateTime(DateUtil.nowDateTime());
            LambdaQueryWrapper<MomentLikeEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MomentLikeEntity::getMomentId, request.getMomentId());
            wrapper.eq(MomentLikeEntity::getUid, user.getUid());
            momentLikeService.update(momentLikeEntity, wrapper);
        }
    }

    @Override
    public void updatePv(Integer momentId) {
        momentService.updatePv(momentId);
    }

    @Override
    public IPage<MomentCommentResponse> getMomentCommentPage(CommentQueryRequest request) {
        IPage<MomentCommentResponseDTO> dtoPage = new Page<>(request.getPageNum(),request.getPageSize());
        dtoPage = momentCommentService.getMomentCommentPage(dtoPage, request.getMomentId());
        IPage<MomentCommentResponse> page = ObjectMapperUtil.convert(dtoPage, MomentCommentResponse.class);
        List<MomentCommentResponse> commentList = page.getRecords();
        page.setRecords(commentList);
        commentList.forEach(comment -> {
            if (!StringUtil.isEmpty(comment.getJob())) {
                comment.setJobText(dictItemService.getItemName(Constant.DICT_JOB, comment.getJob()));
            }
            if (!StringUtil.isEmpty(comment.getBirthday())) {
                comment.setBirthdayYear(comment.getBirthday().substring(2,4));
            }
        });
        return page;
    }

    @Override
    @Transactional
    public MomentCommentResponse addMomentComment(UserEntity user, MomentCommentRequest request) {
        MomentCommentEntity momentCommentEntity = new MomentCommentEntity();
        momentCommentEntity.setMomentId(request.getMomentId());
        momentCommentEntity.setContent(request.getContent());
        momentCommentEntity.setStatus(CommonStatus.CHECKED.getValue());
        momentCommentEntity.setCreateTime(DateUtil.nowDateTime());
        momentCommentEntity.setReplyUid(user.getUid());
        momentCommentService.save(momentCommentEntity);

        //更新动态评论数
        momentService.updateCv(request.getMomentId(), CountOperatorType.ADD.getValue());

        MomentCommentResponse response = new MomentCommentResponse();
        response.setCommentId(momentCommentEntity.getId());
        response.setContent(request.getContent());
        response.setReplyUid(user.getUid());
        response.setReplyUserName(user.getUserName());
        response.setAvatar(user.getAvatar());
        response.setGender(user.getGender());
        response.setBirthday(user.getBirthday());
        response.setBirthdayYear(user.getBirthday().substring(2,4));
        response.setLivingCity(user.getLivingCity());
        response.setJob(user.getJob());
        response.setJobText(dictItemService.getItemName(Constant.DICT_JOB, user.getJob()));
        response.setChildCommentList(new ArrayList<>());
        response.setCreateTime(DateUtil.nowDateTime(DateUtil.DATE_FORMAT));
        return response;

    }

    @Override
    @Transactional
    public void deleteMomentComment(MomentCommentRequest request) {
        momentService.removeById(request.getCommentId());
        momentService.updateCv(request.getMomentId(), CountOperatorType.REDUCE.getValue());
    }

    @Override
    public List<MomentMediaResponse> getMomentImageList(Integer momentId) {
        List<MomentMediaResponseDTO> responseDTOList = momentMediaService.getMomentImageList(momentId);
        List<MomentMediaResponse> responseList = ObjectMapperUtil.convert(responseDTOList, MomentMediaResponse.class);
        return responseList;
    }

    @Override
    public CommentDetailResponse getMomentCommentDetail(Integer commentId) {
        MomentCommentEntity momentCommentEntity = momentCommentService.getById(commentId);
        CommentDetailResponse response = new CommentDetailResponse();
        response.setMomentId(momentCommentEntity.getMomentId());
        response.setContent(momentCommentEntity.getContent());
        UserEntity replyUser = userService.getUserByUid(momentCommentEntity.getReplyUid());
        response.setReplyUid(replyUser.getUid());
        response.setReplyUserName(replyUser.getUserName());
        response.setCreateTime(DateUtil.dateToStr(momentCommentEntity.getCreateTime(), DateUtil.DATE_FORMAT));
        return response;
    }

    @Override
    public MomentImageResponse getUserMomentImage(Integer uid) {

        MomentImageResponse response = new MomentImageResponse();
        //个人动态数量
        LambdaQueryWrapper<MomentEntity> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(MomentEntity::getUid, uid);
        response.setMomentListLength(momentService.count(lambdaQueryWrapper));

        //个人动态照片，最多展示四张
        List<MomentMediaResponse> momentMediaList = getMomentImageList(uid);
        response.setMomentMediaList(momentMediaList);
        return response;
    }

    @Override
    public void deleteMoment(MomentUpdateRequest request) {
        momentService.removeById(request.getMomentId());
        LambdaQueryWrapper<MomentMediaEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MomentMediaEntity::getMomentId, request.getMomentId());
        momentMediaService.remove(wrapper);
    }

}