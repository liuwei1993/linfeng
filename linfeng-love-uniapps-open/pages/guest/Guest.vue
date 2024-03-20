<template>
	<view class="recommend-content">
		<view class="header">
			<image class="back" src="/static/image/back.png"></image>
			<view class="info">
				<image class="avatar" :src="guestUser.avatar"></image>
				<view class="nickname">{{guestUser.userName}}</view>
			</view>
			<view class="base u-skeleton-fillet">
				<view class="grid-wrap">
					<view class="info-base-item">
						<image mode="aspectFill" src="/static/image/birthday.png"></image>
						<text class="sub">{{guestUser.birthdayYear}}年 · {{guestUser.constellation}}</text>
					</view>
					<view class="info-base-item">
						<image v-if="guestUser.gender == 1" mode="aspectFill" src="/static/image/man.png"></image>
						<image v-else mode="aspectFill" src="/static/image/woman.png"></image>
						<text class="sub">{{guestUser.stature}}cm · {{guestUser.weight}}kg</text>
					</view>
					<view class="info-base-item">
						<image mode="aspectFill" src="/static/image/city.png"></image>
						<text class="sub">现居{{guestUser.homeCity}} · {{guestUser.livingProvince}}{{guestUser.livingCity}}人</text>
					</view>
					<view class="info-base-item">
						<image mode="aspectFill" src="/static/image/edu.png"></image>
						<text class="sub">{{guestUser.educationText}} · {{guestUser.school}}</text>
					</view>
					<view class="info-base-item">
						<image mode="aspectFill" src="/static/image/job.png"></image>
						<text class="sub">{{guestUser.jobText}}</text>
					</view>
				</view>
				
			</view>
		</view>
		
		<view class="f-wrap u-skeleton-fillet" v-if="guestUser.userMediaList != undefined && guestUser.userMediaList.length != 0">
			<view class="info-title">
				<image class="icon" mode="aspectFill" src="/static/image/star.png"></image>
				<text>个人形象</text>
			</view>
			<view class="about-text">
				<u-swiper name="url" height="800" :autoplay="false" :list="guestUser.userMediaList"></u-swiper>
			</view>
		</view>
		
		<!-- 我的介绍 -->
		<view>
			<view v-if="guestUser.intro != null" class="f-wrap u-skeleton-fillet">
				<view class="info-title">
					<image class="icon" mode="aspectFill" src="/static/image/star.png"></image>
					<text>自我介绍</text>
				</view>
				<view class="about-text">
					<rich-text :nodes="guestUser.intro"></rich-text>
				</view>
			</view>
		</view>
		
		<!-- 我的动态 -->
		<view class="f-wrap u-skeleton-fillet" @click="goToGuestMoment(guestUser.uid)" v-if="guestUser.momentListLength != 0">
			<view class="info-title">
				<image class="icon" mode="aspectFill" src="/static/image/star.png"></image>
				<text>我的动态</text>
				<text class="dynamic-count">({{guestUser.momentListLength}})</text>
				<u-icon name="arrow-right" :size="28" class="arrow-right-wrap"></u-icon>
			</view>
			<view class="post-img-wrap">
				<image v-for="(item,index) in guestUser.momentMediaList" :key="index" mode="aspectFill" :src="item.url"></image>
			</view>
		</view>
		
		<view class="footer"></view>
		
		<view class="handle-wrap" v-if="showNext">
			<view class="like-dislike">	
				<view class="know" @click="next(guestUser.uid)">
					<text>换一个</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
import { ref, defineProps, toRefs } from "vue";
import request from '@/utils/request.js'
const props = defineProps({
  guestUser: {
    type: Object,
    default: ()=>{}
  },
  topHeight: {
	  type: String,
	  default: '0px'
  },
  showNext: {
  	  type: Boolean,
  	  default: true
  }
})

const {topHeight, guestUser} = toRefs(props)
const emits = defineEmits(['on-operator'])

const goToGuestMoment = (uid) => {
	uni.navigateTo({
		url:'/pages/moment/guest?uid=' + uid
	})
}

const next = (uid) => {
	request.post("guest/recommend/operator", {
		uid: uid
	}).then(res => {
		if (res.code == 0) {
			emits('on-operator', status)
		}
	})
}

</script>

<style lang="scss" scoped>
.recommend-content{
	margin-top: v-bind(topHeight);
	.header {
		display: flex;
		flex-direction: column;
		position: relative;
		.back{
			position: absolute;
			width: 100%;
			height: 100%;
			z-index: -1;
		}
		.info{
			padding: 10rpx 30rpx;
			padding-top: 50rpx;
			display: flex;
			align-items: center;
			.level{
				font-weight: normal;
				background-color: $lf-bg-blue;
				font-size: 24rpx;
				border-radius: 30rpx;
				color: $lf-text-white;
				float: left;
				padding: 5rpx 10rpx;
				margin-left: 10rpx;
			}
		}
		.avatar {
			margin-right: 40rpx;
			height: 110rpx;
			width: 110rpx;
			border-radius: 50%;
			overflow: hidden;
		}
		.nickname{
			font-size: 42rpx;
			font-weight: bold;
		}
		.base{
			margin: 30rpx;
			background-color: $lf-bg-grey;
			border-radius: 20rpx;
			opacity: 0.9;
		}
	}
	.grid-wrap {
		margin-top: 10rpx;
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		grid-gap: 30rpx 15rpx;
		padding: 30rpx;
		.info-base-item{
			display: flex;
			flex-direction: row;
			image {
				width: 28rpx;
				height: 28rpx;
			}
			.sub {
				font-size: 27rpx;
				line-height: 28rpx;
				margin-left: 20rpx;
			}
		}
	}
	.info-title{
		font-weight: bold;
		font-size: 30rpx;
		padding-left: 30rpx;
		display: flex;
		.icon{
			width: 40rpx;
			height: 40rpx;
			margin-right: 20rpx;;
		}
	}
	.about-text{
		font-size: 29rpx;
		padding: 30rpx;
	}
	.tag-box {
		padding: 30rpx;
		.tag {
			padding: 10rpx 20rpx;
			border-radius: 28rpx;
			font-size: 28rpx;
			display: inline-block;
			margin-right: 20rpx;
			margin-bottom: 20rpx;
			background-color: $lf-bg-blue-week;
			color: $lf-text-blue;
		}
	}
	.post-img-wrap {
		padding: 20rpx;
		display: grid;
		grid-template-columns: repeat(4, 1fr);
		grid-gap: 10rpx;
		margin-top: 20rpx;
	
		image {
			width: 100%;
			height: 200rpx;
			border-radius: 10rpx;
		}
	}
	.dynamic-count{
		font-size: 28rpx;
		font-weight: normal;
		margin-left: 10rpx;
	}
	
	.arrow-right-wrap{
		margin-left: auto;
		margin-right: 30rpx;
	}
	.auth-wrap{
		display: flex;
		padding: 20rpx 0;
		.auth-item{
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			flex: 1;
			padding: 20rpx 0;
			border-radius: 20rpx;
			.auth-image{
				position: relative;
				background-color: $lf-bg-grey-week;
				width: 130rpx;
				height: 130rpx;
				border-radius: 130rpx;
				justify-content: center;
				align-items: center;
				display: flex;
				margin-bottom: 20rpx;
			}
			.auth-icon{
				width: 80rpx;
				height: 80rpx;
			}
			.auth-icon2{
				width: 60rpx;
				height: 60rpx;
			}
			.auth-icon3{
				width: 70rpx;
				height: 70rpx;
			}
			.auth-check{
				width: 40rpx;
				height: 40rpx;
				position: absolute;
				bottom: 0rpx;
				right: 0rpx;
			}
			.auth-txt{
				font-size: 28rpx;
			}
		}
	}
	.user-media{
		padding: 0 30rpx;
	}
	.footer{
		height: 200rpx;
		width: 100%;
	}
}
.handle-wrap{
	padding: 50rpx 0;
	position: fixed;
	/* #ifdef MP-WEIXIN */
	bottom: 30rpx;
	/* #endif */
	/* #ifdef H5 || APP-PLUS */
	bottom: 130rpx;
	/* #endif */
	width: 100%;
	.like-dislike{
		display: flex;
		justify-content: center;
		align-items: center;
		flex: 1;
		padding: 20rpx 0;
		border-radius: 20rpx;
		.know{
			display: flex;
			background-color: $lf-bg-blue;
			color: $lf-text-white;
			padding: 20rpx 20rpx;
			border-radius: 100rpx;
			margin: 10rpx 30rpx;
			align-items: center;
			font-size: 30rpx;
			min-width: 240rpx;
			justify-content: center;
		}
		.txt{
			font-size: 28rpx;
		}
	}
}
</style>
