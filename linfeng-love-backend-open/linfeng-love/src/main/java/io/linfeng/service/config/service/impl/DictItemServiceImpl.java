package io.linfeng.service.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.api.PageObject;
import io.linfeng.common.utils.DateUtil;
import io.linfeng.common.utils.RedisUtil;
import io.linfeng.service.config.dao.DictItemDao;
import io.linfeng.service.config.entity.DictItemEntity;
import io.linfeng.service.config.service.DictItemService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service("dictItemService")
public class DictItemServiceImpl extends ServiceImpl<DictItemDao, DictItemEntity> implements DictItemService {

    private final RedisUtil redisUtil;

    public DictItemServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public PageObject queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<DictItemEntity> wrapper = new LambdaQueryWrapper<>();
        String typeCode = (String)params.get("typeCode");
        wrapper.eq(DictItemEntity::getTypeCode, typeCode);
        long pageSize = Long.parseLong((String)params.get("limit"));
        long pageNum = Long.parseLong((String)params.get("page"));;
        IPage<DictItemEntity> page = this.page(
                new Page<>(pageNum, pageSize),
                wrapper
        );

        return new PageObject(page);
    }

    @Override
    public void saveItemAndRefreshCache(DictItemEntity dictItem) {
        LambdaQueryWrapper<DictItemEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DictItemEntity::getTypeCode, dictItem.getTypeCode());
        List<DictItemEntity> dictItemEntityList = this.list(wrapper);
        Integer maxCode = 0;
        if(dictItemEntityList != null && dictItemEntityList.size() != 0){
            maxCode = dictItemEntityList.stream().max((a, b) -> {
                if (a.getCode() > b.getCode()){
                    return 1;
                }else{
                    return -1;
                }
            }).get().getCode();
        }
        dictItem.setCode(maxCode + 1);
        dictItem.setCreateTime(DateUtil.nowDateTime());
        this.save(dictItem);
        redisUtil.delete(DictItemEntity.CACHE_DICT_PREFIX + dictItem.getTypeCode());
    }

    @Override
    public void updateItemAndRefreshCache(DictItemEntity dictItem) {
        dictItem.setUpdateTime(DateUtil.nowDateTime());
        this.updateById(dictItem);
        redisUtil.delete(DictItemEntity.CACHE_DICT_PREFIX + dictItem.getTypeCode());
    }

    @Override
    public void deleteAndRefreshCache(Integer[] ids, String typeCode) {
        this.removeByIds(Arrays.asList(ids));
        redisUtil.delete(DictItemEntity.CACHE_DICT_PREFIX + typeCode);
    }

    @Override
    public String getItemName(String typeCode, Integer code) {
        List<DictItemEntity> dictItemList = getItemList(typeCode);
        String name = null;
        for(DictItemEntity dictItemEntity :  dictItemList){
            if(code == dictItemEntity.getCode()){
                name = dictItemEntity.getName();
                break;
            }
        }
        return name;
    }

    @Override
    public List<DictItemEntity> getItemList(String typeCode) {
        List<DictItemEntity> dictItemList = redisUtil.getList(DictItemEntity.CACHE_DICT_PREFIX + typeCode, DictItemEntity.class);
        if (dictItemList == null) {
            LambdaQueryWrapper<DictItemEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DictItemEntity::getTypeCode, typeCode);
            dictItemList = this.list(wrapper); //从数据库加载所有变量
            redisUtil.set(DictItemEntity.CACHE_DICT_PREFIX + typeCode, dictItemList, 7200);
        }
        return dictItemList;
    }
}
