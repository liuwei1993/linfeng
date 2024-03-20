<template>
	<view v-show="noLogin" class="container flex-items-plus flex-column">
		<view class="login-logoBox">
			<image class="login-logo" src="/static/image/linfeng.png"></image>
		</view>
		<view class="iphoneNum-box flex-row-plus flex-items">
			<view style="margin-right: 30rpx">
				<image class="loginIcon" src="/static/image/mobile.png"></image>
			</view>
			<view>
				<input v-model="submitForm.mobileNo" placeholder-class="iphoneNum-input" type="number" maxlength='11'
					placeholder="请输入您的手机号" />
			</view>
		</view>
		<view class="flex-row-plus mar-top-20 sms-code-row">
			<view class="code-box">
				<view style="margin-right: 30rpx">
					<image class="loginIcon" src="/static/image/sms-code.png"></image>
				</view>
				<view>
					<input v-model="submitForm.code" placeholder-class="codeNum-input" placeholder="请输入验证码" />
				</view>
			</view>
			<view :class="sendable === true ? 'on' : 'off'" class="getcode" @click="sendCode">
				{{ getSmsCodeText }}
			</view>
		</view>
		<view class="mar-top-60">
			<view class="registerBut mar-top-100" @click="smsLogin">登录
			</view>
		</view>
		<view class="flex-row-plus mar-top-30">
			<text class="font-colors">未注册手机验证后自动注册登录</text>
		</view>
	</view>
</template>



<script setup>
import { inject, ref } from "vue";
import { onLoad } from "@dcloudio/uni-app";
import request from '@/utils/request.js';

const $store = inject('$store')
const getSmsCodeText = ref("获取验证码")
const submitForm = ref({})
const sendable = ref(true)
const noLogin = ref(false)
const protocolAgree = ref(false)

onLoad(() => {
	if (uni.getStorageSync('token') == null || uni.getStorageSync('token') == '') {
		noLogin.value = true
	}else{
		getUserInfo()
	}
})

const sendCode = () => {
	let phoneCodeVerification = /^[1][3-9][0-9]{9}$/;
	if (submitForm.value.mobileNo == '' || submitForm.value.mobileNo == undefined
	|| !phoneCodeVerification.test(submitForm.value.mobileNo)) {
		uni.showToast({
			title:'请输入正确的手机号',
			icon: 'none'
		})
		return
	}
	
	request.post("sms/send", submitForm.value).then(res => {
		if(res.code == 0){
			uni.showToast({
				title: '验证码发送成功',
				icon: 'none',
			})
			submitForm.value.code = res.msg
			
		}else{
			uni.showToast({
				title: res.msg,
				icon: 'none',
			})
		}
		
	})
	
	if (!sendable.value) return;
	sendable.value = false;
	let n = 60;
	getSmsCodeText.value = "剩余 " + n + "s";
	let run = setInterval(() => {
	n = n - 1;
	if (n < 0) {
	  clearInterval(run);
	}
	getSmsCodeText.value = "剩余 " + n + "s";
	if (getSmsCodeText.value < "剩余 " + 0 + "s") {
	  sendable.value = true;
	  getSmsCodeText.value = "重新获取";
	}
	}, 1000);
}

const smsLogin = () => {
	let phoneCodeVerification = /^[1][3-9][0-9]{9}$/;
	if (submitForm.value.mobileNo == '' || submitForm.value.mobileNo == undefined
	|| !phoneCodeVerification.test(submitForm.value.mobileNo)) {
		uni.showToast({
			title:'请输入正确的手机号',
			icon: 'none'
		})
		return
	}
	let codeVerification = /^[0-9]{6}$/;
	if (submitForm.value.code == '' || submitForm.value.code == undefined
	|| !codeVerification.test(submitForm.value.code)) {
		uni.showToast({
			title:'请输入6位数字验证码',
			icon: 'none'
		})
		return
	}
	request.post('auth/smsLogin', submitForm.value).then(res => {
		uni.setStorageSync("token", res.result.token);
		getUserInfo()
	})
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

<style lang="scss" scoped>
	.container {
		background-color: #FFFFFF;
		min-height: 100%;
		width: 100%;
		position: absolute;

		.login-logoBox {
			margin-top: -300rpx;

			.login-logo {
				width: 200rpx;
				height: 200rpx;
			}
		}

		.iphoneNum-box {
			margin-top: 100rpx;
			border-bottom: 1rpx solid #DDDDDD;
			height: 100rpx;
			width: 600rpx;

			.loginIcon {
				width: 40rpx;
				height: 53rpx;
			}

			.iphoneNum-input {
				color: #999999;
				font-size: 28rpx;
				font-weight: 400;
			}
		}
		.sms-code-row{
			border-bottom: 1rpx solid #DDDDDD;
		}

		.passwordNum-box {
			border-bottom: 1rpx solid #DDDDDD;
			height: 100rpx;
			width: 600rpx;

			.passwordNum-input {
				color: #999999;
				font-size: 28rpx;
				font-weight: 400;
				width: 346rpx;
			}
		}

		.code-box {
			height: 100rpx;
			width: 360rpx;
			display: flex;
			flex-direction: row;
			justify-content: space-between;
			align-items: center;

			.loginIcon {
				width: 44rpx;
				height: 50rpx;
			}

			.code-lab {
				width: 200rpx;
			}

			.codeNum-input {
				color: #999999;
				font-size: 28rpx;
				font-weight: 400;
			}
		}
		.on{
			color: $lf-text-blue;
		}
		.off{
			color: $lf-text-grey;
		}
		.getcode {
			height: 100rpx;
			width: 230rpx;
			display: flex;
			flex-direction: row;
			justify-content: center;
			align-items: center;
			margin-left: 20rpx;
		}

		.registerBut {
			background: $lf-bg-blue;
			color: #efece8;
			height: 100rpx;
			width: 600rpx;
			text-align: center;
			line-height: 100rpx;
			margin-top: 30rpx;
		}
	}

	.flex-items-plus {
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.flex-column {
		flex-direction: column
	}

	.flex-items {
		display: flex;
		align-items: center;
	}

	.flex-row-plus {
		display: flex;
		flex-direction: row
	}
	
	.protocol-wrap{
		padding: 0 30rpx;
	}

	.mar-top-20 {
		margin-top: 20upx;
	}

	.mar-top-60 {
		margin-top: 60upx;
	}

	.mar-top-100 {
		margin-top: 100upx;
	}

	.mar-top-30 {
		margin-top: 30upx;
	}
	.agree{
		margin-left: 30rpx;
	}
	.agree ::v-deep .u-checkbox__label{
		margin-right: 0;
	}

	.font-colors {
		color: #adaca9;
	}
	.protocol{
		color: $lf-text-blue
	}
	.register-text {
		color: #8b857d;
	}
	.u-checkbox__label{
		margin-right: 0;
	}
</style>
