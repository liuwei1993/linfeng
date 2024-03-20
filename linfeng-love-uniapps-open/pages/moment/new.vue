<template>
	<view class="info">
		<view class="info-wrap">
			<view class="content">
				<textarea placeholder="现在在想些什么？" class="txt" maxlength="1000" :auto-height="true"
					v-model="momentInfo.content"></textarea>
			</view>
			<view class="post-img-wrap" v-if="momentInfo.mediaType == 1 && momentMediaList.length != 0">
				<u-upload ref="uUpload" name="file" :max-count="9" :size-type="['original']" :custom-btn="true" :fileList="momentMediaList"
				 :header="header" :action="uploadUrl" @on-success="uploadMediaSuccess"  @on-remove="removeImage" :width="212" :height="212"
					>
					<template v-slot:addBtn>
						<view class="slot-btn">
							<u-icon name="plus" size="50" color="#797979"></u-icon>
							<view class="slot-txt">
								上传照片
							</view>
						</view>
					</template>
				</u-upload>
			</view>
			<view class="footer">
				<view class="tool2">
					<image @click="selectImage" :class="momentInfo.mediaType != 2?'':'disabled'" src="https://pic.linfeng.tech/love/album.png"></image>
					<view class="submit"  @click="submit" :class="noneContent()?'grey':'blue'">发布</view>
				</view>
			</view>
		</view>
	</view>
	
</template>

<script setup>
import { ref, nextTick } from "vue";
import { onLoad } from "@dcloudio/uni-app";
import request from '@/utils/request.js';
import config from '@/utils/config.js';

const header = ({token: uni.getStorageSync('token')})
const uploadUrl = config.uploadUrl
const momentMediaList = ref([])
const momentInfo = ref({
	content:null,
	mediaType:null,
	mediaList:[],
})
const videoContext = uni.createVideoContext('video',this);
const showPlay = ref(true)
const showTopicPopup = ref(false)

onLoad((options) => {
})
const selectImage = () => {
	if(momentInfo.value.mediaType == 2){
		return
	}
	uni.chooseImage({
		count: 9,
		sizeType: ['original', 'compressed'], 
		sourceType: ['album','camera'], 
		success: async function (res) {
			momentInfo.value.mediaType = 1
			let fileList = res.tempFiles
			let showList = []
			momentMediaList.value = []
			let count = 0
			fileList.forEach(file => {
				uploadFile(file.path).then((url) =>{
					count ++
					showList.push({url:url})
				})
				
			})
			let timer =  setInterval(() => {
				if(count == fileList.length){
					clearInterval(timer);
					momentMediaList.value = showList
				}
			},500);
		}
	});
}

const uploadFile = (filePath) => {
	return new Promise((resolve, reject) => {
		uni.showLoading({
		  title: '正在上传',
		})
		
		uni.uploadFile({
			url: uploadUrl, //文件上传接口
			filePath: filePath,
			header: header,
			name:'file',
			fail(res) {
				uni.hideLoading()
				reject(res)
			},
			success: (res) => {
				uni.hideLoading()
				let data = JSON.parse(res.data)
				resolve(data.result)
			}
		});
	});
	
}

const uploadMediaSuccess = (data) => {
		momentMediaList.value.push({url:data.result})
}

const removeImage = (index) => {
	momentMediaList.value.splice(index, 1)
	if(momentMediaList.value.length == 0){
		momentInfo.value.mediaType = null
	}
}

const noneContent = () => {
	if(momentInfo.value.content == undefined || momentInfo.value.content == null 
	|| momentInfo.value.content == ''){
		return true
	}
	return false
}

const submit = () => {
	
	if(noneContent()){
		return
	}
	
	momentInfo.value.mediaList = momentMediaList.value
	request.post("moment/add", momentInfo.value).then(res => {
		uni.$emit('newMomnet', {});
		uni.navigateBack();
	})
}
	
</script>
<style lang="scss">
.info-wrap{
	border-radius: 50rpx 50rpx 0 0;
	background-color: #FFF;
	.content{
		padding: 40rpx 40rpx;
		height: 250rpx;
		border-radius: 20rpx;
		position: relative;
		font-size: 30rpx;
		textarea::placeholder{
			font-size: 30rpx;
			color:#ccc;
		}
		.txt{
			padding-left: 30rpx;
			padding-top: 30rpx;
		}
	}
	.post-img-wrap {
		padding: 0 20rpx;
		image {
			border-radius: 10rpx;
			margin: 10rpx;
		}
		.slot-btn {
			margin: 10rpx;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			width: 212rpx;
			height: 212rpx;
			border-radius: 10rpx;
			background-color: $lf-bg-grey-week;
		}
		.slot-txt {
			margin-top: 17rpx;
			font-size: 28rpx;
			line-height: 33rpx;
			opacity: 0.5;
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
	.my-topic {
		margin-top: 20rpx;
		margin-left: 30rpx;
		.item {
			padding: 10rpx 20rpx;
			border-radius: 28rpx;
			font-size: 28rpx;
			display: inline-block;
			margin-right: 20rpx;
			margin-bottom: 20rpx;
			background-color: $lf-bg-grey-week;
		}
	}
	.topic{
		position: relative;
		.select-header{
			position: fixed;
			height: 120rpx;
			line-height: 120rpx;
			font-size: 34rpx;
			left: 0;
			right: 0;
			display: flex;
			justify-content: center;
			background-color: $lf-bg-white;
		}
		.topic-select-wrap{
			padding-top: 100rpx;
			display: flex;
			flex-direction: column;
			.topic-select{
				display: flex;
				align-items: center;
				height: 100rpx;
				.topic-title{
					font-size: 28;
					margin-left: 30rpx;
				}
				.join{
					font-size: 28;
					color: $lf-text-grey;
					margin-left: 80rpx;
				}
				image{
					width: 40rpx;
					height: 40rpx;
					margin-right: 40rpx;
					margin-left: auto;
				}
			}
		}
	}
	
	.footer{
		position: fixed;
		bottom: 50rpx;
		width: 100%;
		padding: 10rpx;
		color: $lf-text-grey;
		.tool1{
			display: flex;
			align-items: center;
			margin-bottom: 20rpx;
			border-bottom: 1px solid #ccc;
			padding: 20rpx 30rpx;
			.privacy{
				margin-left: 20rpx;
			}
			.address{
				margin-right: 10rpx;
				margin-left: auto;
				max-width: 300rpx;
				text-overflow: ellipsis;
				overflow: hidden;
				white-space: nowrap;
			}
			.btn{
				border-radius: 40rpx;
				padding: 10rpx 20rpx;
				font-size: 28rpx;
				text-align: center;
				border: 1px solid #ccc;
			}
		}
		.tool2{
			display: flex;
			align-items: center;
			image{
				margin-left: 40rpx;
				width: 60rpx;
				height: 60rpx;
			}
			.disabled{
				opacity: 0.5;
			}
			.submit{
				margin-left: auto;
				margin-right: 30rpx;
				border-radius: 40rpx;
				padding: 10rpx 20rpx;
				font-size: 28rpx;
				text-align: center;
				color: $lf-text-white;
			}
			.blue{
				background-color: $lf-bg-blue;
				
			}
			.grey{
				background-color: $lf-bg-grey;
			}
		}
		
	}
	
}		
</style>
