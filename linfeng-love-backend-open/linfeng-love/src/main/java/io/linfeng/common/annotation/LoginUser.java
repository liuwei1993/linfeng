/**
 * -----------------------------------
 *  Copyright (c) 2021-2024
 *  All rights reserved, Designed By linfeng.tech , linfengtech.cn
 * 林风婚恋交友开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/yuncoder001/linfeng-love.git
 * 商业版授权联系技术客服	 QQ:  973921677/3582996245
 * 严禁分享、盗用、转卖源码或非法牟利！
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登录用户信息
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {

}
