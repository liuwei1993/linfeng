package io.linfeng.service.moment.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 我的动态表
 * 
 * @author JiangCX
 * @date 2023-09-28 09:29:05
 */
@Data
@TableName("lf_moment")
public class MomentEntity implements Serializable {
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
	 * 内容
	 */
	private String content;
	/**
	 * 附件类型：1图片2视频
	 */
	private Integer mediaType;
	/**
	 * 浏览量
	 */
	private Integer pv;
	/**
	 * 点赞量
	 */
	private Integer lv;
	/**
	 * 评论量
	 */
	private Integer cv;
	/**
	 * 状态2正常-1下架
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
