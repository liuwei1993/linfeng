import config from './config.js';
export default {
	request(options = {}) {
		return new Promise((resolve, reject) => {
			let url = options.url;
			if (url.indexOf("http://") == -1 && url.indexOf("https://") == -1) {
				options.url = config.domain + url;
			}
			options.header.token = uni.getStorageSync("token");
			options.complete = (response) => {
				if (response.statusCode == 200 || response.statusCode == 0) {
					if (response.data.code == 401 || response.data.code == 420) {
						uni.removeStorageSync("token")
						uni.navigateTo({
							url: "/pages/preload/index"
						})
					}
					if (response.data.code == 403) {
						uni.removeStorageSync("token")
						uni.showModal({
							title: '提示',
							content: '您的账号已被禁用',
							confirmText: '客服咨询',
							cancelText: '取消',
							success: function(res) {
								if (res.confirm) {
									uni.navigateTo({
										url:'/pages/user/contact/index'
									})
								}
							}
						});
					}

					if (response.data.code == 500) {
						uni.showToast({
							title: response.data.msg,
							icon: "none",
							duration: 2000
						});
					}
					resolve(response.data)
				} else {
					uni.showToast({
						title: '请求异常',
						icon: "none"
					});
				}
			}

			uni.request(options)
		})
	},

	post(url, data = {}, header = {}) {

		let options = {
			url: url,
			data: data,
			header: header,
			method: "POST"
		}

		return this.request(options);
	},

	get(url, data = {}, header = {}) {
		let options = {
			url: url,
			data: data,
			header: header
		}

		return this.request(options);
	}
};