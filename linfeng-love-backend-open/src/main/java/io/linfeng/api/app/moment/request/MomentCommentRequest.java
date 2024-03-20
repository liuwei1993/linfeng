package io.linfeng.api.app.moment.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(description="评论请求对象")
public class MomentCommentRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 动态Id
	 */
	@ApiModelProperty(value = "动态Id")
	private Integer momentId;

	/**
	 * 评论Id
	 */
	@ApiModelProperty(value = "评论Id")
	private Integer commentId;

	/**
	 * 内容
	 */
	@ApiModelProperty(value = "内容")
	private String content;

}
