package io.linfeng.api.app.moment.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(description="动态请求对象")
public class MomentQueryRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 查询方式
	 */
	@ApiModelProperty(value = "查询方式")
	private Integer queryType;
	/**
	 * 特定用户uid（查询方式为特定用户）
	 */
	@ApiModelProperty(value = "特定用户uid")
	private Integer uid;
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
