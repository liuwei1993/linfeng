package io.linfeng.service.moment.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class MomentResponseDTO {

	/**
	 * momentId
	 */
	private Integer momentId;

	/**
	 * uid
	 */
	private Integer uid;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 性别
	 */
	private Integer gender;
	/**
	 * 头像
	 */
	private String avatar;
	/**
	 * 生日
	 */
	private String birthday;
	/**
	 * 生日年份
	 */
	private String birthdayYear;
	/**
	 * 居住城市
	 */
	private String livingCity;
	/**
	 * 工作
	 */
	private Integer job;
	/**
	 * 动态内容
	 */
	private String content;
	/**
	 * 文件类型
	 */
	private Integer mediaType;
	/**
	 * 文件列表
	 */
	private List<MomentMediaResponseDTO> mediaList;
	/**
	 * 点赞数量
	 */
	@ApiModelProperty(value = "点赞数量")
	private Integer lv;
	/**
	 * 评论数量
	 */
	@ApiModelProperty(value = "评论数量")
	private Integer cv;
	/**
	 * 是否点赞
	 */
	@ApiModelProperty(value = "是否点赞")
	private Boolean likeFlag;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private String createTime;

}
