package io.linfeng.service.file.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.linfeng.service.file.entity.FileEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传
 * 
 * @author JiangCX
 * @date 2023-10-25 13:35:13
 */
@Mapper
public interface FileDao extends BaseMapper<FileEntity> {
	
}
