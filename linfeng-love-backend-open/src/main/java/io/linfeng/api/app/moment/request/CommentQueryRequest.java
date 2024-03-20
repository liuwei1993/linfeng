package io.linfeng.api.app.moment.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(description="评论请求对象")
public class CommentQueryRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 动态Id
	 */
	@ApiModelProperty(value = "动态Id")
	private Integer momentId;
	/**
	 * 页数
	 */
	@ApiModelProperty(value = "页数")
	private Integer pageNum;
	/**
	 * 页码
	 */
	@ApiModelProperty(value = "页码")
	private Integer pageSize;

}
