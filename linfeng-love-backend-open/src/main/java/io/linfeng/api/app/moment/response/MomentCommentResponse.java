package io.linfeng.api.app.moment.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@ApiModel(description="用户评论响应对象")
public class MomentCommentResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 评论id
	 */
	@ApiModelProperty(value = "评论id")
	private Integer commentId;
	/**
	 * 评论内容
	 */
	@ApiModelProperty(value = "评论内容")
	private String content;
	/**
	 * 评论者uid
	 */
	@ApiModelProperty(value = "评论者uid")
	private Integer replyUid;
	/**
	 * 评论者用户名
	 */
	@ApiModelProperty(value = "评论者用户名")
	private String replyUserName;
	/**
	 * 评论者头像
	 */
	@ApiModelProperty(value = "评论者头像")
	private String avatar;
	/**
	 * 性别
	 */
	@ApiModelProperty(value = "性别")
	private Integer gender;
	/**
	 * 生日
	 */
	@ApiModelProperty(value = "生日")
	private String birthday;
	/**
	 * 生日
	 */
	@ApiModelProperty(value = "生日年份")
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
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private String createTime;

	/**
	 * 子评论列表
	 */
	@ApiModelProperty(value = "子评论列表")
	private List<MomentCommentResponse> childCommentList;

}
