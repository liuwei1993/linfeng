package io.linfeng.service.moment.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.api.PageObject;
import io.linfeng.common.utils.StringUtil;
import io.linfeng.service.moment.dao.MomentCommentDao;
import io.linfeng.service.moment.dto.response.MomentCommentResponseDTO;
import io.linfeng.service.moment.entity.MomentCommentEntity;
import io.linfeng.service.moment.service.MomentCommentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("momentCommentService")
public class MomentCommentServiceImpl extends ServiceImpl<MomentCommentDao, MomentCommentEntity> implements MomentCommentService {

    @Override
    public PageObject queryPage(Map<String, Object> params) {

        LambdaQueryWrapper<MomentCommentEntity> wrapper = new LambdaQueryWrapper<>();
        if(!StringUtil.isEmpty(params.get("uid"))){
            wrapper.eq(MomentCommentEntity::getReplyUid, params.get("uid"));
        }
        if(!StringUtil.isEmpty(params.get("content"))){
            wrapper.like(MomentCommentEntity::getContent, params.get("content"));
        }
        wrapper.orderByDesc(MomentCommentEntity::getId);
        long pageSize = Long.parseLong((String)params.get("limit"));
        long pageNum = Long.parseLong((String)params.get("page"));;
        IPage<MomentCommentEntity> page = this.page(
                new Page<>(pageNum, pageSize),
                wrapper
        );

        return new PageObject(page);
    }

    @Override
    public IPage<MomentCommentResponseDTO> getMomentCommentPage(IPage<MomentCommentResponseDTO> page, Integer momentId) {

        //和动态查询一样,为防止collection引起的数据折叠,需要先查询分页数据,再关联业务数据
        IPage<MomentCommentEntity> commentPage = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<MomentCommentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MomentCommentEntity::getMomentId, momentId);
        wrapper.orderByDesc(MomentCommentEntity::getCreateTime);
        commentPage = page(commentPage, wrapper);
        List<Integer> commentIdList = commentPage.getRecords().stream().map(comment -> comment.getId()).collect(Collectors.toList());
        IPage<MomentCommentResponseDTO> dtoPage = new Page<>();
        dtoPage.setPages(commentPage.getPages());
        dtoPage.setTotal(commentPage.getTotal());
        dtoPage.setSize(commentPage.getSize());
        dtoPage.setCurrent(commentPage.getCurrent());
        List<Integer> idList = commentIdList;
        if(idList == null || idList.size() == 0){
            dtoPage.setRecords(new ArrayList<>());
        }else{
            dtoPage.setRecords(this.baseMapper.selectMomentCommentList(commentIdList));
        }
        return dtoPage;
    }

}