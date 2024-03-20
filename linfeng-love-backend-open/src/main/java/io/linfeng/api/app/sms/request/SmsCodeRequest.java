package io.linfeng.api.app.sms.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "短信验证码请求对象")
public class SmsCodeRequest {

    @ApiModelProperty(value = "手机号")
    private String mobileNo;

}
