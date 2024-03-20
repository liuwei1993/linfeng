package io.linfeng.common.utils;

import cn.hutool.core.util.StrUtil;

/**
 * @author JiangCX
 * @date 2023/1/20 13:15
 */


public class StringUtil {


    /**
     * @Title: isEmpty
     * @Description: 判断对象是否为空
     * @param obj
     * @return
     * @return Integer
     */
    public static boolean isEmpty(Object obj) {
        return null == obj || "".equals(obj);
    }

    /**
     * 手机号脱敏处理
     * @param phone 手机号
     */
    public static String maskMobile(String phone) {
        if (StrUtil.isBlank(phone)) {
            return "";
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

}
