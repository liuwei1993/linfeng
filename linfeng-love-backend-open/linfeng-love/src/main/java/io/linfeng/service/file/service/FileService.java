package io.linfeng.service.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.service.file.entity.FileEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 *
 * @author JiangCX
 * @date 2023-10-25 13:35:13
 */
public interface FileService extends IService<FileEntity> {

    /**
     * 文件上传
     * @param file 文件
     * @return 图片地址
     */
    String upload(MultipartFile file);

}

