//环境配置
const baseUrl = "127.0.0.1:8080"; //你本地测试的接口地址
const domain = 'http://' + baseUrl + "/app/"; //接口服务地址
const uploadUrl = domain + 'file/upload'; //文件上传地址

export default {
	baseUrl: baseUrl,
	domain: domain,
	uploadUrl: uploadUrl
}
