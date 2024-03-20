
package io.linfeng.api.app.auth.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="手机验证码登录请求对象")
public class SmsLoginRequest {

    @ApiModelProperty(value = "手机号")
    private String mobileNo;

    @ApiModelProperty(value = "验证码")
    private String code;

}
