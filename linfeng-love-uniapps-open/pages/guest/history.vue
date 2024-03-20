<template>
	<view class="recommend-history">
		<view class="out-wrap">
			<view class="recommend-title">
				<view class="column"></view>
				<text class="txt-left"></text>
				<text class="txt-right">推荐 {{historyRecommend.length}}</text>
			</view>
		</view>
		<GuestList :guestUsertList="historyRecommend"></GuestList>
	</view>
	
</template>

<script setup>
import { inject, ref } from "vue";
import { onShow } from "@dcloudio/uni-app";
import request from '@/utils/request.js'
import GuestList from '@/pages/guest/GuestList.vue'

const $store = inject('$store')
const businessConfig = $store.state.businessConfig

const historyRecommend = ref({})

onShow(() => {
	gethistoryRecommend()
})

const gethistoryRecommend = () => {
	request.get('guest/recommend/history').then(res => {
		historyRecommend.value = res.result
	})
}

const goDetail = (oid) => {
	uni.navigateTo({
		url:'/pages/guest/detail?oid=' + oid
	})
}

</script>

<style>
</style>
<style lang="scss" scoped>
	.recommend-history{
		position: absolute;
		background-color: $lf-bg-grey-week;
		width: 100%;
		min-height: 100%;
		.out-wrap{
			padding: 0 20rpx;
			
			.recommend-title{
				display: flex;
				padding: 20rpx 10rpx;
				align-items: center;
				.column{
					height: 30rpx;
					background-color: $lf-bg-blue;
					width: 15rpx;
					border-radius: 15rpx;
				}
				.txt-left{
					margin-left: 15rpx;
					font-size: 28rpx;
				}
				.txt-right{
					margin-right: 5rpx;
					margin-left: auto;
					color: $lf-text-grey;
					font-size: 26rpx;
				}
			}
			.recommend-wrap{
				display: flex;
				flex-wrap: wrap;
				.recommend-item{
					margin: 2% 1.5%;
					width: 47%;
					background-color: $lf-bg-white;
					border-radius: 20rpx;
					.image-main{
						position: relative;
						image{
							width: 100%;
							height: 300rpx;
							border-radius: 20rpx;
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
		.footer{
			font-size: 26rpx;
			text-align: center;
			margin-top: 40rpx;
			height: 150rpx;
			color: $lf-text-grey;
		}
	}
</style>
