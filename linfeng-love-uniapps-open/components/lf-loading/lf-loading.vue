<template>
	<u-mask :show="true" :mask-click-able="false" :custom-style="{background: '#ffffff'}">
		<view class="wrap">
			<view class="loading">
			</view>
		</view>
	</u-mask>
	
</template>

<script>
	export default {
		name:"lf-loading",
		data() {
			return {
				
			};
		}
	}
</script>

<style lang="scss" scoped>
.wrap{
	width: 100%;
	margin: auto;
	height: 100%;
	position: absolute;
	z-index: 100000;
	display: flex;
	align-items: center;
	text-align: center;
	justify-content: center;
}
.loading{
	width: 60rpx;
	height: 60rpx;
	background: 0 0;
	border-radius: 50%;
	border: 4px solid;
	border-color: #e5e5e5 #e5e5e5 #e5e5e5 #2F85FC;
	animation: u-circle 1s linear infinite;
}

@keyframes u-circle {
	0% {
		-webkit-transform: rotate(0deg);
		transform: rotate(0deg);
	}

	to {
		-webkit-transform: rotate(1turn);
		transform: rotate(1turn);
	}
}
</style>