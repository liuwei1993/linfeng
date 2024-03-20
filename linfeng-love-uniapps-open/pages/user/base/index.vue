<template>
	<view class="base">
		<!-- 头像 -->
		<lfUploadOne ref="imageUpload" name="file" :max-count="1" :size-type="['original']" :custom-btn="true" :fileList="avatarList"
		 :header="header" width="93%" height="800" :action="uploadImgUrl" :auto-upload="true" @on-choose-complete="chooseComplete" @on-success="uploadSuccess">
		</lfUploadOne>
		
		<!-- 基本信息 -->
			<view class="info-wrap u-skeleton-fillet">
				<view class="info-title">
					<image class="icon" mode="aspectFill" src="/static/image/base.png"></image>
					<text>基本信息</text>
					<view class="edit mr20"  @click="editMain()">编辑</view>
				</view>
				
				<view class="name-main">
					<view class="name">
						<text>{{userInfo.userName}}</text>
					</view>
				</view>
				
				<view class="grid-wrap">
					<view class="info-base-item">
						<image mode="aspectFill" src="/static/image/birthday.png"></image>
						<text class="sub">{{userInfo.birthdayYear}}年 · {{userInfo.constellation}}</text>
					</view>
					<view class="info-base-item">
						<image v-if="userInfo.gender == 1" mode="aspectFill" src="/static/image/man.png"></image>
						<image v-else mode="aspectFill" src="/static/image/woman.png"></image>
						<text class="sub">{{userInfo.stature}}cm · {{userInfo.weight}}kg</text>
					</view>
					<view class="info-base-item">
						<image mode="aspectFill" src="/static/image/city.png"></image>
						<text class="sub">现居{{userInfo.homeCity}} · {{userInfo.livingProvince}}{{userInfo.livingCity}}人</text>
					</view>
					<view class="info-base-item">
						<image mode="aspectFill" src="/static/image/edu.png"></image>
						<text class="sub">{{userInfo.educationText}} · {{userInfo.school}}</text>
					</view>
					<view class="info-base-item">
						<image mode="aspectFill" src="/static/image/job.png"></image>
						<text class="sub">{{userInfo.jobText}}</text>
					</view>
				</view>
				
			</view>
		
		
		<!-- 身份认证 -->
		
		<!-- 个人形象展示 -->
		<view class="info-wrap u-skeleton-fillet">
			<view class="info-title">
				<image class="icon" mode="aspectFill" src="/static/image/star.png"></image>
				<text>个人形象展示</text>
			</view>
			<view class="post-img-wrap">
				<u-upload ref="uUpload" name="file" :max-count="6" :size-type="['original']" :custom-btn="true" :fileList="userMediaList"
				 :header="header" :action="uploadImgUrl" @on-uploaded="uploadImage" @on-success="uploadMediaSuccess" :auto-upload="true"  @on-remove="removeImage" :width="182" :height="182"
					>
					<template v-slot:addBtn>
						<view class="slot-btn">
							<u-icon name="plus" size="40" color="#797979"></u-icon>
							<view class="slot-txt">
								生活照
							</view>
						</view>
					</template>
				</u-upload>
			</view>
		</view>
		
		<!-- 我的动态 -->
		<view class="info-wrap u-skeleton-fillet">
			<view class="info-title" @click="goToMyMoment()">
				<image class="icon" mode="aspectFill" src="/static/image/star.png"></image>
				<text>我的动态</text>
				<text class="dynamic-count">({{momentListLength}})</text>
				<u-icon name="arrow-right" :size="28" class="arrow-right-wrap"></u-icon>
			</view>
			<view class="moment-img-wrap"  @click="goToMyMoment()" v-if="momentMediaList.length != 0">
				<image v-for="(item,index) in momentMediaList" :key="index" mode="aspectFill" :src="item.url"></image>
			</view>
			<view v-else @click="newMomentPage()" class="moment-none">
				去发布
			</view>
		</view>
			
			<!-- 介绍信息 -->
			<view class="info-wrap u-skeleton-fillet">
				<view class="info-title">
					<image class="icon" mode="aspectFill" src="/static/image/star.png"></image>
					<text>自我介绍</text>
					<view class="edit mr20" @click="editIntro()">编辑</view>
				</view>
				<text class="about-text" :class="{'none':userInfo.intro==null}">{{userInfo.intro==null?'请填写自我介绍':userInfo.intro}}</text>
			</view>
	</view>
	
</template>

<script setup>
import { ref } from "vue";
import { onLoad, onShow } from "@dcloudio/uni-app";
import lfUploadOne from "@/components/lf-upload-one/lfUploadOne.vue";
import request from '@/utils/request.js';
import config from '@/utils/config.js';

const header = ({token: uni.getStorageSync('token')})
const uploadImgUrl = config.uploadUrl
const userInfo = ref({})
const userMediaList = ref([])
const mediaLength = ref([])

const avatarList = ref([])

const uploadMediaLength = ref(0)
const uploadMediaList = ref([])

const momentListLength = ref(0)
const momentMediaList = ref([])

const chooseComplete = () => {
	uni.showLoading({
		mask:true,
		title:'请稍等'
	})
}

const uploadSuccess = (data) => {
	request.post("user/avatarEdit", {
		avatar: data.result
	}).then(res => {
		uni.hideLoading()
		if (res.code == 0) {
			uni.showToast({
				title: '修改成功',
				icon: "none"
			});
		}
	})
}
onLoad(() => {
	getUserMediaList()
})

onShow(() => {
	getUserInfo()
	getUserMomentImage()
})

const uploadImage = (data) => {
	request.post("media/upload", uploadMediaList.value).then(res => {
		if (res.code == 0) {
			uni.showToast({
				title: '上传成功',
				icon: "none"
			});
			uploadMediaList.value = []
		}
	})
}

const uploadMediaSuccess = (data) => {
	uploadMediaList.value.push({
			mediaType: 1,
			url: data.result
	})
}

const removeImage = (index) => {
	
	request.post("media/delete", {
		mediaName:userMediaList.value[index].mediaName,
		mediaType:1
	}).then(res => {
		if (res.code == 0) {
			uni.showToast({
				title: '删除成功',
				icon: "none"
			});
		}
	})
}

const getUserInfo = () => {
	request.get("user/userInfo").then(res => {
		userInfo.value = res.result;
		if(avatarList.value.length == 0){
			avatarList.value = [{url: userInfo.value.avatar}]
		}
	})
}

const getUserMediaList = () => {
	request.get("media/list", {
		mediaType: 1
	}).then(res => {
		userMediaList.value = res.result
		mediaLength.value = res.result.length
	})
}

const getUserIntroList = () => {
	request.get("intro/list").then(res => {
		userIntroList.value = res.result;
	})
	
}

const getUserTagList = () => {
	request.get('tag/list').then(res => {
		userTagList.value = res.result
	})
}

const getUserMomentImage = () => {
	request.get('moment/media/list').then(res => {
		momentListLength.value = res.result.momentListLength
		momentMediaList.value = res.result.momentMediaList
	})
}

const newMomentPage = () => {
	realName.checkRealName().then(realname =>{
		if(realname){
			uni.navigateTo({
				url:'/pages/moment/new'
			})
		}
	})
}

const editMain = () => {
	uni.navigateTo({
		url: '/pages/user/base/main'
	});
}

const editIntro = () => {
	uni.navigateTo({
		url: '/pages/user/base/intro'
	});
}

const goToMyMoment = () => {
	uni.navigateTo({
		url:'/pages/moment/my'
	})
}

const toNav = (url) =>{
	uni.navigateTo({
		url:url
	})
}
</script>

<style>
</style>
<style lang="scss" scoped>
	.base{
		padding-bottom:20rpx
	}
	.error-item{
		display: flex;
		margin-top: 20rpx;
		justify-content: center;
		align-items: center;
		.error-txt{
			font-size: 26rpx;
			color: #fd2c2c;
			margin-left: 20rpx;
		}
		.error-btn{
			margin-right: 50rpx;
			margin-left: auto;
			background-color: #efcecc;
			padding: 10rpx 30rpx;
			border-radius: 40rpx;
			color: #fd2c2c;
			font-size: 24rpx;
		}
	}
	.info-wrap{
		padding: 20rpx;
		border-radius: 20rpx;
		box-shadow: 5rpx 5rpx 20rpx #e6e6e6;
		background-color: $lf-bg-white;
		margin: 30rpx;
		.score{
			background-color: $lf-bg-blue-week;
			color: $lf-text-blue;
			font-size: 28rpx;
			height: 50rpx;
			line-height: 50rpx;
			border-radius: 40rpx;
			text-align: center;
			padding: 0 15rpx;
		}
		.edit{
			background-color: $lf-bg-grey-week;
			color: $lf-text-grey;
			font-size: 28rpx;
			height: 50rpx;
			line-height: 50rpx;
			border-radius: 40rpx;
			text-align: center;
			padding: 0 15rpx;
		}
	}
	.mr20{
		margin-right: 20rpx;
		margin-left: auto;
	}
	.slot-btn {
		margin: 10rpx;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		width: 182rpx;
		height: 182rpx;
		border-radius: 10rpx;
		background-color: $lf-bg-grey-week;
	}
	.slot-txt {
		margin-top: 17rpx;
		font-size: 24rpx;
		line-height: 33rpx;
		opacity: 0.5;
	}
	.progress-bar{
		//position:fixed;
		display: flex;
		top: 0;
		align-items: center;
		padding: 10rpx 30rpx;
		.txt{
			width:100rpx;
			font-size: 30rpx;
		}
		.line{
			display: flex;
			align-items: center;
			position: relative;
			width: 100%;
			.green{
				position: absolute;
				border: 5rpx solid $lf-bg-blue;
				height:5rpx;
				width: 30%;
				border-radius: 5rpx;
				z-index: 2;
			}
			.number{
				width: 40rpx;
				height: 40rpx;
				line-height: 40rpx;
				border-radius: 20rpx;
				font-size: 26rpx;
				color: $lf-text-white;
				background-color: $lf-bg-blue;
				margin-left: 30%;
				z-index: 2;
				text-align: center;
			}
			.grey{
				position: absolute;
				border: 5rpx solid #b8b8b8;
				height:5rpx;
				width: 100%;
				border-radius: 5rpx;
			}
		}
	}
	
	
	
	.clock-wrap{
		margin-left: 20rpx;
	}
	.thumb-wrap{
		margin-left: 20rpx;
	}
	.name-main{
		padding-left: 30rpx;
	}
	.name{
		font-weight: bold;
		font-size: 40rpx;
		float: left;
	}
	.sex{
		background-color: $lf-bg-blue-week;
		height: 40rpx;
		width: 40rpx;
		color: $lf-text-blue;
		align-items: center;
		text-align: center;
		border-radius: 100rpx;
		display: inline-block;
		margin-left: 30rpx;
		font-size: 30rpx;
		line-height: 40rpx;
	}
	.level{
		font-weight: normal;
		background-color: $lf-bg-blue;
		font-size: 20rpx;
		border-radius: 14rpx;
		width: 130rpx;
		color: $lf-text-white;
		float: left;
		padding-left: 15rpx;
		margin-top: 30rpx;
	}
	.tag-box {}
	
	.tag-box .tag {
		padding: 10rpx 20rpx;
		border-radius: 20rpx;
		font-size: 24rpx;
		display: inline-block;
		margin-right: 20rpx;
		margin-bottom: 20rpx;
		background-color: $lf-bg-blue-week;
	}
	.grid-wrap {
		margin-top: 10rpx;
		display: grid;
		grid-template-columns: repeat(1, 1fr);
		grid-gap: 30rpx 15rpx;
		padding: 30rpx;
		width: 100%;	
		.info-base-item{
			display: flex;
			flex-direction: row;
			image {
				width: 32rpx;
				height: 32rpx;
			}
			.sub {
				font-size: 28rpx;
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
		margin-bottom: 20rpx;
		align-items: center;
		.icon{
			width: 40rpx;
			height: 40rpx;
			margin-right: 20rpx;;
		}
		.txt{
			
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
	.about-text{
		font-size: 29rpx;
	}
	.none{
		color: $lf-text-grey
	}
	.tag-box {
		padding: 20rpx;
		.tag {
			padding: 10rpx 20rpx;
			border-radius: 28rpx;
			font-size: 28rpx;
			display: inline-block;
			margin-right: 20rpx;
			margin-bottom: 20rpx;
			background-color: #64beff21;
			color: $lf-bg-blue;
		}
	}
	.post-img-wrap {
		padding: 0 20rpx;
		image {
			border-radius: 10rpx;
			margin: 10rpx;
		}
	}
	.moment-img-wrap {
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
	.moment-none{
		background-color: $lf-bg-blue;
		font-size: 30rpx;
		text-align: center;
		padding: 15rpx 0;
		width: 40%;
		border-radius: 40rpx;
		color: $lf-text-white;
		margin: auto;
		margin-top: 30px;
		margin-bottom: 30rpx;
	}
	.dynamic-count{
		font-size: 28rpx;
		font-weight: normal;
		margin-left: 10rpx;
	}
	
	.arrow-right-wrap{
		float: right;
		margin-right: 30rpx;
		margin-top: 20rpx;
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
				margin-bottom: 10rpx;
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
			.auth-btn-on{
				background-color: $lf-bg-blue;
				padding: 10rpx 20rpx;
				border-radius: 40rpx;
				font-size: 26rpx;
				margin-top: 20rpx;
				color: $lf-text-white;
			}
			.auth-btn-off{
				background-color: $lf-bg-grey;
				padding: 10rpx 20rpx;
				border-radius: 40rpx;
				font-size: 26rpx;
				margin-top: 20rpx;
				color: $lf-text-grey;
			}
		}
	}

</style>
