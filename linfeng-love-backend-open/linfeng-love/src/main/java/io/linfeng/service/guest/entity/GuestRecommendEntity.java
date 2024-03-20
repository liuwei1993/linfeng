package io.linfeng.service.guest.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 推荐用户信息表
 * 
 * @author JiangCX
 * @date 2023-09-01 15:20:59
 */
@Data
@TableName("lf_guest_recommend")
public class GuestRecommendEntity implements Serializable {
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
	 * 嘉宾id
	 */
	private Integer guestUid;
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
