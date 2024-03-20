<template>
	<view>
		<view class="community">
			<!-- 导航栏 -->
			<view class="custom-nav">
					<u-tabs class="tabs" v-model="defaultTab" :list="pageTab" :current="pageCurrent" @change="tabChange" active-color="#2F85FC"></u-tabs>
			</view>
			
			<view class="content">
				<MomentList ref="momentListRef" :queryForm="queryForm" marginTop="500rpx"></MomentList>
			</view>
			<view class="new-moment">
				<image @click="newMomentPage()" src="/static/image/new-moment.png"></image>
			</view>
		</view>
	</view>
	
</template>

<script setup>
import { inject, ref, onMounted, nextTick } from "vue";
import prompt from "@/components/prompt/prompt.vue";
import MomentList from "./MomentList.vue";
import { onLoad, onUnload, onShow, onPullDownRefresh, onReachBottom, onShareAppMessage } from "@dcloudio/uni-app";
import request from '@/utils/request.js';

const $store = inject('$store')

const userInfo = ref({})
const navHeight = ref('0px')
const statusBarHeight = ref('0px')
const topHeight = ref('0px')
const pageCurrent = ref(0)
const defaultTab = ref(0)
const pageTab = ref([{
						name: '最新'
					}
				])
const queryForm = ref({
		queryType: 3,
		pageNum:1,
		pageSize:15
})
const momentListRef = ref()
const refreshMoment = ref(true)

onLoad(() => {
	navHeight.value = getApp().globalData.navHeight + 'px';
	statusBarHeight.value = getApp().globalData.statusBarHeight + 'px';
	topHeight.value = getApp().globalData.navHeight + getApp().globalData.statusBarHeight + 'px';
	uni.$on('newMomnet',(data)=>{
　　 	momentListRef.value.refreshMomentList()
	});
})

onUnload(() => {
	uni.$off('newMomnet');
})

onShareAppMessage((res) => {
	
	if (res.from === 'button') {
		let momentDetail = res.target.dataset.item
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
			path: '/pages/moment/detail?momentId=' + res.target.id,
			imageUrl: imageUrl,
		}
	}
})

onShow(() => {
	
	nextTick(() => {
		userInfo.value = $store.state.loginUser
		if(refreshMoment.value){
			refreshMoment.value = false
			setTimeout(function(){
				momentListRef.value.refreshMomentList()
			}, 300)
		}
		
	})
	
})

onReachBottom(() => {
	queryForm.value.pageNum = queryForm.value.pageNum + 1
	momentListRef.value.addMomentList()
});

onPullDownRefresh(() => {
	momentListRef.value.refreshMomentList()
	uni.stopPullDownRefresh() //停止刷新

})


const newMomentPage = () => {
	uni.navigateTo({
		url:'/pages/moment/new'
	})
}
</script>

<style lang="scss" scoped>
	.custom-nav{
		height: v-bind(navHeight);
		top: v-bind(statusBarHeight);
		display: flex;
		align-items: center;
		position: fixed;
		background-color: $lf-bg-white;
		width: 100%;
		z-index: 99;
		.tabs{
			margin: 0 auto;
		}
	}
	.content{
		margin-top: v-bind(topHeight);
	}
	.new-moment{
		image{
			position: fixed;
			right: 50rpx;
			/* #ifdef H5 || APP-PLUS */
			bottom: 200rpx;
			/* #endif */
			/* #ifdef MP-WEIXIN */
			bottom: 100rpx;
			/* #endif */
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
</style>
