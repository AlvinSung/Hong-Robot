package com.founder.chatRobot.constants;

import java.io.File;

import com.founder.chatRobot.util.ConfigUtil;

public class ConfigConstants {
	// 所有文件根路径
	public static final String ROOT_PATH = ConfigUtil.getProperty("root_path");
	
	public static final String BACKUP_PATH = ConfigUtil.getProperty("backup_path") == null ? ROOT_PATH + File.separator + "backup" : ConfigUtil.getProperty("backup_path");
	// 项目名称
	public static final String PROJECT_NAME = ConfigUtil.getProperty("project_name");
	// 模块数据路径 classmap.txt...
	public static final String MODEL_DATA_RELATIVE_PATH = ConfigUtil.getProperty("model_data_path");
	public static final String MODEL_DATA_PATH = ROOT_PATH + File.separator + MODEL_DATA_RELATIVE_PATH + File.separator + PROJECT_NAME;
	public static final String MODEL_CLASS_MAP_FILE = MODEL_DATA_PATH + File.separator + "classmap.txt";
	public static final String MODEL_CHOSEN_TERMS_FILE = MODEL_DATA_PATH + File.separator + "chosenTerms.txt";
	public static final String MODEL_MODEL_MODEL_FILE = MODEL_DATA_PATH + File.separator + "model.model";
	public static final String MODEL_TERM_FILE = MODEL_DATA_PATH + File.separator + "term.txt";
	
	// 元数据路径，初始化时导入内存中
	public static final String META_DATA_RELATIVE_PATH = ConfigUtil.getProperty("meta_data_path");
	public static final String META_DATA_PATH = ROOT_PATH + File.separator + META_DATA_RELATIVE_PATH + File.separator + PROJECT_NAME;

	// 语料库路径
	public static final String CORPUS_DATA_RELATIVE_PATH = ConfigUtil.getProperty("corpus_data_path");
	public static final String CORPUS_DATA_PATH = ROOT_PATH + File.separator + CORPUS_DATA_RELATIVE_PATH + File.separator + PROJECT_NAME;
	
	public static final String CORPUS_ENTITY_RELATIVE_PATH = ConfigUtil.getProperty("corpus_entity_path");
	public static final String CORPUS_ENTITY_PATH = CORPUS_DATA_PATH + File.separator + CORPUS_ENTITY_RELATIVE_PATH;
	
	public static final String CORPUS_ORA_RELATIVE_PATH = ConfigUtil.getProperty("corpus_ora_path");
	public static final String CORPUS_ORA_PATH = CORPUS_DATA_PATH + File.separator + CORPUS_ORA_RELATIVE_PATH;
	
	public static final String CORPUS_ANSWER_RELATIVE_PATH = ConfigUtil.getProperty("corpus_answer_path");
	public static final String CORPUS_ANSWER_PATH = CORPUS_DATA_PATH + File.separator + CORPUS_ANSWER_RELATIVE_PATH;
	public static final String CORPUS_ANSWER_FILE = CORPUS_ANSWER_PATH + File.separator + "answers";
	
	public static final String CORPUS_TRAIN_RELATIVE_PATH = ConfigUtil.getProperty("corpus_train_path");
	public static final String CORPUS_TRAIN_PATH = ROOT_PATH + File.separator + CORPUS_DATA_RELATIVE_PATH + File.separator + CORPUS_TRAIN_RELATIVE_PATH;
	
	public static final String LOG_LEVEL = ConfigUtil.getProperty("log_level");

}
