package io.linfeng.service.moment.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.api.PageObject;
import io.linfeng.common.enums.CountOperatorType;
import io.linfeng.common.utils.StringUtil;
import io.linfeng.service.moment.dao.MomentDao;
import io.linfeng.service.moment.dto.response.MomentResponseDTO;
import io.linfeng.service.moment.entity.MomentEntity;
import io.linfeng.service.moment.service.MomentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("momentService")
public class MomentServiceImpl extends ServiceImpl<MomentDao, MomentEntity> implements MomentService {

    @Override
    public PageObject queryPage(Map<String, Object> params) {

        LambdaQueryWrapper<MomentEntity> wrapper = new LambdaQueryWrapper<>();
        if(!StringUtil.isEmpty(params.get("uid"))){
            wrapper.eq(MomentEntity::getUid, params.get("uid"));
        }
        if(!StringUtil.isEmpty(params.get("content"))){
            wrapper.like(MomentEntity::getContent, params.get("content"));
        }
        wrapper.orderByDesc(MomentEntity::getId);
        long pageSize = Long.parseLong((String)params.get("limit"));
        long pageNum = Long.parseLong((String)params.get("page"));;
        IPage<MomentEntity> page = this.page(
                new Page<>(pageNum, pageSize),
                wrapper
        );

        return new PageObject(page);
    }

    @Override
    public IPage<MomentResponseDTO> selectNewestMomentPage(IPage<MomentResponseDTO> page, Integer uid) {

        //由于复杂对象在进行分页的时候会导致数据折叠而丢失，这边先查询动态的单表数据分页，拿到分页数据后再查询复杂数据，下面几个方法同理
        IPage<Integer> momentPage = new Page<>(page.getCurrent(), page.getSize());
        momentPage = this.baseMapper.selectNewestMomentIdPage(momentPage);
        List<Integer> momentIdList = momentPage.getRecords();
        IPage<MomentResponseDTO> dtoPage = new Page<>();
        dtoPage.setPages(momentPage.getPages());
        dtoPage.setTotal(momentPage.getTotal());
        dtoPage.setSize(momentPage.getSize());
        dtoPage.setCurrent(momentPage.getCurrent());
        List<Integer> idList = momentIdList;
        if(idList == null || idList.size() == 0){
            dtoPage.setRecords(new ArrayList<>());
        }else{
            dtoPage.setRecords(this.baseMapper.selectNewestMomentList(momentIdList, uid));
        }
        return dtoPage;
    }

    @Override
    public IPage<MomentResponseDTO> selectGuestMomentPage(IPage<MomentResponseDTO> page, Integer uid, Integer guestUid) {
        IPage<MomentEntity> momentPage = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<MomentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MomentEntity::getUid, guestUid);
        wrapper.orderByDesc(MomentEntity::getId);
        momentPage = page(momentPage, wrapper);
        List<Integer> momentIdList = momentPage.getRecords().stream().map(moment -> moment.getId()).collect(Collectors.toList());
        IPage<MomentResponseDTO> dtoPage = new Page<>();
        dtoPage.setPages(momentPage.getPages());
        dtoPage.setTotal(momentPage.getTotal());
        dtoPage.setSize(momentPage.getSize());
        dtoPage.setCurrent(momentPage.getCurrent());
        List<Integer> idList = momentIdList;
        if(idList == null || idList.size() == 0){
            dtoPage.setRecords(new ArrayList<>());
        }else{
            dtoPage.setRecords(this.baseMapper.selectGuestMomentList(momentIdList, uid, guestUid));
        }
        return dtoPage;
    }

    @Override
    public IPage<MomentResponseDTO> selectMyMomentPage(IPage<MomentResponseDTO> page, Integer uid) {
        IPage<MomentEntity> momentPage = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<MomentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MomentEntity::getUid, uid);
        wrapper.orderByDesc(MomentEntity::getId);
        momentPage = page(momentPage, wrapper);
        List<Integer> momentIdList = momentPage.getRecords().stream().map(moment -> moment.getId()).collect(Collectors.toList());
        IPage<MomentResponseDTO> dtoPage = new Page<>();
        dtoPage.setPages(momentPage.getPages());
        dtoPage.setTotal(momentPage.getTotal());
        dtoPage.setSize(momentPage.getSize());
        dtoPage.setCurrent(momentPage.getCurrent());
        List<Integer> idList = momentIdList;
        if(idList == null || idList.size() == 0){
            dtoPage.setRecords(new ArrayList<>());
        }else{
            dtoPage.setRecords(this.baseMapper.selectMyMomentList(momentIdList, uid));
        }
        return dtoPage;
    }

    @Override
    public MomentResponseDTO selectMomentDetail(Integer uid, Integer momentId) {
        return this.baseMapper.selectMomentsDetail(uid, momentId);
    }

    @Override
    public void updateCv(Integer momentId, Integer operatorType) {
        if(operatorType  == CountOperatorType.ADD.getValue()){
            this.baseMapper.updateCv(momentId, 1);
        }else{
            this.baseMapper.updateCv(momentId, -1);
        }
    }

    @Override
    @Transactional
    public void updateLv(Integer momentId, Integer count) {
        this.baseMapper.updateLv(momentId, count);
    }

    @Override
    public void updatePv(Integer momentId) {
        this.baseMapper.updatePv(momentId);
    }

    @Override
    public void deleteMoment(Integer momentId) {
        this.removeById(momentId);
    }

}