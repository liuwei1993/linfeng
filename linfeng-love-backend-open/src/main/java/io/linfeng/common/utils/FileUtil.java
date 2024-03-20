package io.linfeng.common.utils;

/**
 * @author JiangCX
 * @date 2023/2/21 10:38
 */
public class FileUtil {

    public static boolean checkSize(long maxSize, long size) {
        // 单位 M
        int len = 1024 * 1024;
        if(size > (maxSize * len)){
            return false;
        }
        return true;
    }

}
