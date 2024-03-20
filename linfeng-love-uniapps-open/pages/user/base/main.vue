<template>
	<view>
		<view class="base-info">
			<u-form ref="form1" :model="submitForm" :error-type="errorType" label-width="auto">
				<view class="title">基础资料</view>
				<u-form-item label="昵称" prop="submitForm.userName">
					<u-input v-model="submitForm.userName" placeholder="请输入昵称" input-align="right" />
				</u-form-item>
				<u-form-item label="性别" prop="gender">
					<u-input v-model="submitForm.genderText" disabled input-align="right" />
				</u-form-item>
				<u-form-item label="生日" prop="birthday">
					<u-input v-model="submitForm.birthday" disabled input-align="right" />
				</u-form-item>
				<u-form-item label="身高" right-icon="arrow-right" prop="stature">
					<u-input v-model="statureSelect.current.label" placeholder="请选择身高" input-align="right" @touchend.prevent="openStatureSelect()"/>
					<u-select v-model="statureSelect.show" :default-value="statureSelect.default" :list="statureSelect.list" @confirm="statureSelectConfirm"></u-select>
				</u-form-item>
				<u-form-item label="体重" right-icon="arrow-right" prop="weight">
					<u-input v-model="weightSelect.current.label" disabled placeholder="请选择体重" input-align="right" @touchend.prevent="openWeightSelect()"/>
					<u-select v-model="weightSelect.show" :default-value="weightSelect.default" :list="weightSelect.list" @confirm="weightSelectConfirm"></u-select>
				</u-form-item>
				<u-form-item label="居住城市" right-icon="arrow-right" prop="livingCity">
					<u-input v-model="livingSelect.current.label" disabled placeholder="请选择居住城市" input-align="right" @touchend.prevent="openLivingSelect()"/>
					<u-select v-model="livingSelect.show" mode="mutil-column-auto"  :list="livingSelect.list" @confirm="livingSelectConfirm"></u-select>
				</u-form-item>
				<u-form-item label="家乡" right-icon="arrow-right" prop="homeCity">
					<u-input v-model="homeSelect.current.label" disabled placeholder="请选择家乡" input-align="right" @touchend.prevent="openHomeSelect()"/>
					<u-select v-model="homeSelect.show" mode="mutil-column-auto"  :list="homeSelect.list" @confirm="homeSelectConfirm"></u-select>
				</u-form-item>
				<u-form-item label="婚姻状况" right-icon="arrow-right" prop="marriage">
					<u-input v-model="marriageSelect.current.label" disabled placeholder="请选择婚姻状况" input-align="right" @touchend.prevent="openMarriageSelect()"/>
					<u-select v-model="marriageSelect.show" :value-name="valueName" :label-name="labelName" :list="marriageSelect.list" @confirm="marriageSelectConfirm"></u-select>
				</u-form-item>
				<view class="title">教育及工作资料</view>
				<u-form-item label="毕业院校" prop="school">
					<u-input v-model="submitForm.school" input-align="right" />
				</u-form-item>
				<u-form-item label="最高学历" right-icon="arrow-right" prop="education">
					<u-input v-model="educationSelect.current.label" disabled placeholder="请选择最高学历" input-align="right" @touchend.prevent="openEducationSelect()"/>
					<u-select v-model="educationSelect.show" :value-name="valueName" :label-name="labelName" :list="educationSelect.list" @confirm="educationSelectConfirm"></u-select>
				</u-form-item>
				<u-form-item label="行业/职业" right-icon="arrow-right" prop="job">
					<u-input v-model="jobSelect.current.label" disabled placeholder="请选择行业/职业" input-align="right" @touchend.prevent="openJobSelect()"/>
					<u-select v-model="jobSelect.show" :value-name="valueName" :label-name="labelName" :list="jobSelect.list" @confirm="jobSelectConfirm"></u-select>
				</u-form-item>
				<u-form-item label="年薪" right-icon="arrow-right" prop="salary">
					<u-input v-model="salarySelect.current.label" disabled placeholder="请选择年薪" input-align="right" @touchend.prevent="openSalarySelect()"/>
					<u-select v-model="salarySelect.show" :value-name="valueName" :label-name="labelName" :list="salarySelect.list" @confirm="salarySelectConfirm"></u-select>
				</u-form-item>
			</u-form>
			<view class="submit" @click="submit">保存</view>
		</view>
	</view>
</template>

<script setup>
	
import { inject, ref } from "vue";
import { onLoad, onReady } from "@dcloudio/uni-app";
import cityList from '@/assets/json/city.json';
import request from '@/utils/request.js';

const $store = inject('$store')

const form1=ref(null)
const submitForm = ref({})
const labelName = ref('name')
const valueName = ref('code')
const errorType = ref(['toast'])
const statureSelect = ref({
						default: [25],
						show: false,
						current: {},
						list: []
					})
const weightSelect = ref({
						default: [15],
						show: false,
						current: {},
						list: []
					})

const livingSelect = ref({
						show: false,
						current: {},
						list: cityList
					})
const homeSelect = ref({
						show: false,
						current: {},
						list: cityList
					})
const marriageSelect = ref({
						show: false,
						current: {},
						list: []
					})
const educationSelect = ref({
						show: false,
						current: {},
						list: []
					})
const jobSelect = ref({
						show: false,
						current: {},
						list: []
					})
const salarySelect = ref({
						show: false,
						current: {},
						list: []
					})

const rules = ref({
					userName: [
						{ required: true, message: '请输入昵称', trigger: ['change', 'blur'] }
					],
					stature: [
						{ required: true, type: 'number', message: '请选择身高', trigger: ['change', 'blur'] }
					],
					weight: [
						{ required: true, type: 'number', message: '请选择体重', trigger: ['change', 'blur'] }
					],
					livingCity: [
						{ required: true, message: '请选择居住城市', trigger: ['change', 'blur'] }
					],
					homeCity: [
						{ required: true, message: '请选择家乡', trigger: ['change', 'blur'] }
					],
					marriage: [
						{ required: true, type: 'number', message: '请选择婚姻状况', trigger: ['change', 'blur'] }
					],
					education: [
						{ required: true, type: 'number', message: '请选择最高学历', trigger: ['change', 'blur'] }
					],
					job: [
						{ required: true, type: 'number', message: '请选择行业/职业', trigger: ['change', 'blur'] }
					],
					salary: [
						{ required: true, type: 'number', message: '请选择年薪', trigger: ['change', 'blur'] }
					]
				})

onLoad(() => {
	
	//初始化表单
	initForm()
	
	//初始化下拉框数据
	getAppDict('DT_MARRIAGE').then((data) =>{
		marriageSelect.value.list = data
	})
	getAppDict('DT_EDUCATION').then((data) =>{ 
		educationSelect.value.list = data
	})
	getAppDict('DT_JOB').then((data) =>{
		jobSelect.value.list = data
	})
	getAppDict('DT_SALARY').then((data) =>{
		salarySelect.value.list = data
	})
	
	//初始化身高体重下拉列表
	for(var i=140; i<=220; i++){
		statureSelect.value.list.push({label:i + "cm", value:i})
	}
	
	for(var i=40; i<=120; i++){
		weightSelect.value.list.push({label:i + "kg", value:i})
	}
	
})

onReady(() => {
	form1.value.setRules(rules);
})

const initForm = () => {
	request.get("user/userInfo").then(res => {
		submitForm.value = res.result;
		if(res.result.stature != null && res.result.stature != ''){
			statureSelect.value.current.label = res.result.stature + 'cm'
		}
		if(res.result.weight != null && res.result.weight != ''){
			weightSelect.value.current.label = res.result.weight + 'kg'
		}
		if(res.result.livingCity != null && res.result.livingCity != ''){
			livingSelect.value.current.label = res.result.livingProvince + ' ' + res.result.livingCity
		}
		if(res.result.homeCity != null && res.result.homeCity != ''){
			homeSelect.value.current.label = res.result.homeProvince + ' ' + res.result.homeCity
		}
		if(res.result.homeCity != null && res.result.homeCity != ''){
			homeSelect.value.current.label = res.result.homeProvince + ' ' + res.result.homeCity
		}
		marriageSelect.value.current.label = res.result.marriageText
		educationSelect.value.current.label = res.result.educationText
		jobSelect.value.current.label = res.result.jobText
		salarySelect.value.current.label = res.result.salaryText
	})
}

const openStatureSelect = () =>{
	statureSelect.value.show = true
}
const statureSelectConfirm = (selected) => {
	let current = selected[0];
	statureSelect.value.current = current;
	submitForm.value.stature = current.value;
}
const openWeightSelect = () =>{
	weightSelect.value.show = true
}
const weightSelectConfirm = (selected) => {
	let current = selected[0];
	weightSelect.value.current = current;
	submitForm.value.weight = current.value;
}
const openLivingSelect = () => {
	livingSelect.value.show = true
}
const livingSelectConfirm = (selected) => {
	let province = selected[0];
	let city = selected[1];
	livingSelect.value.current.value = city.label;
	livingSelect.value.current.label = province.label + ' ' + city.label;
	submitForm.value.livingProvince = province.label
	submitForm.value.livingCity= city.label
}
const openHomeSelect = () => {
	homeSelect.value.show = true
}
const homeSelectConfirm = (selected) => {
	let province = selected[0];
	let city = selected[1];
	homeSelect.value.current.value = city.label;
	homeSelect.value.current.label = province.label + ' ' + city.label;
	submitForm.value.homeProvince = province.label
	submitForm.value.homeCity= city.label
}
const openMarriageSelect = () => {
	marriageSelect.value.show = true
}
const marriageSelectConfirm = (selected) => {
	let current = selected[0];
	marriageSelect.value.current = current;
	submitForm.value.marriage = current.value;
}
const openEducationSelect = () => {
	educationSelect.value.show = true
}
const educationSelectConfirm = (selected) => {
	let current = selected[0];
	educationSelect.value.current = current;
	submitForm.value.education = current.value;
}
const openJobSelect = () => {
	jobSelect.value.show = true
}
const jobSelectConfirm = (selected) => {
	let current = selected[0];
	jobSelect.value.current = current;
	submitForm.value.job = current.value;
}
const openSalarySelect = () => {
	salarySelect.value.show = true
}
const salarySelectConfirm = (selected) => {
	let current = selected[0];
	salarySelect.value.current = current;
	submitForm.value.salary = current.value;
}

const submit = () => {
	form1.value.validate(valid => {
		if (valid) {
			request.post("user/userInfoEdit", submitForm.value).then(res => {
				if (res.code == 0) {
					$store.dispatch('refreshUserInfo');
					uni.navigateBack()
				}
			})
		} else {
		}
	});
	
}

const getAppDict = (typeCode) => {
	return new Promise((resolve, reject) => {
		request.post("/config/dict/list", {
			typeCode: typeCode
		}).then(res => {
			return resolve(res.result)
		})
	});
	
}
					
</script>
<style lang="scss">
	.base-info{
		padding: 0 30rpx;
		padding-bottom: 150rpx;
		.title{
			height: 80rpx;
			line-height: 80rpx;
			background-color:$lf-bg-grey-week;
			font-weight: bold;
			margin: 0 -30rpx;
			padding-left: 30rpx;
			font-size: 30rpx;
		}
	}
	.submit{
		position: fixed;
		bottom: 50rpx;
		left: 0;
		right: 0;
		width: 50%;
		background-color: $lf-bg-blue;
		font-size: 34rpx;
		text-align: center;
		padding: 20rpx 0;
		border-radius: 40rpx;
		color: $lf-text-white;
		font-weight: bold;
		align-items: center;
		margin: 0 auto;
		z-index: 99;
	}
</style>
