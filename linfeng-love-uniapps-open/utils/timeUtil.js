//IM时间工具包

export default{
	// 1.获取当前时间 格式 yyyy-mm-dd HH:MM:SS
	getFormatTime(timestamp){
		let time = new Date(timestamp);
		  let year = time.getFullYear();
		  let month = time.getMonth() + 1;
		  let date = time.getDate();
		  let hours = time.getHours();
		  let minute = time.getMinutes();
		  let second = time.getSeconds();
		  if (month < 10) {
			month = "0" + month;
		  }
		  if (date < 10) {
			date = "0" + date;
		  }
		  if (hours < 10) {
			hours = "0" + hours;
		  }
		  if (minute < 10) {
			minute = "0" + minute;
		  }
		  if (second < 10) {
			second = "0" + second;
		  }
		  return (
			year + "-" + month + "-" + date + " " + hours + ":" + minute + ":" + second
		  );
	},
	// 2.显示消息时间格式 昨天，前天，月-日 时：分
	timeShowFormat(startTime){
		let days = this.timeDifferenceDays(startTime, this.getFormatTime(new Date()));
		if(days === 0)	return startTime.substring(11, 16);
		if(days === 1)	return '昨天 ' + startTime.substring(11, 16);
		if(days === 2)	return '前天 ' + startTime.substring(11, 16);
		if(days > 90)  return startTime.substring(0,16)
		else return startTime.substring(5,16)
	},
	// 3.计算两个时间间隔秒数
	TimeDifferenceSeconds(start, end){
		//定义两个变量start,end分别保存开始和结束时间
		var stime =new Date(start).getTime();
		var etime = new Date(end).getTime();
		var usedTime = etime - stime;  //两个时间戳相差的毫秒数
		return Math.floor(usedTime/1000)
	},
	// 4.计算两个时间间隔天数
	timeDifferenceDays(start, end) {
	   //定义两个变量start,end分别保存开始和结束时间
	    var stime =new Date(start.substring(0,10)).getTime();
	    var etime = new Date(end.substring(0,10)).getTime();
	    var usedTime = etime - stime;  //两个时间戳相差的毫秒数
	    return Math.floor(usedTime / (24 * 3600 * 1000))
	},
	// 4.计算两个时间间隔天数
	timeDifferenceHours(start, end) {
	   //定义两个变量start,end分别保存开始和结束时间
	    var stime =new Date(start.substring(0,10)).getTime();
	    var etime = new Date(end.substring(0,10)).getTime();
	    var usedTime = etime - stime;  //两个时间戳相差的毫秒数
	    return Math.floor(usedTime / (3600 * 1000))
	},
	getNextMonthFirstDay() {
		let currentDate = new Date();
		let currentMonth = currentDate.getMonth() +1;
		let nextMonthFirstDay = '';
		if(currentMonth < 9){
			nextMonthFirstDay = "0" + (currentMonth + 1) + '月01日'
		}else if(currentMonth == 12){
			nextMonthFirstDay = (currentDate.getFullYear() + 1) + '年01月01日'
		}else{
			nextMonthFirstDay = currentMonth + 1 + '月01日';
		}
		return nextMonthFirstDay
	},
	getNextSeasonFirstDay() {
		let currentDate = new Date();
		let currentMonth = currentDate.getMonth() +1;
		let nextSeasonFirstDay = '';
		if(currentMonth < 4){
			nextSeasonFirstDay = '04月01日'
		}else if(currentMonth < 7){
			nextSeasonFirstDay = '07月01日'
		}else if(currentMonth < 10){
			nextSeasonFirstDay = '10月01日'
		}else{
			nextSeasonFirstDay = (currentDate.getFullYear() + 1) + '年01月01日'
		}
		return nextSeasonFirstDay
	},
	getNextYearFirstDay() {
		let currentDate = new Date();
		return (currentDate.getFullYear() + 1) + '年01月01日'
	},
}