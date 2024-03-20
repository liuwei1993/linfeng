
<template>
	<view>
		<view class="content">
			<MomentList ref="momentListRef" :queryForm="queryForm"></MomentList>
		</view>
	</view>
	
</template>

<script setup>
import { inject, ref, onMounted, nextTick } from "vue";
import MomentList from "./MomentList.vue";
import { onLoad, onPullDownRefresh, onShareAppMessage } from "@dcloudio/uni-app";
import request from '@/utils/request.js';

const $store = inject('$store')
const userInfo = $store.state.loginUser

const queryForm = ref({
		queryType: 5,
		oid: userInfo.oid,
		pageNum:1,
		pageSize:15
})
const momentListRef = ref()

onShareAppMessage((res) => {
	
	if (res.from === 'button') {
		let momentDetail = res.target.dataset.item
		let imageUrl
		if(momentDetail.mediaType == '1' && momentDetail.mediaList.length == 0){
			imageUrl = momentDetail.avatar
		}else if(momentDetail.mediaType == '1' && momentDetail.mediaList.length != 0){
			imageUrl = momentDetail.mediaList[0].url
		}else if(momentDetail.mediaType == '2'){
			imageUrl = momentDetail.avatar
		}else{
			imageUrl = momentDetail.avatar
		}
		return {
			title: momentDetail.userName + '发布了一条动态，快来看看吧',
			path: '/pages/moment/detail?momentId=' + res.target.id,
			imageUrl: imageUrl,
		}
	}
})

onMounted(() => {
	
	nextTick(() => {
	  momentListRef.value.refreshMomentList()
	})
})

onPullDownRefresh(() => {
	momentListRef.value.addMomentList()
	uni.stopPullDownRefresh() //停止刷新

})

const tabChange = (index) => {
	pageCurrent.value = index
	if(index == 0){
		queryForm.value.queryType = 1
	}
	if(index == 1){
		queryForm.value.queryType = 3
	}
	if(index == 2){
		queryForm.value.queryType = 2
	}
	momentListRef.value.addMomentList()
	nextTick(() => {
	  uni.pageScrollTo({
	  	scrollTop: 0,
	  	duration: 0
	  })
	})
	
}

</script>

<style lang="scss" scoped>
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
			position: absolute;
			margin-left: 38%;
		}
	}
	.talk-wrap{
		background-color: $lf-bg-blue-week;
		margin: 20rpx 30rpx;
		border-radius: 20rpx;
		padding-bottom: 10rpx;
		.header{
			display: flex;
			padding: 20rpx 10rpx 10rpx 30rpx;
			image{
				width: 40rpx;
				height: 40rpx;
				border-radius: 10rpx;
			}
			.txt{
				font-size: 28rpx;
				margin-left: 20rpx;
			}
			.more{
				margin-right: 20rpx;
				margin-left: auto;
				color: $lf-text-grey;
			}
		}
		.out-wrap{
			display: flex;
			flex-wrap: wrap;
			background-color: $lf-bg-white;
			border-radius: 5rpx;
			margin: 15rpx 10rpx;
			padding: 0 20rpx;
			.item{
				display: flex;
				justify-content: center;
				align-items: center;
				width: 48%;
				padding: 15rpx 0;
				border-radius: 20rpx;
				.txt{
					display: flex;
					flex-direction: column;
					flex: 1;
					margin-left: 10rpx;
					.title{
						font-size: 28rpx;
					}
					.browse{
						font-size: 25rpx;
						color: $lf-text-grey;
					}
				}
				.time{
					font-size: 23rpx;
					color: $lf-text-grey;
				}
				.icon {
					position: relative;
					width: 60rpx;
					height: 60rpx;
					border-radius: 10rpx;
					justify-content: center;
					align-items: center;
					display: flex;
					margin-right: 5rpx;
				}
			}
		}
	}
	.content{
		margin-top: v-bind(topHeight);
	}
	.new-moment{
		image{
			position: fixed;
			right: 50rpx;
			bottom: 100rpx;
			width: 100rpx;
			height: 100rpx;
			border-radius: 100rpx;
		}
	}
	.post-text {
		display: block;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 10;
		white-space: pre-wrap;
		overflow: hidden;
		margin: 20rpx 10rpx;
	}
</style>
