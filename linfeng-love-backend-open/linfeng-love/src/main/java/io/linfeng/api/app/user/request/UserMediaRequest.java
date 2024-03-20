package io.linfeng.api.app.user.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(description="用户附件请求对象")
public class UserMediaRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 文件名称
	 */
	@ApiModelProperty(value = "文件名称")
	private String mediaName;
	/**
	 * 文件类型
	 */
	@ApiModelProperty(value = "文件类型")
	private Integer mediaType;
	/**
	 * 文件路径
	 */
	@ApiModelProperty(value = "文件路径")
	private String url;

}
