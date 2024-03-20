
package io.linfeng.service.config.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * 字典请求对象
 */
@Data
@ApiModel(value = "字典请求对象")
public class DictRequest {

    @ApiModelProperty(value = "typeCode",required = true)
    @NotBlank(message="typeCode不能为空")
    private String typeCode;
}
