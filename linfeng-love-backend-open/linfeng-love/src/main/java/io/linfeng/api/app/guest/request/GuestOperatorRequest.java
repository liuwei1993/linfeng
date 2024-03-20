package io.linfeng.api.app.guest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;



@Data
@ApiModel(description="嘉宾操作请求对象")
public class GuestOperatorRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户uid
	 */
	@ApiModelProperty(value = "用户uid")
	private Integer uid;

	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态")
	private Integer status;

}
