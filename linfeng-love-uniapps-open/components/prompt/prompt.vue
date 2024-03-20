<template>
	<view>
		<view class="prompt-wrap">
			<image class="icon" src="/static/image/hand.png"></image>
			<text class="txt">{{tipText}}</text>
			<text class="btn" @click="goNext()">{{btnText}}</text>
		</view>
	</view>
</template>

<script  setup>
import { defineProps,toRefs } from "vue";

const props = defineProps({
  tipText: {
    type: String,
    default: ''
  },
  btnText: {
    type: String,
    default: '确定'
  },
  btnUrl: {
    type: String,
    default: ''
  },
  marginTop: {
    type: String,
    default: '400rpx'
  }
})

const {btnUrl, marginTop} = toRefs(props)
const goNext = () => {
	if(btnUrl.value != ''){
		uni.navigateTo({
			url: btnUrl.value
		});
	}
}
 
</script>

<style lang="scss">
	.prompt-wrap{
		display: flex;
		justify-content: center;
		align-items: center;
		flex-direction: column;
		margin-top: v-bind(marginTop);
		.icon{
			width: 300rpx;
			height: 200rpx;
		}
		.txt{
			font-size: 28rpx;
			margin-top: 40rpx;
			margin-bottom: 40rpx;
			color: $lf-text-grey;
		}
		.btn{
			width: 200rpx;
			font-size: 34rpx;	
			text-align: center;
			padding: 10rpx 0;
			border-radius: 40rpx;
			border: 1rpx solid #000000;
			font-weight: 600;
			margin: 0 auto;
		}
	}
</style>