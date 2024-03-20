package io.linfeng.service.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author JiangCX
 * @email 2445465217@qq.com
 * @date 2023-01-20 12:10:43
 */
@Data
@TableName("lf_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	@TableId(value = "uid",type = IdType.AUTO)
	private Integer uid;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 头像
	 */
	private String avatar;
	/**
	 * 性别（字典）
	 */
	private Integer gender;
	/**
	 * 生日
	 */
	private String birthday;
	/**
	 * 年龄
	 */
	private Integer age;
	/**
	 * 身高
	 */
	private Integer stature;
	/**
	 * 体重
	 */
	private Integer weight;
	/**
	 * 家乡省份
	 */
	private String homeProvince;
	/**
	 * 家乡城市
	 */
	private String homeCity;
	/**
	 * 居住省份
	 */
	private String livingProvince;
	/**
	 * 居住城市
	 */
	private String livingCity;
	/**
	 * 婚姻状态（字典）
	 */
	private Integer marriage;
	/**
	 * 毕业院校
	 */
	private String school;
	/**
	 * 最高学历（字典）
	 */
	private Integer education;
	/**
	 * 行业/职业（字典）
	 */
	private Integer job;
	/**
	 * 年薪（字典）
	 */
	private Integer salary;
	/**
	 * 最后登录ip
	 */
	private String lastLoginIp;
	/**
	 * 自我介绍
	 */
	private String intro;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
