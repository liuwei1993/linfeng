<template>
	<view>
		<view class="guide">
			<view class="header">
				<image class="logo" src="/static/image/linfeng.png"></image>
				<view class="title">
					<view class="name">林风婚恋交友</view>
					<view class="desc">结识志同道合的朋友，拓展人生圈子</view>
				</view>
			</view>
			<view class="function-wrap">
				<view class="fuction-item">
					<image class="logo" src="/static/image/terminal.png"></image>
					<view class="function-title">
						<view class="function-name">多端入口，满足不同的场景</view>
						<view class="function-desc">打造专属的私域流量池，客户更加稳定！</view>
					</view>
				</view>
				<view class="fuction-item">
					<image class="logo" src="/static/image/open.png"></image>
					<view class="function-title">
						<view class="function-name">代码规范，降低成本</view>
						<view class="function-desc">开源无加密，让科技更加普惠</view>
					</view>
				</view>
				<view class="fuction-item">
					<image class="logo" src="/static/image/originality.png"></image>
					<view class="function-title">
						<view class="function-name">一款产品，解决多个问题</view>
						<view class="function-desc">方便二开，让你的创意无限放大</view>
					</view>
				</view>
				<view class="fuction-item">
					<image class="logo" src="/static/image/train.png"></image>
					<view class="function-title">
						<view class="function-name">一对一指导，精准高效</view>
						<view class="function-desc">提供针对性的解决方案，轻松应对挑战</view>
					</view>
				</view>
			</view>
			
			
			
			<view class="btn" @click="goNext()">开启邂逅</view>
		</view>
	</view>
</template>

<script  setup>
	
const goNext = () => {
	// #ifdef MP-WEIXIN
	uni.navigateTo({
		url: "/pages/preload/index"
	});
	// #endif
	
	// #ifdef H5 || APP-PLUS
	uni.navigateTo({
		url: "/pages/preload/sms-login"
	});
	// #endif
	
}
 
</script>

<style lang="scss">
	.guide{
		position: absolute;
		width: 100%;
		min-height: 100%;
		background-image: linear-gradient(to bottom, #ddeafd, #F5F7FA);
		.header{
			display: flex;
			justify-content: center;
			align-items: center;
			/* #ifdef MP-WEIXIN */
			margin-top: 250rpx;
			/* #endif */
			/* #ifdef H5 || APP-PLUS */
			margin-top: 150rpx;
			/* #endif */
			image{
				width: 120rpx;
				height: 120rpx;
				border-radius: 60rpx;
			}
			.title{
				display: flex;
				justify-content: center;
				flex-direction: column;
				margin-left: 20rpx;
				.name{
					font-size: 40rpx;
					font-weight: bold;
				}
				.desc{
					margin-top: 10rpx;
					font-size: 28rpx;
				}
			}
		}
		.function-wrap{
			margin: 30rpx;
			padding: 0 30rpx 50rpx 30rpx;
			background-color: #ffffff;
			display: flex;
			flex-direction: column;
			margin-top: 100rpx;
			border-radius: 20rpx;
			margin-bottom: 100rpx;
			.fuction-item{
				display: flex;
				align-items: center;
				margin-top: 50rpx;
				image{
					width: 80rpx;
					height: 80rpx;
				}
				.function-title{
					display: flex;
					flex-direction: column;
					margin-left: 20rpx;
					.function-name{
						font-weight: bold;
						font-size: 30rpx;
					}
					.function-desc{
						font-size: 28rpx;
						margin-top: 10rpx;
					}
				}
			}
		}
		.btn{
			background-color: $lf-bg-blue;
			font-size: 34rpx;
			text-align: center;
			padding: 20rpx 0;
			border-radius: 40rpx;
			color: $lf-text-white;
			font-weight: bold;
			width: 70%;
			margin: 0 auto;
		}
	}
</style>