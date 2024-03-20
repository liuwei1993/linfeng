package io.linfeng.service.user.dto.request;

import lombok.Data;


@Data
public class UserMediaRequestDTO {

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
