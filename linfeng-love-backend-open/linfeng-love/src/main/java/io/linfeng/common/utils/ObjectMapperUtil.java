package io.linfeng.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.List;
import java.util.stream.Collectors;

public class ObjectMapperUtil {

    private static Mapper mapper = new DozerBeanMapper();

    public static <T, S> T convert(final S s, Class<T> clz) {
        return s == null ? null : mapper.map(s, clz);
    }

    public static <T, S> List<T> convert(List<S> s, Class<T> clz) {
        return s == null ? null : s.stream().map(vs -> mapper.map(vs, clz)).collect(Collectors.toList());
    }

    public static <T, S> IPage<T> convert(IPage<S> s, Class<T> clz) {
        if(s == null){
            return null;
        }
        IPage<T> page = new Page<>();
        page.setCurrent(s.getCurrent());
        page.setPages(s.getPages());
        page.setSize(s.getSize());
        page.setTotal(s.getTotal());
        page.setRecords(convert(s.getRecords(), clz));
        return page;
    }
}
