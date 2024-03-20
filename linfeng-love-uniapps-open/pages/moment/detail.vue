<template>
	<view>
		<view class="community" v-if="showComment">
			<view class="content">
				<view class="post-wrap" >
					<view class="post-title">
						<view>
							<image @click="goToGuestDetail(moment.uid)" class="avatar" :src="moment.avatar"></image>
						</view>
						<view class="info">
							<view class="title">
								<text class="nickname">{{moment.userName}}</text>
								<image v-if="moment.gender == 1" mode="aspectFill" class="sex-icon" src="/static/image/man-color.png"></image>
								<image v-else mode="aspectFill" class="sex-icon" src="/static/image/woman-color.png"></image>
							</view>
							<text class="other">{{moment.birthdayYear}}年 · {{moment.livingCity}} · {{moment.jobText}}</text>
						</view>
					</view>
					<view class="post-content">
						<view class="post-text">{{moment.content}}</view>
						<!-- 图片 -->
						<view v-if="moment.mediaType == 1" :class="checkMediaLength()">
							<image v-for="(media,mediaIndex) in moment.mediaList"  :key="mediaIndex" mode="aspectFill" :src="media.url"></image>
						</view>
					</view>
					<view class="publish-info">
						<view class="time">{{timeFormat(moment.createTime)}}</view>
					</view>
					
					<view class="post-footer">
						<!-- #ifdef H5 || APP-PLUS -->
						<view class="footer-item"  @click="showShareDialog()">
							<u-icon name="share" size="40"></u-icon>
						</view>
						<!--#endif-->
						<!-- #ifdef MP-WEIXIN -->
						<view class="footer-item">
							<button open-type="share" :id="moment.momentId" :data-item="item"> <u-icon name="share" size="40"></u-icon></button>
						</view>
						<!--#endif-->
						<view class="footer-item">
							<u-icon name="chat" size="40"></u-icon>
							<text class="count">{{moment.cv}}</text>
						</view>
						<view class="footer-item">
							<u-icon v-if="moment.likeFlag" @click="updateLv(moment, -1)" name="thumb-up-fill" size="40"></u-icon>
							<u-icon v-else @click="updateLv(moment, 1)" name="thumb-up" size="40"></u-icon>
							
							<text class="count">{{moment.lv}}</text>
						</view>
					</view>
					<u-line margin="20rpx 0 30rpx 0"></u-line>
					
					<view class="like-list">
						<image v-for="(item,itemIndex) in likeList"  :key="itemIndex" :src="item.avatar"></image>
						<view class="txt">{{moment.lv>10?`等${moment.lv}`:moment.lv}}人觉得很赞</view>
					</view>
					
					<u-line margin="30rpx 0 0 0"></u-line>
					
					<view class="comment-wrap">
						<view class="comment-header">
							<view>评论 {{moment.cv}}</view>
						</view>
						<view v-for="(comment,commentIndex) in commentList"  :key="commentIndex">
							<view class="comment">
								<view>
									<image @click="goToGuestDetail(comment.replyUid)" class="avatar" :src="comment.avatar"></image>
								</view>
								<view class="info">
									<view class="title">
										<text class="nickname">{{comment.replyUserName}}{{comment.replyUid == moment.uid?'(作者)':''}}</text>
										<image v-if="comment.gender == 1" mode="aspectFill" class="sex-icon" src="/static/image/man-color.png"></image>
										<image v-else mode="aspectFill" class="sex-icon" src="/static/image/woman-color.png"></image>
									</view>
									
									<text class="other">{{comment.birthdayYear}}年 · {{comment.livingCity}} · {{comment.jobText}}</text>
								</view>
							</view>
							<view class="comment-content">
								<view class="comment-text">{{comment.content}}</view>
							</view>
							<view class="time">
								<view>{{timeFormat(comment.createTime)}}</view>
								<view v-if="comment.replyUid == userInfo.uid" @click="deleteComment(comment)" class="delete">删除</view>
							</view>
							<u-line margin="40rpx 0 0 110rpx" length="85%"></u-line>
						</view>
					</view>
				</view>
				
			</view>
			<view class="input-tool">
				<view class="input-line">
					<u-input  class="input-area" :placeholder="placeholder" v-model="commentInfo.content" type="text-area" :height="60" :auto-height="true" />
					<view @click="commentSubmit" class="send">发送</view>
				</view>
			</view>
			<!-- #ifdef H5 || APP-PLUS -->
			<view class="cu-modal" :class="showShare? 'show' : ''"  @tap="hideShareDialog">
				<view class="cu-dialog"  v-if="showShare" @tap="hideShareDialog">
					 <cc-poster @success="posterSuccess" :imgSrc="shareInfo.image" :QrSrc="shareInfo.qrcode"
						:Title="shareInfo.title" :Referrer="shareInfo.userName" ViewDetails="发布了一条动态，一起去围观"
						:LineType="false"></cc-poster>
				</view>
			</view>
			<!--#endif-->
		</view>
		<view v-if="loadSuccess && !showComment" class="prompt-wrap">
			<image class="icon" src="/static/image/no-data.png"></image>
			<text class="txt">动态不存在</text>
		</view>
	</view>
	
</template>

<script setup>
import { inject, ref, nextTick } from "vue";
import { onLoad, onShow, onReachBottom, onShareAppMessage } from "@dcloudio/uni-app";
import timeUtil from '@/utils/timeUtil.js';
import request from '@/utils/request.js';

const $store = inject('$store')

const userInfo = ref({})
const moment = ref({})
const showPlay = ref(true)
const videoContext = uni.createVideoContext('video',this);
const commentInfo = ref({
	momentId:'',
	content:'',
	pid: 0,
	beReplyUid:'',
})
const pageNum = ref(1)
const pageSize = ref(10)
const commentList = ref([])
const placeholder = ref('说点什么吧')
const likeList = ref([])
const hasData = ref(true)
const showComment = ref(false)
const loadSuccess = ref(false)

const showShare = ref(false)
const shareInfo = ref({
	image:'',
	qrcode:'',
	title:'',
	userName:''
})

onLoad((options) => {
	getMomentDeail(options.momentId)
	getCommentList(options.momentId, pageNum.value, pageSize.value)
	commentInfo.value.momentId = options.momentId
	userInfo.value = $store.state.loginUser
})

onShareAppMessage((res) => {
	
	if (res.from === 'button') {
		let momentDetail = moment.value
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
			path: '/pages/moment/detail?momentId=' + moment.value.momentId,
			imageUrl: imageUrl,
		}
	}
})

onReachBottom(() => {
	pageNum.value = pageNum.value + 1
	getCommentList(commentInfo.value.momentId, pageNum.value, pageSize.value)
});

const getMomentDeail = (momentId) => {
	request.get("moment/detail",{
		momentId: momentId
	}).then(res => {
		loadSuccess.value = true
		if(res.result == null){
			return
		}
		showComment.value = true
		moment.value = res.result;
		commentInfo.value.bereplyUid = moment.value.uid
	})
}

const getCommentList = (momentId, pageNum, pageSize) => {
	if(!hasData.value){
		return
	}
	request.get("moment/comment/list",{
		pageNum: pageNum,
		pageSize: pageSize,
		momentId: momentId
	}).then(res => {
		let resultList = res.result.records
		resultList.forEach(comment => {
			comment.openChildCommentLength = 1
		})
		commentList.value = commentList.value.concat(resultList)
		if(res.result.current == res.result.pages){
			hasData.value = false
		}
	})
}

const deleteComment = (comment) => {
	request.post("moment/comment/delete", {
		momentId: moment.value.momentId,
		commentId: comment.commentId
	}).then(res => {
		if(res.code == 0){
			uni.showToast({
				icon:'none',
				title:'删除成功'
			})
			moment.value.cv = moment.value.cv -1
			for(let i=0; i<commentList.value.length; i++){
				if(commentList.value[i].commentId == comment.commentId){
					commentList.value.splice(i, 1)
					break
				}
			}
		}
		
	})
}



const checkMediaLength = () => {
	if(moment.value.mediaList.length == 1){
		return 'image-style-1'
	}else if(moment.value.mediaList.length == 2){
		return 'image-style-2'
	}else if(moment.value.mediaList.length == 4){
		return 'image-style-4'
	}else{
		return 'image-style-9'
	}
}

const commentSubmit = () => {
	
	if(commentInfo.value.content == '' || commentInfo.value.content == null){
		uni.showToast({
			title:'内容不能为空'
		})
		return
	}
	request.post("moment/comment/add", commentInfo.value).then(res => {
		if(res.code == 0){
			uni.showToast({
				title:'发表成功'
			})
			moment.value.cv = moment.value.cv + 1
			commentInfo.value.content = ''
			commentList.value.unshift(res.result)
			
		}
		
	})
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

const showShareDialog = () => {
	if(moment.value.mediaType == '1' && moment.value.mediaList.length == 0){
		shareInfo.value.image = moment.value.avatar
	}else if(moment.value.mediaType == '1' && moment.value.mediaList.length != 0){
		shareInfo.value.image = moment.value.mediaList[0].url
	}else if(moment.value.mediaType == '2'){
		shareInfo.value.image = moment.value.avatar
	}else{
		uni.showToast({
			icon:'none',
			title:'生成海报失败'
		})
		return
	}
	if(moment.value.content.length > 12){
		shareInfo.value.title = moment.value.content.slice(0, 12) + "..."
	}else{
		shareInfo.value.title = moment.value.content
	}
	shareInfo.value.userName = moment.value.userName
	showShare.value = true
	
}

const hideShareDialog = () => {
	showShare.value = false
}

const posterSuccess = () => {
	
}

const goToGuestDetail = (uid) => {
	if(moment.value.anonymous == 1){
		return
	}
	uni.navigateTo({
		url:'/pages/guest/detail?showLike=false&uid=' + uid
	})
}

const timeFormat = (time) => {
	if(time == undefined){
		return ''
	}
	return timeUtil.timeShowFormat(time);
}
</script>

<style lang="scss" scoped>
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
		.post-wrap{
			padding: 20rpx 30rpx;
			.post-title{
				padding: 10rpx 30rpx;
				display: flex;
				align-items: center;
				image{
					width: 80rpx;
					height: 80rpx;
					border-radius: 80rpx;
				}
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
			}
			.image-style-1 {
				display: block;
				width: 100%;
				max-height: 600rpx;
				border-radius: 5px;
				overflow: hidden;
				
			}
			
			.image-style-2 {
				display: flex;
				image {
					margin: 5rpx;
					border-radius: 5px;
					width: 100%;
					height: 294rpx;
				}
			}
			
			.image-style-4 {
				display: flex;
				flex-wrap: wrap;
							
				image {
					width: 48%;
					height: 320rpx;
					margin: 0.5%;
					
				}
			}
			
			.image-style-9 {
				display: flex;
				flex-wrap: wrap;
				image {
					width: 31.3%;
					height: 200rpx;
					margin: 1%;
					border-radius: 5px;
				}
				
			}
			.video-wrap{
				margin: 20rpx 30rpx;
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
			.like-list{
				display: flex;
				margin-top: 30rpx;
				justify-content: flex-start;
				align-items: center;
				padding-left: 40rpx;
				image{
					width: 50rpx;
					height: 50rpx;
					border-radius: 50rpx;
					margin-left: -20rpx;
				}
				.txt{
					font-size: 24rpx;
					margin-left: 30rpx;
				}
			}
		}
		.comment-wrap{
			padding: 30rpx;
			padding-bottom: 60rpx;
			.comment-header{
				height: 60rpx;
				line-height: 60rpx;
				font-size: 30rpx;
				background-color: $lf-bg-white;
				margin-bottom: 20rpx;
			}
			.comment{
				padding: 10rpx 0;
				display: flex;
				align-items: center;
				image{
					width: 80rpx;
					height: 80rpx;
					border-radius: 80rpx;
				}
				.info{
					display: flex;
					flex-direction: column;
					flex: 1;
					padding: 10rpx 30rpx;
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
			}
			.comment-content{
				padding-left: 110rpx;
			}
			.time{
				color: $lf-text-grey;
				margin-top: 20rpx;
				padding-left: 110rpx;
				display: flex;
				.delete{
					margin-right: 20rpx;
					margin-left: auto;
					color: $lf-text-blue;
				}
			}
			.child{
				padding-left: 110rpx;
				padding-top: 20rpx;
				.child-comment{
					display: flex;
					align-items: center;
					image{
						width: 50rpx;
						height: 50rpx;
						border-radius: 50rpx;
					}
					.child-info{
						display: flex;
						flex-direction: column;
						flex: 1;
						padding: 10rpx 30rpx;
						.child-title{
							display: flex;
							align-items: center;
							.child-nickname{
								font-size: 28rpx;
							}
							.child-sex-icon{
								margin-left: 5rpx;
								width: 24rpx;
								height: 24rpx;
							}
						}
						.child-other{
							font-size: 25rpx;
							color: $lf-text-grey;
						}
					}
				}
				.child-content{
					padding-left: 80rpx;
				}
				.child-time{
					color: $lf-text-grey;
					margin-top: 20rpx;
					padding-left: 80rpx;
					display: flex;
					.child-delete{
						margin-right: 20rpx;
						margin-left: auto;
						color: $lf-text-blue;
					}
				}
			}
			.more-txt{
				padding-left: 190rpx;
				color: $lf-text-blue;
				font-weight: bold;
				margin: 10rpx 0;
			}
			
		}
		
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
	.input-tool{
		position: fixed;
		bottom: 0;
		background-color: #f9f8f8;
		width: 100%;
		.input-line{
			display: flex;
			align-items: center;
			padding: 10rpx;
			.input-area{
				padding: 10rpx 20rpx;
				margin-left: 20rpx;
				background-color: #FFFFFF;
				border-radius: 20rpx;
				width: 530rpx;
				max-height: 300rpx;
				min-height: 60rpx;
				font-size: 28rpx;
			}
			.send{
				margin-left: 30rpx;
				border-radius: 20rpx;
				padding: 10rpx 20rpx;
				font-size: 28rpx;
				text-align: center;
				background-color: $lf-bg-blue;
				color: $lf-text-white;
			}
		}
	}
	.comment-load{
		margin-top: 20rpx;
		text-align: center;
	}
	.prompt-wrap{
			display: flex;
			justify-content: center;
			align-items: center;
			flex-direction: column;
			margin-top: 300rpx;
			.icon{
				width: 100rpx;
				height: 100rpx;
			}
			.txt{
				font-size: 28rpx;
				margin-top: 40rpx;
				margin-bottom: 40rpx;
				color: $lf-text-grey;
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
