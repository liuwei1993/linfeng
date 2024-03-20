<template>
	<view class="guest-list-wrap">
		<view class="guest-list">
			<view v-for="(guestUser,index) in guestUsertList" :key="index"  class="guest-item">
				<view class="image-main"  @click="goDetail(guestUser, index)">
					<image mode="aspectFill" :src="guestUser.avatar"></image>
				</view>
				<view class="title">
					<text class="name">{{guestUser.userName}}</text>
					<image v-if="guestUser.gender == 1" mode="aspectFill" class="like-icon" src="/static/image/man-color.png"></image>
					<image v-else mode="aspectFill" class="like-icon" src="/static/image/woman-color.png"></image>
				</view>
				<text class="age">{{guestUser.age}}岁 · {{guestUser.stature}}cm</text>
				<u-line color="#ccc" margin="10rpx 0 0 0"></u-line>
			</view>
			
		</view>
		
	</view>
	
</template>

<script setup>
import { inject, ref, defineProps, toRefs } from "vue";
import { onShow } from "@dcloudio/uni-app";
import request from '@/utils/request.js'

const $store = inject('$store')

const props = defineProps({
  guestUsertList: {
    type: Array,
    default: []
  }
})

const {guestUsertList} = toRefs(props)

const userInfo = ref({})

const inputBottom = ref('0rpx')

onShow(() => {
	userInfo.value = $store.state.loginUser
})

const goDetail = (guestUser, index) => {
	uni.navigateTo({
		url:'/pages/guest/detail?uid=' + guestUser.uid
	})
}

const toNav = (url) => {
	uni.navigateTo({
		url: url
	});
}

</script>

<style>
</style>
<style lang="scss" scoped>
	.guest-list-wrap{
		position: absolute;
		background-color: $lf-bg-grey-week;
		width: 100%;
		min-height: 100%;
		.guest-list{
			display: flex;
			flex-wrap: wrap;
			.guest-item{
				margin: 2% 1.5%;
				width: 47%;
				background-color: $lf-bg-white;
				border-radius: 20rpx;
				.image-main{
					position: relative;
					padding: 10rpx;
					image{
						width: 100%;
						height: 300rpx;
						border-radius: 20rpx;
					}
					.filter-blur{
						filter: blur(10rpx);
					}
					.like{
						position: absolute;
						display: flex;
						align-items: center;
						top: 20rpx;
						left: 20rpx;
						background-color: #00000069;
						border-radius: 35rpx;
						padding: 5rpx 10rpx;
						.like-icon{
							width: 30rpx;
							height: 30rpx;
						}
						.txt{
							margin-left: 10rpx;
							font-size: 26rpx;
							color: $lf-text-white;
						}
					}
				}
				.title{
					display: flex;
					align-items: center;
					margin-left: 20rpx;
					margin-bottom: 5rpx;
					margin-top: 5rpx;
					.name{
						font-size: 32rpx;
						font-weight: bold;
					}
					image{
						margin-left: 5rpx;
						width: 30rpx;
						height: 30rpx;
					}
				}
				.age{
					color: $lf-text-grey;
					font-size: 26rpx;
					margin-left: 20rpx;
				}
				.btn{
					display: flex;
					justify-content: center;
					align-items: center;
					padding: 20rpx 0;
					image{
						width: 30rpx;
						height: 30rpx;
						margin-right: 10rpx;
					}
					.sub{
						font-size: 30rpx;
						color: $lf-text-blue;
					}
				}
			}
		}
	}
</style>
