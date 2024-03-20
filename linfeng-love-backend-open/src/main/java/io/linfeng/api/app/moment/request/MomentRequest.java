package io.linfeng.api.app.moment.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@ApiModel(description="动态发布请求对象")
public class MomentRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * momentId
	 */
	@ApiModelProperty(value = "momentId")
	private Integer momentId;

	/**
	 * 内容
	 */
	@ApiModelProperty(value = "内容")
	private String content;
	/**
	 * 附件类型
	 */
	@ApiModelProperty(value = "附件类型")
	private Integer mediaType;

	/**
	 * 附件列表
	 */
	@ApiModelProperty(value = "附件列表")
	private List<MomentMediaRequest> mediaList;

	/**
	 * 操作类型
	 */
	@ApiModelProperty(value = "操作类型")
	private Integer operatorType;

}
