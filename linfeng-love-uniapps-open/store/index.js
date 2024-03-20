
import { createStore } from 'vuex';
import request from '@/utils/request.js'

const state = () => ({
	//基本用户信息
	loginUser:{},
    online:false,
	//推荐嘉宾信息
	refreshRecommend:false,
	//IM相关信息
    friendList:[],
    chatMessageMap:new Map(),
    chatSessionList:[],
	totalUnReadChat:0,
	commonSessionList:[],
	bottleMessageMap:new Map(),
	bottleSessionList:[],
	bottleSession:{},
	//业务配置信息
	businessConfig:{},
});

const getters = {
	loginUser: (state) => state.loginUser,
    online: (state) => state.online,
	refreshRecommend: (state) => state.refreshRecommend,
    chatFriendList: (state) => state.friendList,
	chatSessionList: (state) => state.chatSessionList,
	totalUnReadChat: (state) => state.totalUnReadChat,
	chatMessageMap: (state) => state.chatMessageMap,
	commonSessionList: (state) => state.commonSessionList,
	bottleMessageMap: (state) => state.bottleMessageMap,
	bottleSessionList: (state) => state.bottleSessionList,
	bottleSession: (state) => state.bottleSession,
	businessConfig: (state) => state.businessConfig,
};

const mutations = {
	setLoginUser(state, userInfo){
		state.loginUser = userInfo
	},
	setOnline(state, online){
		state.online = online
	},
	setRefreshRecommend(state, flag){
		state.refreshRecommend = flag
	},
	setChatSessionList(state, chatSessionList){
		state.chatSessionList = chatSessionList
	},
	setTotalUnReadChat(state, totalUnReadChat){
		state.totalUnReadChat = totalUnReadChat
	},
	putChatMessage(state, map){
		state.chatMessageMap.set(map.key, map.value)
	},
	removeChatMessage(state, sessionId){
		state.chatMessageMap.delete(sessionId)
	},
	setCommonSessionList(state, commonSessionList){
		state.commonSessionList = commonSessionList
	},
	putBottleMessage(state, map){
		state.bottleMessageMap.set(map.key, map.value)
	},
	setBottleSessionList(state, bottleSessionList){
		state.bottleSessionList = bottleSessionList
	},
	setBottleSession(state, bottleSession){
		state.bottleSession = bottleSession
	},
	setBusinessConfig(state, businessConfig){
		state.businessConfig = businessConfig
	}
};

const actions = {
	//刷新用户信息
	refreshUserInfo(content){
		request.get('user/userInfo').then(res => {
			if (res.code == 0) {
				content.commit('setLoginUser', res.result);
			}
		})
	}, 
	//校验并刷新推荐嘉宾
	checkAndInitRecommend(content){
		request.get('guest/recommend/checkAndInit').then(res => {
			if (res.code == 0) {
				content.commit('setRefreshRecommend', true);
			}
		})
	},
    //初始化朋友列表
    initChatFriendList() {
    	
    },
    //初始化会话列表
    initChatSessionList(content) {
    	request.get('chat/session/list').then(res => {
    		if (res.code == 0) {
				content.commit('setChatSessionList', res.result);
    		}
    	})
    },
	//更新会话列表
	updateChatSessionList(content, chatSession){
		let chatSessionList = content.state.chatSessionList;
		delete chatSession.type;
		
		for(var i=0; i<chatSessionList.length; i++){
			if(chatSessionList[i].sessionId == chatSession.sessionId){
				chatSessionList.splice(i, 1)
				break
			}
		}
		
		chatSessionList.unshift(chatSession);
		content.commit('setChatSessionList', chatSessionList);
		content.dispatch('updateUnReadChatTotal');
	},
	//更新聊天列表
	updateChatList(content, chatMessage){
		let chatMessageMap = content.state.chatMessageMap
		let chatMessagePage = chatMessageMap.get(chatMessage.sessionId)
		if(chatMessagePage == undefined || chatMessagePage == null){
			return
		}
		let chatMessageList = chatMessagePage.list
		let createMessage = true;
		for(var i=0; i<chatMessageList.length; i++){
			if(chatMessageList[i].messageId == chatMessage.messageId){
				chatMessageList[i] = chatMessage
				createMessage = false
				break
			}
		}
		if(createMessage){
			chatMessageList.unshift(chatMessage)
			var map = {
				key: chatMessage.sessionId,
				value: {pageNum: chatMessagePage.pageNum,
					pageSize: chatMessagePage.pageSize,
					list: chatMessageList}
			}
			content.commit('putChatMessage', map);
		}
		
	},
	//初始化会话列表
	initCommonSessionList(content) {
		request.get('message/list').then(res => {
			if (res.code == 0) {
				content.commit('setCommonSessionList', res.result);
			}
		})
	},
	updateCommonSessionList(content, commonSession){
		//更新会话
		let commonSessionList = content.state.commonSessionList;
		for(var i=0; i<commonSessionList.length; i++){
			if(commonSessionList[i].messageSource == commonSession.messageSource){
				commonSessionList.splice(i, 1)[0]
				break
			}
		}
		commonSessionList.unshift(commonSession);
		content.commit('setCommonSessionList', commonSessionList);
		content.dispatch('updateUnReadChatTotal');
		
	},
	//初始化漂流瓶会话列表
	initBottleSessionList(content) {
		request.get('bottle/session/list').then(res => {
			if (res.code == 0) {
				content.commit('setBottleSessionList', res.result);
			}
		})
	},
	//更新漂流瓶会话列表
	updateBottleSessionList(content, bottleSession){
		let bottleSessionList = content.state.bottleSessionList;
		delete bottleSession.type;
		
		for(var i=0; i<bottleSessionList.length; i++){
			if(bottleSessionList[i].bottleId == bottleSession.bottleId){
				bottleSessionList.splice(i, 1)
				break
			}
		}
		
		bottleSessionList.unshift(bottleSession);
		content.commit('setBottleSessionList', bottleSessionList);
	},
	//更新漂流瓶聊天列表
	updateBottleChatList(content, bottleMessage){
		
		let bottleMessageMap = content.state.bottleMessageMap
		
		let bottleMessagePage = bottleMessageMap.get(bottleMessage.bottleId+"")
		if(bottleMessagePage == undefined || bottleMessagePage == null){
			return
		}
		let bottleMessageList = bottleMessagePage.list
		bottleMessageList.unshift(bottleMessage)
		var map = {
			key: bottleMessage.bottleId,
			value: {pageNum: bottleMessagePage.pageNum,
				pageSize: bottleMessagePage.pageSize,
				list: bottleMessageList}
		}
		content.commit('putBottleMessage', map);
		
	},
	//更新未读消息
	updateUnReadChatTotal(content){
		let totalUnReadChat = 0
		content.state.chatSessionList.forEach(chatSession => {
			totalUnReadChat += chatSession.unRead
		})
		content.state.commonSessionList.forEach(commonSession => {
			totalUnReadChat += commonSession.unRead
		})
		content.commit('setTotalUnReadChat', totalUnReadChat);
		
		//在一级页面tarbar页面才不会报错
		let page = getCurrentPages() 
		
		if(page.length == 1){
			if(totalUnReadChat != 0){
				uni.setTabBarBadge({ 
					index: 2,
					text: totalUnReadChat+""
				})
			}else{
				uni.removeTabBarBadge({ 
					index: 2 
				})
			}
		}
	},
	initBusinessConfig(content){
		request.get('config/business').then(res => {
			if (res.code == 0) {
				content.commit('setBusinessConfig', res);
			}
		})
	},
	receiveToDayMission(content){
		request.post('mission/receive').then(res => {
		})
	}
};

// 创建 Vuex.Store 实例
const store = createStore({
    state,
    getters,
    mutations,
    actions,
});

export default store;
