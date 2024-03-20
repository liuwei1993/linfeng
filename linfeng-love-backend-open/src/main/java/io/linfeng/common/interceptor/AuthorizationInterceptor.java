/**
 * -----------------------------------
 *  Copyright (c) 2021-2024
 *  All rights reserved, Designed By linfeng.tech , linfengtech.cn
 *  林风婚恋交友开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/yuncoder001/linfeng-love.git
 *  商业版授权联系技术客服	 QQ:  973921677/3582996245
 *  严禁分享、盗用、转卖源码或非法牟利！
 *  版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.common.interceptor;


import io.jsonwebtoken.Claims;
import io.linfeng.common.enums.CommonStatus;
import io.linfeng.common.exception.LinfengException;
import io.linfeng.common.utils.Constant;
import io.linfeng.common.utils.JwtUtil;
import io.linfeng.common.annotation.Login;
import io.linfeng.service.user.entity.UserEntity;
import io.linfeng.service.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限(Token)验证
 *
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    private final JwtUtil jwtUtil;

    private final UserService userService;

    public static final String USER_KEY = "userId";

    public AuthorizationInterceptor(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Login annotation;
        if(handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Login.class);
        }else{
            return true;
        }

        if(annotation == null){
            return true;
        }

        //获取用户凭证
        String token = request.getHeader(Constant.TOKEN_HEADER);
        if(StringUtils.isBlank(token)){
            token = request.getParameter(Constant.TOKEN_HEADER);
        }

        //凭证为空
        if(StringUtils.isBlank(token)){
            throw new LinfengException(Constant.TOKEN_HEADER + "不能为空", HttpStatus.UNAUTHORIZED.value());
        }

        Claims claims = jwtUtil.getClaimByToken(token);
        if(claims == null || jwtUtil.isTokenExpired(claims.getExpiration())){
            throw new LinfengException(Constant.TOKEN_HEADER + "失效，请重新登录", HttpStatus.UNAUTHORIZED.value());
        }

        //设置userId到request里，后续根据userId，获取用户信息

        request.setAttribute(USER_KEY, Long.parseLong(claims.getSubject()));
        UserEntity user = userService.getUserByUid(Integer.parseInt(claims.getSubject()));

        return true;
    }


}
