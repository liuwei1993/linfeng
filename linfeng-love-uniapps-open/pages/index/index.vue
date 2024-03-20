<template>
	
	<view class="index">
		<!-- 导航栏 -->
		<view class="custom-nav">
				<u-icon name="clock" size="50" class="clock-wrap" @click="toNav('/pages/guest/history')"></u-icon>
				<u-tabs class="tabs" :list="pageTab" :current="pageCurrent" @change="tabChange" active-color="#2F85FC"></u-tabs>
		</view>
		<view>
			<view v-if="guestUser != null">
				<Guest :guestUser="guestUser" :topHeight="topHeight" :showNext="true"  @on-operator="guestOperator"></Guest>
			</view>
			<view v-else>
				<view class="prompt-wrap">
					<image class="icon" src="/static/image/no-data.png"></image>
					<text class="txt">没有合适的嘉宾</text>
				</view>
			</view>
		</view>
		<LfLoading v-if="showLoading"></LfLoading>
	</view>
</template>

<script setup>
import { inject, ref, watch } from "vue";
import { onLoad, onShow } from "@dcloudio/uni-app";
import request from '@/utils/request.js'
import Guest from '@/pages/guest/Guest.vue'
import LfLoading from '@/components/lf-loading/lf-loading.vue'

const $store = inject('$store')
const userInfo = $store.state.loginUser
const showLoading = ref(false)
const navHeight = ref('0px')
const statusBarHeight = ref('0px')
const topHeight = ref('0px')
const pageCurrent = ref(0)
const pageTab = ref([{
						name: '推荐'
					}
				])
const guestUser = ref({
})

const showAction = ref(false)

const showNext = ref(false)

onLoad(() => {
	navHeight.value = getApp().globalData.navHeight + 'px';
	statusBarHeight.value = getApp().globalData.statusBarHeight + 'px';
	topHeight.value = getApp().globalData.navHeight + getApp().globalData.statusBarHeight + 'px';
	newRecommend()
	
})

onShow(() => {
})

const guestOperator = () => {
	newRecommend()
}

const newRecommend = () => {
	showLoading.value = true
	request.post('guest/recommend/new').then(res => {
		showLoading.value = false
		guestUser.value = res.result		
	})
}


const toNav = (url) => {
	uni.navigateTo({
		url: url
	});
}

const tabChange = (index) => {
	pageCurrent.value = index;
}

</script>

<style lang="scss" scoped>
.index{
	.custom-nav{
		height: v-bind(navHeight);
		top: v-bind(statusBarHeight);
		display: flex;
		align-items: center;
		position: fixed;
		background-color: $lf-bg-white;
		width: 100%;
		z-index: 99;
		
		.tabs{
			//position: absolute;
			margin: 0 auto;
		}
		.clock-wrap{
			position: absolute;
			left: 20rpx;
		}
		.thumb-wrap{
			position: absolute;
			left: 90rpx;
		}
		.bottle-wrap{
			width: 50rpx;
			height: 50rpx;
			position: absolute;
			left: 160rpx;
		}
		.bottle{
			width: 50rpx;
			height: 50rpx;
			position: absolute;
			right: 20rpx;
		}
	}
	
}
.prompt-wrap{
		display: flex;
		justify-content: center;
		align-items: center;
		flex-direction: column;
		margin-top: 500rpx;
		.icon{
			width: 300rpx;
			height: 200rpx;
		}
		.txt{
			font-size: 28rpx;
			margin-top: 40rpx;
			margin-bottom: 40rpx;
			color: $lf-text-grey;
		}
	}
	
</style>
