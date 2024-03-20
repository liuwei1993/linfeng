package io.linfeng.service.moment.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 动态评论表
 * 
 * @author JiangCX
 * @date 2023-10-10 10:46:11
 */
@Data
@TableName("lf_moment_comment")
public class MomentCommentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 动态id
	 */
	private Integer momentId;
	/**
	 * 评论者ID
	 */
	private Integer replyUid;
	/**
	 * 评论内容
	 */
	private String content;
	/**
	 * 评论状态
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
