package com.founder.chatRobot.svm;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.founder.chatRobot.common.GeneralApi;
import com.founder.chatRobot.svm.featureChoose.TokenizerHelper;

public class SVMModelUtil {
	
	public static final String JSS_UPLOAD_NAME = "SVMModel.zip";

	public static String adjustSentenceTerms(String oriSentence) {
		String sentence = oriSentence;
		sentence = GeneralApi.stringPreproccess(sentence);

		sentence = sentence.replaceAll("[0-9]+", "0"); // 把数字都替换成0，可以得到数字长度特性
		// sentence = GeneralApi.removeSpecailChars(sentence);//去除断句标点
		sentence = sentence.trim();

		String removeModalParticle = GeneralApi.removeModalParticles(sentence)
				.trim();
		if (StringUtils.isNotBlank(removeModalParticle)) {
			sentence = removeModalParticle;
		}
		return sentence;
	}
	
	public static List<String> getSenceTerms(String record) {
		return getSenceTerms(record, 1, 2, true);
	}
	
	public static List<String> getSenceTerms(String record, int minLen,
			int maxLen, boolean needEnglish) {
		String[] splits = record.split("\t");
		if (0 == splits.length) {
			//LogHelper.PRESALES_LOG.warn("record分割异常：" + record);
			return new ArrayList<String>();
		}
		String tmp;
		tmp = splits[0];
		tmp = adjustSentenceTerms(tmp);
		List<String> terms = TokenizerHelper.ngramTokenize(tmp, minLen,
				maxLen, needEnglish);
		for (int i = 1; i < splits.length; ++i) {
			if (!("null".equals(splits[i]) || splits[i].isEmpty())) {
				terms.add(splits[i]);
			}
		}
		return terms;
	}

}
