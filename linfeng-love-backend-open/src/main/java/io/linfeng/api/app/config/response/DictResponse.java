package io.linfeng.api.app.config.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(description="字典响应实体")
public class DictResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 类型CODE
	 */
	@ApiModelProperty(value = "字典项标识码")
	private String code;
	/**
	 * 字典项名称
	 */
	@ApiModelProperty(value = "字典项名称")
	private String name;

}
