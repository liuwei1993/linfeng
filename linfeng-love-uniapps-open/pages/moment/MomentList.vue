<template>
	<view>
		<view v-if="momentList.length != 0">
			<view class="post-wrap"  v-for="(item,index) in momentList" :key="index">
				<view class="post-title">
					<view>
						<u-avatar  @click="goToGuestDetail(item)" class="avatar" :src="item.avatar"></u-avatar>
					</view>
					<view class="info">
						<view class="title">
							<text class="nickname">{{item.userName}}</text>
							<image v-if="item.gender == 1" mode="aspectFill" class="sex-icon" src="/static/image/man-color.png"></image>
							<image v-else mode="aspectFill" class="sex-icon" src="/static/image/woman-color.png"></image>
						</view>
						<text class="other">{{item.birthdayYear}}年 · {{item.livingCity}} · {{item.jobText}}</text>
					</view>
					<view class="delete" v-if="item.uid == userInfo.uid"  @click="deleteMoment(item.momentId)">删除</view>
				</view>
				<view class="post-content">
					<view @click="goToDetail(item.momentId)" class="post-text">{{item.content}}</view>
					<!-- 图片 -->
					<view v-if="item.mediaType == 1" :class="checkMediaLength(item)">
						<image @click="previewImage(item.mediaList, media.url)" v-for="(media,mediaIndex) in item.mediaList"  :key="mediaIndex" mode="aspectFill" :src="media.url"></image>
					</view>
				</view>
				<view class="publish-info">
					<view class="time">{{timeFormat(item.createTime)}}</view>
				</view>
				<view class="post-footer">
					<!-- #ifdef H5 || APP-PLUS -->
					<view class="footer-item"  @click="showShareDialog(item)">
						<u-icon name="share" size="40"></u-icon>
					</view>
					<!--#endif-->
					<!-- #ifdef MP-WEIXIN -->
					<view class="footer-item">
						<button open-type="share" :id="item.momentId" :data-item="item"> <u-icon name="share" size="40"></u-icon></button>
					</view>
					<!--#endif-->
					<view @click="goToDetail(item.momentId)" class="footer-item">
						<u-icon name="chat" size="40"></u-icon>
						<text class="count">{{item.cv}}</text>
					</view>
					<view class="footer-item">
						<u-icon v-if="item.likeFlag" @click="updateLv(item, -1)" name="thumb-up-fill" size="40"></u-icon>
						<u-icon v-else @click="updateLv(item, 1)" name="thumb-up" size="40"></u-icon>
						
						<text class="count">{{item.lv}}</text>
					</view>
				</view>
				
				<u-line v-if="index < momentList.length - 1" margin="40rpx 0 0 0"></u-line>
			</view>
			<u-divider v-if="!hasData" color="#C8C8C8" border-color="#C8C8C8" margin-top="20">没有更多了</u-divider>
			<view class="moment-load">
				<u-loading :show="showMomentLoading" mode="flower"></u-loading>
			</view>
			<!-- #ifdef H5 || APP-PLUS -->
			<view class="cu-modal" :class="showShare? 'show' : ''"  @tap="hideShareDialog">
				<view class="cu-dialog"  v-if="showShare" @tap="hideShareDialog">
					 <cc-poster @success="posterSuccess" :imgSrc="shareInfo.image"
						:Title="shareInfo.title" :Referrer="shareInfo.userName" ViewDetails="发布了一条动态，一起去围观"
						:LineType="false"></cc-poster>
				</view>
			</view>
			<!--#endif-->
			
		</view>
		<view v-else>
			<view class="prompt-wrap">
				<image class="icon" src="/static/image/no-data.png"></image>
				<text class="txt">没有嘉宾发表动态</text>
			</view>
		</view>
	</view>
	
</template>

<script setup>
import { inject, ref, defineProps, toRefs, defineExpose } from "vue";
import request from '@/utils/request.js';
import timeUtil from '@/utils/timeUtil.js';

const $store = inject('$store')
const userInfo = $store.state.loginUser

const showShare = ref(false)
const shareInfo = ref({
	image:'',
	title:'',
	userName:''
})

const props = defineProps({
  queryForm: {
    type: Object,
    default: ()=>{}
  },
  marginTop: {
    type: String,
    default: "300rpx"
  }
})

const {queryForm} = toRefs(props)
const momentList = ref([])
const playVideoIndex = ref(-1)
const hasData = ref(false)
const showMomentLoading = ref(false)

const addMomentList = () => {
	if(!hasData.value){
		return
	}
	showMomentLoading.value = true
	request.get("moment/list",queryForm.value).then(res => {
		showMomentLoading.value = false
		let list = res.result.records
		momentList.value = momentList.value.concat(list)
		if(res.result.current == res.result.pages){
			hasData.value = false
		}
	})
}

const refreshMomentList = () => {
	request.get("moment/list",queryForm.value).then(res => {
		let list = res.result.records
		if(res.result.current != res.result.pages){
			hasData.value = true
		}
		console.log(hasData.value)
		momentList.value = res.result.records
	})
}


const checkMediaLength = (item) => {
	if(item.mediaList.length == 1){
		return 'image-style-1'
	}else if(item.mediaList.length == 2){
		return 'image-style-2'
	}else if(item.mediaList.length == 4){
		return 'image-style-4'
	}else{
		return 'image-style-9'
	}
}

const previewImage = (images, url) => {
	let urls = []
	images.forEach(image => {
		urls.push(image.url)
	})
	uni.previewImage({
		urls: urls,
		current: url,
		success: () => {
		},
		fail: (error) => {
			uni.showToast({
				title: "预览图片失败",
				icon: "none"
			});
		}
	});
}

const updateLv = (moment, operatorType) => {
	if(operatorType == 1){
		moment.likeFlag = true
		moment.lv = moment.lv + 1
	}else{
		moment.likeFlag = false
		moment.lv = moment.lv - 1
	}
	
	request.post("moment/updateLv", {
		momentId: moment.momentId,
		operatorType: operatorType
	})
}

const deleteMoment = (momentId) => {
	request.post("moment/delete", {
		momentId: momentId
	}).then(res => {
		if(res.code == 0){
			uni.showToast({
				icon:'none',
				title:'删除成功'
			})
			refreshMomentList()
		}
	})
}

const showShareDialog = (item) => {
	if(item.mediaType == '1' && item.mediaList.length == 0){
		shareInfo.value.image = item.avatar
	}else if(item.mediaType == '1' && item.mediaList.length != 0){
		shareInfo.value.image = item.mediaList[0].url
	}else if(item.mediaType == '2'){
		shareInfo.value.image = item.avatar
	}else{
		uni.showToast({
			icon:'none',
			title:'生成海报失败'
		})
		return
	}
	if(item.content.length > 12){
		shareInfo.value.title = item.content.slice(0, 12) + "..."
	}else{
		shareInfo.value.title = item.content
	}
	shareInfo.value.userName = item.userName
	showShare.value = true
	
}

const hideShareDialog = () => {
	showShare.value = false
}

const posterSuccess = () => {
	
}
const timeFormat = (time) => {
	return timeUtil.timeShowFormat(time);
}

const goToDetail = (momentId) => {
	uni.navigateTo({
		url:'/pages/moment/detail?momentId=' + momentId
	})
}

const goToGuestDetail = (moment) => {
	if(moment.anonymous == 1){
		return
	}
	uni.navigateTo({
		url:'/pages/guest/detail?showLike=false&oid=' + moment.oid
	})
}

defineExpose({ refreshMomentList, addMomentList })

</script>

<style>
</style>
<style lang="scss" scoped>
	.post-wrap{
		padding: 20rpx 30rpx;
		.post-title{
			padding: 10rpx 30rpx;
			display: flex;
			align-items: center;
			.info{
				display: flex;
				flex-direction: column;
				flex: 1;
				padding: 20rpx 30rpx;
				.title{
					display: flex;
					align-items: center;
					.nickname{
						font-size: 28rpx;
					}
					.sex-icon{
						margin-left: 5rpx;
						width: 24rpx;
						height: 24rpx;
					}
				}
				.other{
					font-size: 25rpx;
					color: $lf-text-grey;
				}
				
			}
			.delete{
				margin-right: 20rpx;
				margin-left: auto;
				color: $lf-text-blue;
				z-index: 99;
			}
		}
		.image-style-1 {
			display: block;
			width: 100%;
			max-height: 600rpx;
			border-radius: 5px;
			overflow: hidden;
			margin-top: 20rpx;
		}
		
		.image-style-2 {
			display: flex;
			image {
				margin: 5rpx;
				border-radius: 5px;
				width: 100%;
				height: 294rpx;
			}
			margin-top: 20rpx;
		}
		
		.image-style-4 {
			display: flex;
			flex-wrap: wrap;
			margin-top: 20rpx;	
			image {
				width: 48%;
				height: 320rpx;
				margin: 0.5%;
				
			}
		}
		
		.image-style-9 {
			display: flex;
			flex-wrap: wrap;
			margin-top: 20rpx;
			image {
				width: 31.3%;
				height: 200rpx;
				margin: 1% 1% 1% 0;
				border-radius: 5px;
			}
			
		}
		.video-wrap{
			margin: 20rpx 0;
			position: relative;
			width: 350rpx;
			.upload-video{
				width: 350rpx;
			}
			image{
				position: absolute;
				left: 0;
				right: 0;
				top: 0;
				height: 70rpx;
				width: 70rpx;
				bottom: 0;
				margin: auto;
			}
			.icon{
				position: absolute;
				right: 20rpx;
				top: 20rpx;
				background-color: $lf-bg-grey;
				border-radius: 50rpx;
				display: flex;
				justify-content: center;
				align-items: center;
				padding: 10rpx;	
			}
		}
		.post-content{
			padding-left: 30rpx;	
		}
		.publish-info{
			padding-left: 30rpx;
			padding-top: 20rpx;
			.time{
				font-size: 26rpx;
				color: $lf-text-grey;
			}
			.my-talk-box {
				margin-top: 20rpx;
				.my-talk {
					padding: 10rpx 20rpx;
					border-radius: 28rpx;
					font-size: 28rpx;
					display: inline-block;
					margin-right: 20rpx;
					margin-bottom: 20rpx;
					background-color: $lf-bg-grey-week;
				}
			}
		}
		.post-footer{
			display: flex;
			margin-top: 30rpx;
			justify-content: center;
			.footer-item{
				width: 30%;
				display: flex;
				justify-content: center;
				.count{
					font-size: 32rpx;
					margin-left: 20rpx;
				}
				button::after {
				    border: none;
					
				}
				button {
					background-color: #fff;
					line-height: 0;
					padding-left: 0;
					padding-right: 0;
				}
			}
		}
		.talk-wrap{
			background-color: $lf-bg-blue-week;
			margin: 20rpx 0;
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
						white-space: nowrap;
						overflow: hidden;
						text-overflow: ellipsis;
						.title{
							white-space: nowrap;
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
		
	}
	.moment-load{
		margin-top: 20rpx;
		text-align: center;
	}
	.prompt-wrap{
		display: flex;
		justify-content: center;
		align-items: center;
		flex-direction: column;
		margin-top: v-bind(marginTop);
		.icon{
			width: 200rpx;
			height: 200rpx;
		}
		.txt{
			font-size: 28rpx;
			margin-top: 40rpx;
			margin-bottom: 40rpx;
			color: $lf-text-grey;
		}
		.btn{
			width: 380rpx;
			font-size: 34rpx;	
			text-align: center;
			padding: 10rpx 0;
			border-radius: 40rpx;
			border: 1rpx solid #000000;
			font-weight: 600;
			margin: 0 auto;
		}
	}
	.cu-modal {
		position: fixed;
		top: 0;
		right: 0;
		bottom: 0;
		left: 0;
		z-index: 1110;
		opacity: 0;
		outline: 0;
		text-align: center;
		-ms-transform: scale(1.185);
		transform: scale(1.185);
		backface-visibility: hidden;
		perspective: 2000upx;
		background: rgba(0, 0, 0, 0.6);
		transition: all 0.3s ease-in-out 0s;
		pointer-events: none;
	}
	
	.cu-modal::before {
		content: "\200B";
		display: inline-block;
		height: 100%;
		vertical-align: middle;
	}
	
	.cu-modal.show {
		opacity: 1;
		transition-duration: 0.3s;
		-ms-transform: scale(1);
		transform: scale(1);
		overflow-x: hidden;
		overflow-y: auto;
		pointer-events: auto;
	}
	
	.cu-dialog {
		position: relative;
		display: inline-block;
		vertical-align: middle;
		margin-left: auto;
		margin-right: auto;
		width: 680upx;
		max-width: 100%;
		background-color: #f8f8f8;
		border-radius: 10upx;
		overflow: hidden;
	}
</style>
