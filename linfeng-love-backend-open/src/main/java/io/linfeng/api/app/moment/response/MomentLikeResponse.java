package io.linfeng.api.app.moment.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(description="动态点赞响应对象")
public class MomentLikeResponse implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * uid
	 */
	@ApiModelProperty(value = "uid")
	private Integer uid;
	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
	private String userName;
	/**
	 * 头像
	 */
	@ApiModelProperty(value = "头像")
	private String avatar;


}
