package io.linfeng.api.app.guest.response;

import io.linfeng.api.app.user.response.UserMediaResponse;
import io.linfeng.api.app.moment.response.MomentMediaResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@ApiModel(description="嘉宾信息响应对象")
public class GuestResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 嘉宾uid
	 */
	@ApiModelProperty(value = "嘉宾uid")
	private Integer uid;
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
	 * 性别
	 */
	@ApiModelProperty(value = "性别")
	private Integer gender;
	/**
	 * 性别(字典翻译)
	 */
	@ApiModelProperty(value = "性别(字典翻译)")
	private String genderText;
	/**
	 * 出生年份
	 */
	@ApiModelProperty(value = "出生年份")
	private String birthdayYear;
	/**
	 * 年龄
	 */
	@ApiModelProperty(value = "年龄")
	private Integer age;
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
	 * 婚姻状态(字典翻译)
	 */
	@ApiModelProperty(value = "婚姻状态(字典翻译)")
	private String marriageText;
	/**
	 * 毕业院校
	 */
	@ApiModelProperty(value = "毕业院校")
	private String school;
	/**
	 * 最高学历(字典翻译)
	 */
	@ApiModelProperty(value = "最高学历(字典翻译)")
	private String educationText;
	/**
	 * 行业/职业(字典翻译)
	 */
	@ApiModelProperty(value = "行业/职业(字典翻译)")
	private String jobText;
	/**
	 * 年薪(字典翻译)
	 */
	@ApiModelProperty(value = "年薪(字典翻译)")
	private String salaryText;
	/**
	 * 自我介绍
	 */
	@ApiModelProperty(value = "自我介绍")
	private String intro;

	/**
	 * 用户附件
	 */
	@ApiModelProperty(value = "用户附件")
	private List<UserMediaResponse> userMediaList;

	/**
	 * 动态数量
	 */
	@ApiModelProperty(value = "动态数量")
	private Integer momentListLength;

	/**
	 * 动态附件
	 */
	@ApiModelProperty(value = "动态附件")
	private List<MomentMediaResponse> momentMediaList;


}
