package io.linfeng.service.sms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信验证码表
 * 
 * @author linfeng
 * @date 2023-11-20 09:08:31
 */
@Data
@TableName("lf_sms_code")
public class SmsCodeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 手机号
	 */
	private String mobileNo;
	/**
	 * 验证码
	 */
	private String smsCode;
	/**
	 * 发送时间
	 */
	private Date sendTime;
	/**
	 * 发送次数
	 */
	private Integer sendCount;
	/**
	 * 失败次数
	 */
	private Integer failedCount;
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
