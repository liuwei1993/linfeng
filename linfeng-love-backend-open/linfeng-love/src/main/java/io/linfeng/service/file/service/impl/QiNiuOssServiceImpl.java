package io.linfeng.service.file.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import io.linfeng.common.exception.LinfengException;
import io.linfeng.common.utils.StringUtil;
import io.linfeng.service.file.service.QiNiuOssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 七牛云存储
 *
 */
@Slf4j
@Component
public class QiNiuOssServiceImpl implements QiNiuOssService {

    @Value("${qiniu.accessKey}")
    private String qiNiuAccessKey;

    @Value("${qiniu.secretKey}")
    private String qiNiuSecretKey;

    @Value("${qiniu.bucketName}")
    private String qiNiuBucketName;

    @Value("${qiniu.domain}")
    private String qiNiuDomain;

    @Value("${qiniu.directory}")
    private String qiNiuDirectory;

    private String directory;

    private String domain;

    private String token;

    private Long tokenExpiredTime;


    private UploadManager uploadManager;

    public QiNiuOssServiceImpl(){
        uploadManager = new UploadManager(new Configuration(Zone.autoZone()));
    }

    @Override
    public String upload(byte[] data, String suffix) {
        checkToken();
        String path = generatePath(suffix);
        try {
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                log.error("上传七牛云失败：", res.error);
                throw new LinfengException("图片上传失败");
            }
        } catch (Exception e) {
            throw new LinfengException("上传七牛云失败：", e);
        }
        return domain + "/" + path;
    }

    private String generatePath(String suffix){
        //生成uuid
        String uuid = IdUtil.fastSimpleUUID();

        //文件路径
        String path = directory + "/"
                + DateUtil.format(new Date(), "yyyyMMdd")
                + "/" + uuid
                + suffix;
        return path;
    }

    private void checkToken(){
        //token过期时间默认3600秒，在过期5分钟前刷新token，如果是在集群环境下需要存在数据库或者redis当中
        if(!StringUtil.isEmpty(token) && (tokenExpiredTime - 5 * 60 * 100) > System.currentTimeMillis()){
            return;
        }
        String accessKey = qiNiuAccessKey;
        String secretKey = qiNiuSecretKey;
        String bucketName = qiNiuBucketName;
        domain = qiNiuDomain;
        directory = qiNiuDirectory;
        token = Auth.create(accessKey, secretKey).uploadToken(bucketName);
        tokenExpiredTime = System.currentTimeMillis() + 3600 * 100;
    }
}
