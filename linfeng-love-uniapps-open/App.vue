<script>
	import $store from '@/store/index.js';
	export default {
		onLaunch: function() {
			// 状态栏高度
			this.globalData.statusBarHeight = uni.getSystemInfoSync().statusBarHeight
			//导航栏高度
			this.globalData.navHeight = 55
			// #ifdef H5
			this.globalData.terminalType = "h5"
			if (uni.getStorageSync('token') != null && uni.getStorageSync('token') != '') {
				$store.dispatch('refreshUserInfo')
			}
			// #endif
			
			// #ifdef MP-WEIXIN
			this.globalData.terminalType = "wx_ma"
			let custom = uni.getMenuButtonBoundingClientRect();
			this.globalData.navHeight = custom.height + (custom.top - this.globalData.statusBarHeight) * 2;
			// #endif
		},
		onShow: function() {
		},
		onHide: function() {
		},
		globalData: {
		}
	}
</script>

<style lang="scss">
	@import "./uni_modules/vk-uview-ui/index.scss";
	/*每个页面公共css */
	.f-wrap {
		padding: 20rpx;
		background-color: #FFFFFF;
		margin-top: 20rpx;
	}
</style>
