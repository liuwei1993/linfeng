package io.linfeng.api.app.moment.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(description="评论响应对象")
public class CommentDetailResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 动态id
	 */
	@ApiModelProperty(value = "动态id")
	private Integer momentId;
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
	 * 评论者姓名
	 */
	@ApiModelProperty(value = "评论者姓名")
	private String replyUserName;
	/**
	 * 评论时间
	 */
	@ApiModelProperty(value = "评论时间")
	private String createTime;

}
