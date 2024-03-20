package io.linfeng.service.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.api.PageObject;
import io.linfeng.service.config.entity.DictTypeEntity;

import java.util.Map;

/**
 * 数据字典类型表
 *
 * @author JiangCX
 * @date 2023-11-13 10:09:12
 */
public interface DictTypeService extends IService<DictTypeEntity> {

    PageObject queryPage(Map<String, Object> params);

}

