/**
 * -----------------------------------
 *  Copyright (c) 2021-2024
 *  All rights reserved, Designed By linfeng.tech & linfengtech.cn
 *  林风婚恋交友开源版本请务必保留此注释头信息
 *  开源地址: https://gitee.com/yuncoder001/linfeng-love.git
 *  商业版授权联系技术客服	 QQ:  973921677/3582996245
 *  项目官网：https://net.linfeng.tech
 *  严禁盗用、转卖源码或非法牟利！
 *  版权所有 ，侵权必究！
 * -----------------------------------
 */
//环境配置
const baseUrl = "127.0.0.1:8080"; //你本地测试的接口地址
const domain = 'http://' + baseUrl + "/app/"; //接口服务地址
const uploadUrl = domain + 'file/upload'; //文件上传地址

export default {
	baseUrl: baseUrl,
	domain: domain,
	uploadUrl: uploadUrl
}
