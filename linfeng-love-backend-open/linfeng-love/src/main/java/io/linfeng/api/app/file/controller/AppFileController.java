/**
 * -----------------------------------
 * Copyright (c) 2021-2024
 * All rights reserved, Designed By linfeng.tech , linfengtech.cn
 * 林风婚恋交友开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/yuncoder001/linfeng-love.git
 * 商业版授权联系技术客服	 QQ:  973921677/3582996245
 * 严禁分享、盗用、转卖源码或非法牟利！
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.api.app.file.controller;

import io.linfeng.common.api.R;
import io.linfeng.service.file.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * APP文件上传
 *
 */
@RestController
@RequestMapping("app/file")
@Api(tags = "文件上传Api")
public class AppFileController {

	private final FileService fileService;

	public AppFileController(FileService fileService) {
		this.fileService = fileService;
	}

	/**
	 * 文件上传
	 * @param file 待上传文件
	 * @return 文件url
	 */
	@ApiOperation("上传文件")
	@PostMapping("/upload")
	public R upload(@RequestParam("file") MultipartFile file) {
		String url = fileService.upload(file);
		return R.ok().put("result",url);
	}



}
