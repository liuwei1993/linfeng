<template>
	<!-- #ifdef MP-WEIXIN -->
	<privacy @agreePrivacy="agreePrivacy"></privacy>
	<!-- #endif -->
</template>

<script setup>
import { inject, ref } from "vue";
import { onLoad } from "@dcloudio/uni-app";
import request from '@/utils/request.js'
import privacy from '@/components/privacy/privacy.vue'

const $store = inject('$store')

onLoad(() => {
	// #ifdef H5
	if (uni.getStorageSync('token') == null || uni.getStorageSync('token') == '') {
		uni.reLaunch({
			url:"/pages/guide/index"
		})
	}else{
		getUserInfo()
	}
	// #endif
})
const agreePrivacy = () => {
	if (uni.getStorageSync('token') == null || uni.getStorageSync('token') == '') {
		uni.reLaunch({
			url:"/pages/preload/mobile"
		})
	}else{
		getUserInfo()
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
			//获取会话列表
			uni.switchTab({
				url:"/pages/index/index"
			})
		}
	})
}
</script>

<style lang="scss">
</style>