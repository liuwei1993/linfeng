/**
 * -----------------------------------
 *  Copyright (c) 2021-2024
 *  All rights reserved, Designed By linfeng.tech , linfengtech.cn
 *  林风婚恋交友开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/yuncoder001/linfeng-love.git
 *  商业版授权联系技术客服	 QQ:  973921677/3582996245
 *  严禁分享、盗用、转卖源码或非法牟利！
 *  版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.InetAddress;
import java.net.UnknownHostException;


@Slf4j
@EnableAsync
@EnableTransactionManagement
@SpringBootApplication
public class Applications {

	public static void main(String[] args) throws UnknownHostException {
		ConfigurableApplicationContext application = SpringApplication.run(Applications.class, args);
		Environment env = application.getEnvironment();
		log.info("\n----------------------------------------------------------------\n\t" +
						"林风婚恋交友开源版 '{}' 运行成功! 访问连接:\n\t" +
						"Swagger文档: \t\thttp://{}:{}/doc.html\n\t" +
						"数据库监控: \t\thttp://{}:{}/druid\n" +
						"----------------------------------------------------------------",
				env.getProperty("spring.application.name"),
				InetAddress.getLocalHost().getHostAddress(),
				env.getProperty("server.port"),
				"127.0.0.1",
				env.getProperty("server.port"));
	}

}