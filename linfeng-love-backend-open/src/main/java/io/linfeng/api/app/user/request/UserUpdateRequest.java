
package io.linfeng.api.app.user.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * app用户信息修改
 *
 */
@Data
@ApiModel(description = "用户信息修改请求对象")
public class UserUpdateRequest {

    /**
     * 手机号
     */
    
    @ApiModelProperty(value = "手机号")
    private String mobile;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;
    /**
     * 性别(0未知，1男，2女)
     */
    @ApiModelProperty(value = "性别")
    private Integer gender;
    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    private String birthday;
    /**
     * 身高
     */
    @ApiModelProperty(value = "身高")
    private Integer stature;
    /**
     * 体重
     */
    @ApiModelProperty(value = "体重")
    private Integer weight;
    /**
     * 家乡省份
     */
    @ApiModelProperty(value = "家乡省份")
    private String homeProvince;
    /**
     * 家乡城市
     */
    @ApiModelProperty(value = "家乡城市")
    private String homeCity;
    /**
     * 居住省份
     */
    @ApiModelProperty(value = "居住省份")
    private String livingProvince;
    /**
     * 居住城市
     */
    @ApiModelProperty(value = "居住城市")
    private String livingCity;
    /**
     * 婚姻状态（字典）
     */
    @ApiModelProperty(value = "婚姻状态")
    private Integer marriage;
    /**
     * 毕业院校
     */
    @ApiModelProperty(value = "毕业院校")
    private String school;
    /**
     * 最高学历（字典）
     */
    @ApiModelProperty(value = "最高学历")
    private Integer education;
    /**
     * 行业/职业（字典）
     */
    @ApiModelProperty(value = "行业/职业")
    private Integer job;
    /**
     * 年薪（字典）
     */
    @ApiModelProperty(value = "年薪")
    private Integer salary;
    /**
     * 自我介绍
     */
    @ApiModelProperty(value = "自我介绍")
    private String intro;

}
