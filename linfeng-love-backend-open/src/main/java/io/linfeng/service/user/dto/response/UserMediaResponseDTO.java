package io.linfeng.service.user.dto.response;

import lombok.Data;

import java.io.Serializable;


@Data
public class UserMediaResponseDTO implements Serializable {

	/**
	 * 文件名称
	 */
	private String mediaName;
	/**
	 * 文件类型
	 */
	private Integer mediaType;
	/**
	 * 文件路径
	 */
	private String url;

}
