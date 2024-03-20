package io.linfeng.service.file.dto.request;

import lombok.Data;

import java.io.Serializable;


@Data
public class FileRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 文件流
	 */
	private byte[] data;

	/**
	 * 文件后缀
	 */
	private String suffix;

}
