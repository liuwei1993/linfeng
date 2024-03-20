package io.linfeng.service.config.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.api.PageObject;
import io.linfeng.service.config.dao.DictTypeDao;
import io.linfeng.service.config.entity.DictTypeEntity;
import io.linfeng.service.config.service.DictTypeService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("dictTypeService")
public class DictTypeServiceImpl extends ServiceImpl<DictTypeDao, DictTypeEntity> implements DictTypeService {

    @Override
    public PageObject queryPage(Map<String, Object> params) {
        long pageSize = Long.parseLong((String)params.get("limit"));
        long pageNum = Long.parseLong((String)params.get("page"));;
        IPage<DictTypeEntity> page = this.page(
                new Page<>(pageNum, pageSize)
        );

        return new PageObject(page);
    }

}