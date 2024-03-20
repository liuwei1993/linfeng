package io.linfeng.service.file.service;

/**
 * 七牛云服务
 *
 * @author JiangCX
 * @date 2023-10-25 13:35:13
 */
public interface QiNiuOssService {

    /**
     * 上传文件到七牛云
     * @param data 文件流
     * @param suffix 后缀
     * @return 七牛云图片地址
     */
    public String upload(byte[] data, String suffix);

}

