package io.linfeng.api.app.moment.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@ApiModel(description="动态响应对象")
public class MomentResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * momentId
	 */
	@ApiModelProperty(value = "momentId")
	private Integer momentId;

	/**
	 * uid
	 */
	@ApiModelProperty(value = "uid")
	private Integer uid;
	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
	private String userName;
	/**
	 * 性别
	 */
	@ApiModelProperty(value = "性别")
	private Integer gender;
	/**
	 * 头像
	 */
	@ApiModelProperty(value = "头像")
	private String avatar;
	/**
	 * 生日
	 */
	@ApiModelProperty(value = "生日")
	private String birthday;
	/**
	 * 出生年份
	 */
	@ApiModelProperty(value = "出生年份")
	private String birthdayYear;
	/**
	 * 居住城市
	 */
	@ApiModelProperty(value = "居住城市")
	private String livingCity;
	/**
	 * 工作
	 */
	@ApiModelProperty(value = "工作")
	private Integer job;
	/**
	 * 工作（字典翻译）
	 */
	@ApiModelProperty(value = "工作（字典翻译）")
	private String jobText;
	/**
	 * 动态内容
	 */
	@ApiModelProperty(value = "动态内容")
	private String content;
	/**
	 * 文件类型
	 */
	@ApiModelProperty(value = "文件类型")
	private Integer mediaType;
	/**
	 * 文件列表
	 */
	@ApiModelProperty(value = "文件列表")
	private List<MomentMediaResponse> mediaList;
	/**
	 * 点赞数量
	 */
	@ApiModelProperty(value = "点赞数量")
	private Integer lv;
	/**
	 * 评论数量
	 */
	@ApiModelProperty(value = "评论数量")
	private Integer cv;
	/**
	 * 是否点赞
	 */
	@ApiModelProperty(value = "是否点赞")
	private Boolean likeFlag;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private String createTime;

}
