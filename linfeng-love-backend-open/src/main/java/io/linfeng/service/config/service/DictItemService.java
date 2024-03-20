package io.linfeng.service.config.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.api.PageObject;
import io.linfeng.service.config.entity.DictItemEntity;

import java.util.List;
import java.util.Map;


/**
 * 字典项
 *
 */
public interface DictItemService extends IService<DictItemEntity> {

    PageObject queryPage(Map<String, Object> params);

    void saveItemAndRefreshCache(DictItemEntity dictItem);

    void updateItemAndRefreshCache(DictItemEntity dictItem);

    void deleteAndRefreshCache(Integer[] ids, String typeCode);

    String getItemName(String typeCode, Integer code);

    List<DictItemEntity> getItemList(String typeCode);

}
