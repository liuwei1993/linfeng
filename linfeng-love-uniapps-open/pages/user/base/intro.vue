<template>
	<view class="info">
		<view class="info-wrap">
			<view class="info-title">
				<image class="icon" mode="aspectFill" src="/static/image/star.png"></image>
				<text>自我介绍</text>
			</view>
			<view class="content">
				<textarea placeholder="请填写自我介绍" class="txt" maxlength="1000" :auto-height="true"
					v-model="userInfo.intro"></textarea>
					<text class="number">10/60</text>
			</view>
			<view class="info-title">
				<text>试试这样写</text>
			</view>
			<view class="prompt-wrap">
				<view class="info-base-item">
					<view class="btn">
						<image mode="aspectFill" src="/static/image/birthday.png"></image>
						<text class="txt">家庭</text>
					</view>
					<text class="sub">老家是湖北黄冈的小县城，现定居于武汉。自己是独生子女、小康家庭。平时生活圈较窄，希望在这遇到心仪的ta~</text>
				</view>
				<view class="info-base-item">
					<view class="btn">
						<image mode="aspectFill" src="/static/image/birthday.png"></image>
						<text class="txt">学业</text>
					</view>
					<text class="sub">大学就读于武汉大学，在校成绩优异。</text>
				</view>
				<view class="info-base-item">
					<view class="btn">
						<image mode="aspectFill" src="/static/image/birthday.png"></image>
						<text class="txt">工作</text>
					</view>
					<text class="sub">互联网行业新媒体运营。</text>
				</view>
				<view class="info-base-item">
					<view class="btn">
						<image mode="aspectFill" src="/static/image/birthday.png"></image>
						<text class="txt">性格</text>
					</view>
					<text class="sub">我对于学习、工作比较认真上进，性格上温和友善、自信大方。</text>
				</view>
			</view>
			<view class="submit" @click="submit">保存</view>
		</view>
	</view>
	
</template>

<script setup>
import { ref } from "vue";
import { onLoad } from "@dcloudio/uni-app";
import request from '@/utils/request.js';

const userInfo = ref({})

onLoad((options) => {
	getUserInfo()
})

const getUserInfo = () => {
	const getUserInfo = () => {
		request.get("user/userInfo").then(res => {
			userInfo.value = res.result;
		})
	}
}

const submit = () => {
	
	if(userInfo.value.intro == null || userInfo.value.intro == ''){
		uni.showToast({
			title: '请输入内容哦',
			icon: "none"
		});
		return
	}
	
	request.post("user/userInfoEdit", userInfo.value).then(res => {
		if (res.code == 0) {
			uni.navigateBack()
		}
	})
}
	
</script>
<style lang="scss">
.info-wrap{
	border-radius: 50rpx 50rpx 0 0;
	background-color: #FFF;
	.info-title{
		font-weight: bold;
		font-size: 30rpx;
		padding-left: 30rpx;
		display: flex;
		padding-top:30rpx;
		.icon{
			width: 40rpx;
			height: 40rpx;
			margin-right: 20rpx;;
		}
		.txt{
			
		}
	}
	.content{
		background-color: #F6F7FB;
		margin: 40rpx 40rpx;
		height: 400rpx;
		border-radius: 20rpx;
		position: relative;
		font-size: 28rpx;
		textarea::placeholder{
			font-size: 28rpx;
			color:#ccc;
			
		}
		.txt{
			padding-left: 30rpx;
			padding-top: 30rpx;
		}
		.number{
			font-size: 30rpx;
			color:#ccc;
			bottom: 30rpx;
			right: 30rpx;
			position: absolute;
		}
	}
	.prompt-wrap {
		margin-top: 10rpx;
		display: flex;
		flex-direction: column;
		padding: 30rpx;
		width: 100%;	
		.info-base-item{
			margin-top: 40rpx;
			display: flex;
			flex-direction: row;
			image {
				width: 36rpx;
				height: 36rpx;
				margin-right: 10rpx;
			}
			.btn{
				width: 170rpx;
				border: 1px solid #ccc;
				height: 50rpx;
				line-height: 50rpx;
				border-radius: 25rpx;
				padding: 10rpx 20rpx;
				font-size: 28rpx;
				display: flex;
				text-align: center;
				justify-content: center;
				align-items: center;
				color: #666;
			}
			.sub {
				color: #666;
				font-size: 28rpx;
				margin-left: 20rpx;
				width: 600rpx;
				line-height: 45rpx;
				padding-top: 5rpx;
			}
		}
	}
	.submit{
		position: fixed;
		bottom: 50rpx;
		left: 0;
		right: 0;
		width: 50%;
		background-color: $lf-bg-blue;
		font-size: 34rpx;
		text-align: center;
		padding: 20rpx 0;
		border-radius: 40rpx;
		color: #fff;
		font-weight: bold;
		align-items: center;
		margin: 0 auto;
		z-index: 99;
	}
}		
</style>
