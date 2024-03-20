
package io.linfeng.service.config.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典项
 *
 */
@Data
@TableName("lf_dict_item")
public class DictItemEntity implements Serializable {


	public static final String CACHE_DICT_PREFIX = "dict:";
	private static final long serialVersionUID = 1L;
	
	/**
	 * ID
	 */
	@TableId(value = "id",type = IdType.AUTO)
	private Integer id;

	/**
	 * 类型CODE
	 */
	private String typeCode;

	/**
	 * 字典项标识码
	 */
	private Integer code;

	/**
	 * 字典项名称
	 */
	private String name;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	
}
