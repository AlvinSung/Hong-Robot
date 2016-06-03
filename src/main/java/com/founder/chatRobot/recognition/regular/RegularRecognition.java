package com.founder.chatRobot.recognition.regular;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.recognition.common.interfaces.old.Recognition;
import com.founder.chatRobot.recognition.regular.common.Creater;

public class RegularRecognition<T> implements Recognition {
	String regEx;
	Entity attribute;
	String factTypeName;
	String repRegEx;
	Creater<T> constructor;

	public RegularRecognition(String regEx, String repRegEx,
			@SuppressWarnings("rawtypes") Class infoType, Entity attribute,
			String factTypeName) {
		this.regEx = regEx;
		this.attribute = attribute;
		// this.infoType = infoType;
		this.factTypeName = factTypeName;
		this.repRegEx = repRegEx;
		this.constructor = new Creater<T>(infoType);
	}

	public RegularRecognition(String regEx, String repRegEx,
			Creater<T> constructor, Entity attribute, String factTypeName) {
		this.regEx = regEx;
		this.attribute = attribute;
		// this.infoType = infoType;
		this.factTypeName = factTypeName;
		this.repRegEx = repRegEx;
		this.constructor = constructor;
	}

	public String getRegEx() {
		return regEx;
	}

	@SuppressWarnings("unchecked")
	public ConditionBean recogn(String sentence, String sessionId,
			String userId, String position) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			TypeUnmatchException {
		ConditionBean result = new ConditionBean(sentence);
		FactInfo<T> info;

		result.setSessionId(sessionId);
		result.setPosition(position);
		result.setUserId(userId);

		// System.out.println("this is sentence." + sentence);

		Pattern pat = Pattern.compile(this.regEx);
		Matcher mat = pat.matcher(sentence);

		String repString;
		Pattern repPat = Pattern.compile(this.repRegEx);
		Matcher repMat;
		int pos = 0;

		while (mat.find()) {
			// Constructor<T> factInfoConstructor;
			try {
				/*
				 * factInfoConstructor = this.infoType
				 * .getConstructor(mat.group(0).getClass());
				 * factInfoConstructor.setAccessible(true);
				 */

				repMat = repPat.matcher(mat.group());
				if (repMat.find()) {
					repString = repMat.group();
					pos = sentence.indexOf(repString, pos);
					if (pos >= 0) {
						info = new FactInfo<T>(
								this.constructor.create(repString));
						result.addFact(new EntryInfo<T>(this.factTypeName,
								info, pos, repString.length()));
						pos += repString.length();
					}
				}
				mat = pat.matcher(sentence.substring(pos));
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	public static void main(String[] args) {
		String sentence = "码5264是";
		String regEx = "[0-9]{4}";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(sentence);
		boolean rs = mat.find();
		String tmp = mat.group(0);
		sentence = mat.replaceAll("test");
		System.out.println(sentence);
		System.out.println(rs);
		System.out.println(tmp);

	}

}
