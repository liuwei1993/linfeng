package io.linfeng.service.moment.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 帖子收藏表(废弃)
 * 
 * @author JiangCX
 * @date 2023-10-11 08:39:17
 */
@Data
@TableName("lf_moment_like")
public class MomentLikeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 用户id
	 */
	private Integer uid;
	/**
	 * 动态id
	 */
	private Integer momentId;
	/**
	 * 状态
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
