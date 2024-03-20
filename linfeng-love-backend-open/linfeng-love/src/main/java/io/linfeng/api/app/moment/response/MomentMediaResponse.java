package io.linfeng.api.app.moment.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(description="动态附件对象")
public class MomentMediaResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 文件路径
	 */
	@ApiModelProperty(value = "文件路径")
	private String url;

}
