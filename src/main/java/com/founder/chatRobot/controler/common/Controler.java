package com.founder.chatRobot.controler.common;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.constants.ConfigConstants;
import com.founder.chatRobot.context.manager.common.ContextManager;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;
import com.founder.chatRobot.svm.Identify;

public abstract class Controler {
	protected ContextManager contextManager;
	protected Identify identify;
	public static final KnowledgeManager knowledgeManager = new KnowledgeManager();
	public static Logger chatRecordLogger, debugLoger;
	
	static{
		Properties pro = new Properties();
        pro.put("log4j.rootLogger", ConfigConstants.LOG_LEVEL + ",debugLog,chartRecord");
        
        pro.put("log4j.appender.debugLog", "org.apache.log4j.RollingFileAppender");
        pro.put("log4j.appender.debugLog.File", ConfigConstants.ROOT_PATH + File.separator + "debugLog.log");
        pro.put("log4j.appender.debugLog.Threshold", "DEBUG");
        pro.put("log4j.appender.debugLog.layout", "org.apache.log4j.PatternLayout");
        pro.put("log4j.appender.debugLog.layout.ConversionPattern", "%n[%d{HH:mm:ss}] [%t] [%p] [%c] -%l -%m%n");
 
        pro.put("log4j.appender.chartRecord", "org.apache.log4j.RollingFileAppender");
        pro.put("log4j.appender.chartRecord.File", ConfigConstants.ROOT_PATH + File.separator + "chartRecord.log");
        pro.put("log4j.appender.chartRecord.Threshold", "INFO");
        pro.put("log4j.appender.chartRecord.layout", "org.apache.log4j.PatternLayout");
        pro.put("log4j.appender.chartRecord.layout.ConversionPattern", "%n[%d{HH:mm:ss}] [%p] -%m%n");
 
        PropertyConfigurator.configure(pro);
		
		//PropertyConfigurator.configure (ConfigConstants.ROOT_PATH + File.separator + "log4j.properties");
		chatRecordLogger = LogManager.getLogger("chartRecord");
		debugLoger = LogManager.getLogger("debugLog");
	}

	public abstract void init();

	public abstract List<ResultBean> tread(String sentence, String sessionId,
			String userId, String posision);

	public void addSimpleTask(String typeName, String chineseTypeName,
			String answer) {
		// TODO Auto-generated method stub

	}

}
