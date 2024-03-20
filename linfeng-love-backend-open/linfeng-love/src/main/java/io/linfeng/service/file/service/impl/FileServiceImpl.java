package io.linfeng.service.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.exception.LinfengException;
import io.linfeng.common.utils.DateUtil;
import io.linfeng.common.utils.FileUtil;
import io.linfeng.service.file.dao.FileDao;
import io.linfeng.service.file.entity.FileEntity;
import io.linfeng.service.file.service.FileService;
import io.linfeng.service.file.service.QiNiuOssService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service("fileService")
public class FileServiceImpl extends ServiceImpl<FileDao, FileEntity> implements FileService {

    @Value("${qiniu.max-size}")
    private Long maxSize;

    private final QiNiuOssService qiNiuOssService;

    public FileServiceImpl(QiNiuOssService qiNiuOssService) {
        this.qiNiuOssService = qiNiuOssService;
    }

    @Override
    public String upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new LinfengException("上传文件不能为空");
        }

        if(!FileUtil.checkSize(maxSize, file.getSize())){
            throw new LinfengException("上传文件超出规定大小");
        }
        String url = null;
        try{
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            url = qiNiuOssService.upload(file.getBytes(), suffix);
            FileEntity fileEntity = new FileEntity();
            fileEntity.setUrl(url);
            fileEntity.setCreateTime(DateUtil.nowDateTime());
            this.save(fileEntity);
        }catch (Exception e){
            log.error("文件上传失败", e);
            throw new LinfengException("文件上传失败", e);
        }
        return url;
    }

}