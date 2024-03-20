<template>
	<view>
		<view class="login">
			<image class="logo" src="/static/image/linfeng.png"></image>
			<text class="txt1">申请获取手机号登录</text>
			<text class="txt2">根据《网络安全法》等规定发帖评论等需要先绑定手机号哦</text>
			<button open-type="getPhoneNumber" @getphonenumber="getPhoneNumber" class="submit">
				手机号授权</button>
		</view>
	</view>
</template>

	
<script setup>
import { inject, ref } from "vue";
import { onLoad } from "@dcloudio/uni-app";
import request from '@/utils/request.js';
const $store = inject('$store')

onLoad(() => {
	uni.login({
		provider: 'weixin',
		success: function(loginRes) {
		}
	});
})

const getLoginCode = () => {
	return new Promise((resolve, reject) => {
		uni.login({
			provider: 'weixin',
			success: function(loginRes) {
				resolve(loginRes.code);
			}
		});
	});
}
const getPhoneNumber = async (e) => {
	let loginCode = await getLoginCode();
	if (e.detail.errMsg === "getPhoneNumber:ok") {
		uni.showLoading({
		  title: '绑定中',
		})
		request.post('auth/miniWxLogin', {
			code: loginCode,
		    encryptedData: e.detail.encryptedData,
		    iv: e.detail.iv
		}).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				uni.setStorageSync("token", res.result.token);
				$store.dispatch('refreshUserInfo');
				getUserInfo()
			}else{
				uni.showToast({
					title: "获取手机授权失败",
					icon: "none"
				});
			}
		}).catch(error => {
			uni.hideLoading()
			uni.showToast({
			  title: '绑定失败',
			  icon: 'none',
			  duration: 2000,
			})
		});
	} else {
		uni.showToast({
			title: "获取手机授权失败",
			icon: "none"
		});
	}
}
const getUserInfo = () => {
	
	request.get("user/userInfo").then(res => {
		let loginUser = res.result;
		$store.commit('setLoginUser', loginUser);
		if(loginUser.userName == null){
			uni.reLaunch({
				url:"/pages/guide/step"
			})
		}else{
			uni.switchTab({
				url:"/pages/index/index"
			})
		}
	})
}
</script>

<style lang="scss">
	.login {
		display: flex;
		flex-direction: column;
		padding: 100rpx;
	}

	.login .logo {
		width: 230rpx;
		height: 230rpx;
		margin: 50rpx auto;
	}

	.login .txt1 {
		margin-bottom: 10rpx;
	}

	.login .txt2 {
		margin-bottom: 50rpx;
	}

	.login .txt3 {
		color: #8c8c8c;
		margin-bottom: 90rpx;
		text-align: center;
	}

	.user-avatar {
		width: 160rpx;
		height: 160rpx;
		border-radius: 50%;
		overflow: hidden;
		margin: 50rpx auto;
	}

	.user-name {
		font-size: 35rpx;
		font-family: PingFang SC;
		font-weight: bold;
		color: #000;
		margin: 50rpx auto;
	}
	.submit{
		position: absolute;
		bottom: 50rpx;
		left: 0;
		right: 0;
		width: 50%;
		font-size: 34rpx;
		text-align: center;
		padding: 20rpx 0;
		border-radius: 40rpx;
		color: #fff;
		font-weight: bold;
		align-items: center;
		margin: 0 auto;
		z-index: 99;
		background-color: $lf-bg-blue;
		line-height: 40rpx;
	}
</style>