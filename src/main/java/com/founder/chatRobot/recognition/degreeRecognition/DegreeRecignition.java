package com.founder.chatRobot.recognition.degreeRecognition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.context.manager.common.ContextManager;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;
import com.founder.chatRobot.recognition.common.interfaces.old.Recognition;

public class DegreeRecignition implements Recognition {
	private static List<String> degreeWords = new ArrayList<String>(20);
	private static List<String> extentVerbs = new ArrayList<String>(20);
	private static String[] degreeWordsArray = { "少", "多", "高", "低" };
	private static String[] extentVerbsArray = { "减少", "降低", "增加", "提高", "少点",
			"降点", "优惠", "便宜" };
	private static String[] echo = { "啊", "唉", "吧", "梆", "嘿", "乎", "哗", "喀",
			"啦", "哦", "哇", "呀", "哟", "吱", "兮", "叽", "咔", "咚", "哒", "咦", "咪",
			"唔", "唰", "嗒", "嗖", "嘟", "嗬", "嗯", "嗨", "嘣", "嘭", "噢", "嚓", "的" ,"这么" , "那么"};
	private Entity degreeEntity;

	public DegreeRecignition(KnowledgeManager knowledgeManager,
			ContextManager contextManager) {
		for (String word : degreeWordsArray) {
			degreeWords.add(word);
		}

		for (String word : extentVerbsArray) {
			extentVerbs.add(word);
		}

		this.degreeEntity = (Entity) knowledgeManager.uniqueIndex("Degree");
	}

	@Override
	public ConditionBean recogn(String sentence, String sessionId,
			String userId, String position) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			TypeUnmatchException {
		String[] pouns = { "，", "。", "？", "！", ",", ".", "!", "?", " " };
		List<String> parts, tmp;
		String[] tmpParts;
		boolean hasExtentVerb = false, hasDegreeWord = false;

		ConditionBean condition = new ConditionBean(sentence);
		condition.setPosition(position);
		condition.setSessionId(sessionId);
		condition.setUserId(userId);

		for (int i = 0; i < echo.length; i++) {
			sentence = sentence.replaceAll(echo[i], "");
		}

		parts = new ArrayList<String>(1);
		parts.add(sentence);

		for (String poun : pouns) {
			// System.out.println(parts.size());
			tmp = new LinkedList<String>();
			for (String partSentence : parts) {
				int pos;
				if ((pos = partSentence.indexOf(poun)) >= 0
						&& (pos < partSentence.length() - 1)) {
					tmpParts = partSentence.split(poun);
					for (String tmpString : tmpParts) {
						tmp.add(tmpString);
					}
				} else
					tmp.add(partSentence);
			}
			parts = tmp;
		}

		for (String part : parts) {
			part = part.toLowerCase();
			// System.out.println("pppp : " + part);
			for (String word : extentVerbsArray) {
				if (part.contains(word) && part.length() - word.length() <= 1) {
					if (!hasExtentVerb) {
						hasExtentVerb = true;
						FactInfo<Entity> info = new FactInfo<Entity>(
								degreeEntity);
						int pos = sentence.indexOf(word);
						condition.addFact(new EntryInfo<Entity>("Degree", info,
								pos, word.length()));
					}
					part = "";
					// break;
				}
			}

			for (String word : degreeWords) {
				if (part.contains(word) && part.length() - word.length() <= 1) {
					if (!hasExtentVerb && !hasDegreeWord) {
						hasDegreeWord = true;
						FactInfo<Entity> info = new FactInfo<Entity>(
								degreeEntity);
						int pos = sentence.indexOf(word);
						condition.addFact(new EntryInfo<Entity>("Degree", info,
								pos, word.length()));
					}
					part = "";
					// break;
				}
			}

		}

		return condition;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
