package io.linfeng.api.app.moment.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(description="动态更新请求对象")
public class MomentUpdateRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * momentId
	 */
	@ApiModelProperty(value = "momentId")
	private Integer momentId;

	/**
	 * 操作类型
	 */
	@ApiModelProperty(value = "操作类型")
	private Integer operatorType;

}
