
package io.linfeng.service.user.dto.request;

import lombok.Data;

import java.util.List;

/**
 * app用户信息修改
 *
 */
@Data
public class UserUpdateRequestDTO {

    /**
     * 手机号
     */
    private String mobile;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 性别(0未知，1男，2女)
     */
    private Integer gender;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 身高
     */
    private Integer stature;
    /**
     * 体重
     */
    private Integer weight;
    /**
     * 家乡省份
     */
    private String homeProvince;
    /**
     * 家乡城市
     */
    private String homeCity;
    /**
     * 居住省份
     */
    private String livingProvince;
    /**
     * 居住城市
     */
    private String livingCity;
    /**
     * 婚姻状态（字典）
     */
    private Integer marriage;
    /**
     * 毕业院校
     */
    private String school;
    /**
     * 最高学历（字典）
     */
    private Integer education;
    /**
     * 行业/职业（字典）
     */
    private Integer job;
    /**
     * 年薪（字典）
     */
    private Integer salary;
    /**
     * 自我介绍
     */
    private String intro;

}
