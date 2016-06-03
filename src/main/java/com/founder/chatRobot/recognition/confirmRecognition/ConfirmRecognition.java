package com.founder.chatRobot.recognition.confirmRecognition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.controler.common.Controler;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.recognition.common.interfaces.old.Recognition;

public class ConfirmRecognition implements Recognition {
	private static List<String> yesWords = new ArrayList<String>(20);
	private static List<String> noWords = new ArrayList<String>(20);
	private static String[] confirmWords = { "是", "好", "恩", "嗯",
			"可以", "对", "要的", "要得", "需要", "行", "要"};
	private static String[] refuseWords = { "不", "不了", "谢谢", "谢谢了", "算了", "不用", "不必",
			"不用了", "不必了", "不是", "不要", "不需要", "不要了", "不需要了" };
	private static String[] echo = { "啊", "唉", "吧", "梆", "嘿", "乎", "哗", "喀",
			"啦", "哦", "哇", "呀", "哟", "吱", "兮", "叽", "咔", "咚", "哒", "咦", "咪",
			"唔", "唰", "嗒", "嗖", "嘟", "嗬", "嗯", "嗨", "嘣", "嘭", "噢", "嚓", "的" };

	static {
		for (String word : ConfirmRecognition.confirmWords) {
			yesWords.add(word);
		}

		for (String word : ConfirmRecognition.refuseWords) {
			noWords.add(word);
		}
	}

	public ConditionBean recogn(String sentence, String sessionId,
			String userId, String position) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			TypeUnmatchException {
		ConditionBean condition = new ConditionBean(sentence);
		String[] pouns = { "，", "。", "？", "！", ",", ".", "!", "?", " " };
		List<String> parts, tmp;
		String[] tmpParts;
		boolean hasNoWord = false, hasYesWord = false;
		
		/*System.out.println("finding confirm.");
		System.out.println(sentence);*/

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
			for (String word : noWords) {
				if (part.contains(word) && part.length() - word.length() <= 1) {
					hasNoWord = true;
					// System.out.println(Controler.knowledgeManager.uniqueIndex("不同意"));
					FactInfo<Entity> info = new FactInfo<Entity>(
							(Entity) Controler.knowledgeManager
									.uniqueIndex("不同意"));
					int pos = sentence.indexOf(word);
					condition.addFact(new EntryInfo<Entity>("Confirm", info,
							pos, word.length()));
					part = "";
					//break;
				}
			}

			for (String word : yesWords) {
				if (part.contains(word) && part.length() - word.length() <= 1) {
					hasYesWord = true;
					// System.out.println(Controler.knowledgeManager.uniqueIndex("确认"));
					FactInfo<Entity> info = new FactInfo<Entity>(
							(Entity) Controler.knowledgeManager
									.uniqueIndex("确认"));
					int pos = sentence.indexOf(word);
					condition.addFact(new EntryInfo<Entity>("Confirm", info,
							pos, word.length()));
					break;
				}
			}
			if (hasNoWord || hasYesWord) {
				break;
			}
		}

		return condition;
	}

}
