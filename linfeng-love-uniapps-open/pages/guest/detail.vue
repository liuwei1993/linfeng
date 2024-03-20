<template>
	
	<view class="index">
		<Guest :guestUser="guestUser" :topHeight="topHeight" :showNext="false"></Guest>
	</view>
</template>

<script setup>
import { inject, ref } from "vue";
import { onLoad, onShow } from "@dcloudio/uni-app";
import request from '@/utils/request.js'
import Guest from '@/pages/guest/Guest.vue'

const $store = inject('$store')
const guestUser = ref({
	oid:''
})
const showAction = ref(false)
const topHeight = ref('0px')
const userInfo = ref({
	oid:''
})

const showLike = ref(true)


onLoad((options) => {
	getGuestUserDetail(options.uid)
	userInfo.value = $store.state.loginUser
})

const getGuestUserDetail = (uid) => {
	request.get('guest/detail', {
		guestUid: uid
	}).then(res => {
		guestUser.value = res.result
	})
}

</script>

<style>
</style>
<style lang="scss" scoped>

</style>
