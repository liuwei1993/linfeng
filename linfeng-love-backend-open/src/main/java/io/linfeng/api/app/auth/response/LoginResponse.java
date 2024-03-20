
package io.linfeng.api.app.auth.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "登录响应对象")
public class LoginResponse {

    @ApiModelProperty(value = "token")
    private String token;

}
