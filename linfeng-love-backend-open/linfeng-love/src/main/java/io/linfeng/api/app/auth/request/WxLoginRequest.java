
package io.linfeng.api.app.auth.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description="微信登录请求对象")
public class WxLoginRequest {

    /**
     * 微信code
     */
    @ApiModelProperty(value = "微信code", required = true)
    @NotBlank(message="微信code不能为空")
    private String code;

    /**
     * 加密数据
     */
    @ApiModelProperty(value = "加密数据", required = true)
    @NotBlank(message="加密数据不能为空")
    private String encryptedData;

    /**
     * 加密算法初始向量
     */
    @ApiModelProperty(value = "加密算法初始向量", required = true)
    @NotBlank(message="加密算法初始向量不能为空")
    private String iv;

}
