package io.linfeng.service.moment.dto.request;

import lombok.Data;


@Data
public class CommentQueryRequestDTO {

	/**
	 * 动态Id
	 */
	private Integer momentId;
	/**
	 * 页数
	 */
	private Integer pageNum;
	/**
	 * 页码
	 */
	private Integer pageSize;

}
