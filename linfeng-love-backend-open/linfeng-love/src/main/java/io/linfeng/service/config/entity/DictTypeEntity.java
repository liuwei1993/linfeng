package io.linfeng.service.config.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据字典类型表
 * 
 * @author JiangCX
 * @date 2023-11-13 10:09:12
 */
@Data
@TableName("lf_dict_type")
public class DictTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId
	private Integer id;
	/**
	 * 标识、编码
	 */
	private String code;
	/**
	 * 类型名称
	 */
	private String name;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Date updateTime;

}
