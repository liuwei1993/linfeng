package io.linfeng.service.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户附件表
 * 
 * @author JiangCX
 * @date 2023-08-30 14:24:24
 */
@Data
@TableName("lf_user_media")
public class UserMediaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键id
	 */
	@TableId
	private Integer id;
	/**
	 * 用户id
	 */
	private Integer uid;
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
	/**
	 * 创建时间
	 */
	private Date createTime;

}
