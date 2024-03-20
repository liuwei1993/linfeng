package io.linfeng.service.moment.dto.response;

import lombok.Data;

import java.util.List;


@Data
public class MomentCommentResponseDTO {


	/**
	 * 评论id
	 */
	private Integer commentId;
	/**
	 * 评论内容
	 */
	private String content;
	/**
	 * 评论者oid
	 */
	private Integer replyUid;
	/**
	 * 评论者用户名
	 */
	private String replyUserName;
	/**
	 * 评论者头像
	 */
	private String avatar;
	/**
	 * 性别
	 */
	private Integer gender;
	/**
	 * 生日
	 */
	private String birthday;
	/**
	 * 居住城市
	 */
	private String livingCity;
	/**
	 * 工作
	 */
	private Integer job;
	/**
	 * 创建时间
	 */
	private String createTime;

}
