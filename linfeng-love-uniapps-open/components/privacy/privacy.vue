<template>
	<view>
		<!-- 隐私政策弹窗 -->
		<u-mask :show="showPopup">
			<view class="popup-wrap">
				<view class="pop-title">用户隐私保护提示</view>
				<view class="popup-txt">
					感谢您使用本小程序，在使用前您应当阅读井同意
					<text class="blue-color" @click="handleOpenPrivacyContract">{{privacyContractName}}</text>
					，当点击同意并继续时，即表示您已理解并同意该条款内容，该条款将对您产生法律约束力；如您不同意，将无法继续使用小程序相关功能。
				</view>
				<view class="popup-bot">
					<button id="disagree-btn" type="default" @click="handleDisagree">不同意</button>
					<button id="agree-btn" type="primary" open-type="agreePrivacyAuthorization"
						@agreeprivacyauthorization="handleAgreePrivacyAuthorization">同意并继续</button>
				</view>
			</view>
		</u-mask>
	</view>
</template>
 
<script setup>
import { ref, onMounted } from "vue";

const privacyContractName = ref('')
const emits = defineEmits(['agreePrivacy'])
const showPopup = ref(false)

onMounted(() => {
	checkPrivacy()
});

const checkPrivacy = () => {
		uni.getPrivacySetting({
			success: res => {
				if (res.needAuthorization) {
					privacyContractName.value = res.privacyContractName;
					showPopup.value = true
				} else {
					emits('agreePrivacy')
				}
			},
			fail: (err) => {
				console.log(err)
			}
		})
}

const handleDisagree = (e) => {
	showPopup.value = false
	uni.exitMiniProgram()
}
const handleAgreePrivacyAuthorization = (res) => {
	// 用户同意隐私协议事件回调
	// 用户点击了同意，之后所有已声明过的隐私接口和组件都可以调用了
	showPopup.value = false
	//通知父组件
	emits('agreePrivacy')
}
const handleOpenPrivacyContract = () => {
	// 打开隐私协议页面
	uni.openPrivacyContract({
		success: () => {}, // 打开成功
		fail: () => {}, // 打开失败
		complete: (res) => {
			console.log(res, "openPrivacyContract complete");
		}
	})
}
	
</script>
 
<style lang="scss" scoped>
	.popup-wrap {
		width: 540upx;
		box-sizing: border-box;
		padding: 42upx;
		background: white;
		border-radius: 30upx;
		margin: 500rpx auto;
		.pop-title {
			text-align: center;
			font-size: 31upx;
			color: #000;
			font-weight: bold;
			margin-bottom: 20upx;
		}
 
		.blue-color {
			color: $lf-bg-blue;
		}
 
		.popup-txt {
			line-height: 48upx;
			font-size: 28upx;
		}
 
		.popup-bot {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-top: 30upx;
			>button {
				color: #FFF;
				font-size: 26rpx;
				font-weight: 500;
				line-height: 80rpx;
				width: 46%;
				text-align: center;
				height: 80rpx;
				border-radius: 16rpx;
				border: none;
				background: $lf-bg-blue;
			}
			>button:nth-of-type(1){
			  color: $lf-bg-blue;
			  background: #f2f2f2;
			}
		}
	}
</style>
