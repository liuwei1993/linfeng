<template>
	<view class="step">
		<view v-if="step == 1" class="step-one">
			<view class="title">你的性别</view>
			<view class="avatar">
				<view class="item">
					<image v-show="submitForm.gender == 2" @click="selectBoy()" mode="aspectFill" class="icon" src="/static/image/boy.png"></image>
					<image v-show="submitForm.gender == 1" mode="aspectFill" class="icon" src="/static/image/boy-on.png"></image>
					<text class="txt" :class="submitForm.gender == 1 ? 'txt-boy' : ''">男生</text>
				</view>
				<view class="item">
					<image v-show="submitForm.gender == 1" @click="selectGirl()" mode="aspectFill" class="icon" src="/static/image/girl.png"></image>
					<image v-show="submitForm.gender == 2" mode="aspectFill" class="icon" src="/static/image/girl-on.png"></image>
					<text class="txt" :class="submitForm.gender == 2 ? 'txt-girl' : ''">女生</text>
				</view>
			</view>
			
			<view class="title mt100">你的生日</view>
			
			<view class="step-picker">
				<text :class="submitForm.birthday == '' ? 'grey' : ''" @click="openBirthdayPicker()">{{submitForm.birthday == '' ? '选择生日' : submitForm.birthday}}</text>
				<u-picker v-model="showBirthdayPicker" default-time="2000-01-01" mode="time" start-year="1970" end-year="2005" @confirm="birthdayPickerConfirm"></u-picker>
				<u-line length="60%" margin="30rpx auto" color="#C0C0C0"></u-line>
			</view>
			
			<view class="title mt150">你现在居住在那里</view>
			
			<view class="step-picker">
				<text :class="submitForm.livingCity == '' ? 'grey' : ''" @click="openLivingSelect()">
					{{submitForm.livingCity == '' ? '选择城市' : 
					submitForm.livingProvince == submitForm.livingCity ? submitForm.livingCity : 
					submitForm.livingProvince + ' ' + submitForm.livingCity}}
				</text>
				<u-select v-model="showLivingSelect" mode="mutil-column-auto"  :list="cityList" 
				 value-name="label" @confirm="livingSelectConfirm"></u-select>
				<u-line length="60%" margin="30rpx auto" color="#C0C0C0"></u-line>
				<!-- <view class="location" @click="getLocation()">
					<image mode="aspectFill" class="icon" src="/static/image/location.png"></image>
					<text class="txt txt-boy">点击获取定位</text>
				</view> -->
			</view>
			<view @click="nextStep" class="submit">下一步</view>
		</view>
		
		<view v-if="step == 2" class="step-two">
			<view class="title">你的昵称</view>
			
			<view class="step-picker">
				<u-input class="step-input" v-model="submitForm.userName" input-align="center" placeholder="请输入昵称"/>
				<u-line length="60%" margin="0 auto" color="#C0C0C0"></u-line>
			</view>
			<view class="title mt100">你的头像</view>
			<view class="error">需上传本人近期清晰无遮挡照片</view>
			<lfUploadOne ref="imageUpload" name="file" :max-count="1" :size-type="['original']" :custom-btn="true"
			 :header="header" :action="uploadImgUrl" :auto-upload="true" @on-success="uploadSuccess" @on-remove="uploadRemove"
			 @on-choose-complete="onChooseComplete" @on-uploaded="onUploaded" marginTop="30rpx" borderRadius="50rpx">
			 <template v-slot:addBtn>
					<view class="uploadImage">
						<image v-show="submitForm.gender == 1" class="icon" src="https://pic.linfeng.tech/ecf88b60890b215cf4c583359bd093a5.jpeg"></image>
						<image v-show="submitForm.gender == 2" class="icon" src="https://pic.linfeng.tech/0a5040498e889767371653de1a31c97a.jpeg"></image>
						<view class="slot-txt">上传照片</view>
					</view>
				</template>
				
			</lfUploadOne>
			<view @click="updateUser" class="submit" :class="submitForm.gender == 1 ? 'bg-boy' : 'bg-girl'">开启邂逅</view>
		</view>
		
	</view>
	
</template>

<script  setup>
import { inject, ref } from "vue";
import cityList from "@/assets/json/city.json";
import lfUploadOne from "@/components/lf-upload-one/lfUploadOne.vue";
import request from '@/utils/request.js'
import config from '@/utils/config.js'

const $store = inject('$store')

const step = ref(1)
const submitForm = ref({
	gender : 1,
	birthday: '',
	livingProvince:'',
	livingCity:'',
	avatar:null,
	userName:''
})
const showBirthdayPicker = ref(false)
const showLivingSelect = ref(false)
const uploadImgUrl = config.uploadUrl
const header = ref({token: uni.getStorageSync('token')})

const avatar = ref(null)

const uploadSuccess = (data) => {
	if(data.code == 0){
		submitForm.value.avatar = data.result
	}else{
		uni.showToast({
			icon:'error',
			title:data.msg
		})
	}
}

const onChooseComplete = (lists, name) => {
	uni.showLoading({
					title:'正在上传文件'
				})
}

const onUploaded = (lists, name) => {
	uni.hideLoading()
}

const uploadRemove = (data) => {
	submitForm.value.avatar = null
}
const selectBoy = () => {
	submitForm.value.gender = 1
}
const selectGirl = () => {
	submitForm.value.gender = 2
}
const birthdayPickerConfirm = (selected) =>{
	submitForm.value.birthday = selected.year + '-' + selected.month + '-' + selected.day
}

const openBirthdayPicker = () =>{
 	showBirthdayPicker.value = true
}
const livingSelectConfirm = (selected) =>{
	let province = selected[0];
	let city = selected[1];
	submitForm.value.livingProvince = province.label;
	submitForm.value.livingCity = city.label;
}

const openLivingSelect = () =>{
 	showLivingSelect.value = true
}

const getLocation = () =>{
 	uni.getLocation({
 		type: 'wgs84',
 		success: function (res) {
			console.log(res)
 		},
		fail(err) {
			console.log(err)
		}
 	});
}

const nextStep = () =>{
	if(submitForm.value.birthday == ''){
		uni.showToast({
			title: "请选择你的生日",
			icon: "none"
		});
		return
	}
	if(submitForm.value.livingCity == ''){
		uni.showToast({
			title: "请选择你的居住地址",
			icon: "none"
		});
		return
	}
	
	step.value = 2
}

const updateUser = () =>{
	uni.showLoading({
		mask:true,
		title:'请稍等'
	})
	if(submitForm.value.userName == ''){
		uni.showToast({
			title: "请填写你的昵称",
			icon: "none"
		});
		return
	}
	
	if(submitForm.value.avatar == null){
		uni.showToast({
			title: "请上传你的头像",
			icon: "none"
		});
		return
	}
	
	request.post("user/userInfoFirstEdit", submitForm.value).then(res => {
		uni.hideLoading()
		if (res.code == 0) {
			$store.dispatch('refreshUserInfo');
			uni.switchTab({
				url:'/pages/index/index'
			})
		}
	})
}
</script>

<style lang="scss">
.step{
	.mt30{
		margin-top: 30rpx
	}
	.mt100{
		margin-top:100rpx
	}
	.mt150{
		margin-top:150rpx
	}
	.mt550{
		margin-top:550rpx
	}
	.title{
		text-align: center;
		font-size: 34rpx;
		font-weight: bold;
	}
	.step-one{
		.txt-boy{
			color:$lf-bg-blue
		}
		.txt-girl{
			color:#ff6b6b
		}
		.avatar{
			display: flex;
			padding: 20rpx 100rpx;
			.item{
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;
				flex: 1;
				padding: 20rpx 0;
				border-radius: 20rpx;
				.icon{
					width: 150rpx;
					height: 150rpx;
				}
				.txt{
					margin-top: 20rpx;
					font-size: 30rpx;
					font-weight: bold;
				}
			}
		}
		.step-picker{
			text-align: center;
			font-size: 32rpx;
			margin-top: 30rpx;
			.grey{
				color: $lf-text-grey
			}
			.step-input{
				margin-top: 20rpx;
			}
		}
		.location{
			display: flex;
			justify-content: center;
			align-items: center;
			margin-top: 20rpx;
			.icon{
				width: 40rpx;
				height: 40rpx;
				margin-right: 20rpx;
			}
			.txt{
				font-size: 30rpx;
				font-weight: bold;
			}
		}
	}
	.step-two{
		.error{
			text-align: center;
			font-size: 26rpx;
			color: $lf-text-error;
			margin-top: 30rpx;
		}
		.uploadImage{
			display: flex;
			position: absolute;
			left: 0;
			right: 0;
			border-radius: 10rpx;
		}
		.icon{
			margin: 0 auto;
			width: 300rpx;
			height: 400rpx;
			border-radius: 50rpx;
			margin-top: 30rpx;
		}
		.slot-txt{
			position: absolute;
			bottom: 30rpx;
			left: 0;
			text-align: center;
			right: 0;
			background-color: #FFF;
			width: 150rpx;
			margin: 0 auto;
			font-size: 26rpx;
			border-radius: 30rpx;
			padding: 5rpx 0;
		}
	}
	.submit{
		position: fixed;
		bottom: 50rpx;
		left: 0;
		right: 0;
		width: 50%;
		font-size: 34rpx;
		text-align: center;
		padding: 20rpx 0;
		border-radius: 40rpx;
		color: #fff;
		font-weight: bold;
		align-items: center;
		margin: 0 auto;
		z-index: 99;
		background-color: $lf-bg-blue;
	}
}

</style>