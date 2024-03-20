package io.linfeng.api.app.moment.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@ApiModel(description="动态图片对象")
public class MomentImageResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 动态数量
	 */
	@ApiModelProperty(value = "动态数量")
	private Integer momentListLength;

	/**
	 * 文件列表
	 */
	@ApiModelProperty(value = "文件列表")
	private List<MomentMediaResponse> momentMediaList;

}
