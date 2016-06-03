package com.founder.chatRobot.recognition.actionConfirmRecognition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.controler.common.Controler;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.recognition.common.interfaces.old.Recognition;

public class ActionConfirmRecognition implements Recognition {
	private Collection<String> actionCollection;
	private static List<String> yesPosWords, yesPreWords, noPreWords, confirmWords;
	private static String[] echo = { "啊", "唉", "吧", "梆", "嘿", "乎", "哗", "喀",
		"啦", "哦", "哇", "呀", "哟", "吱", "兮", "叽", "咔", "咚", "哒", "咦", "咪",
		"唔", "唰", "嗒", "嗖", "嘟", "嗬", "嗯", "嗨", "嘣", "嘭", "噢", "嚓" };

	static {
		String[] yesPosWordArray = { "了", "过", "矣" }, yesPreWordArray = {"已经",
				"才", "刚", "刚才", "现已", "已" }, noPreWordArray = { "未", "没", "没有",
				"未曾", "并没", "并没有", "尚未", "未尝" };
		String[] confirmWordArray = { "是", "是的", "恩", "嗯", "对", "有", "需要"};
		//String[] denyWordArray = { "不是", "不是的", "算了", "不用"};
		yesPosWords = new ArrayList<String>(yesPosWordArray.length);
		yesPreWords = new ArrayList<String>(yesPreWordArray.length);
		noPreWords = new ArrayList<String>(noPreWordArray.length);
		confirmWords = new ArrayList<String>(confirmWordArray.length);
		//denyWords = new ArrayList<String>(confirmWordArray.length);
		for (String word : yesPosWordArray) {
			yesPosWords.add(word);
		}
		for (String word : yesPreWordArray) {
			yesPreWords.add(word);
		}
		for (String word : noPreWordArray) {
			noPreWords.add(word);
		}
		for (String word : confirmWordArray) {
			confirmWords.add(word);
		}
		/*for (String word : denyWordArray) {
			denyWords.add(word);
		}*/
	}

	public ActionConfirmRecognition(Collection<String> actionCollection) {
		this.actionCollection = actionCollection;
	}

	public ActionConfirmRecognition(String actionWord, String... otherWords) {
		this.actionCollection = new LinkedList<String>();
		this.actionCollection.add(actionWord);
		for (String word : otherWords) {
			this.actionCollection.add(word);
		}
	}

	public ConditionBean recogn(String sentence, String sessionId,
			String userId, String position) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			TypeUnmatchException {
		ConditionBean result = new ConditionBean(sentence);

		result.setPosition(position);
		result.setSessionId(sessionId);
		result.setUserId(userId);

		boolean foundYes = false, foundNo = false;
		String word = null;
		for (String actionWord : this.actionCollection) {
			foundYes = false;
			foundNo = false;
			for (String noPreWord : ActionConfirmRecognition.noPreWords) {
				word = noPreWord + actionWord;
				//System.out.println("noword : " + word);
				int index = sentence.indexOf(word);
				if (index >= 0) {
					foundNo = true;
					break;
				}
			}
			
			if(!foundNo){
				for (String noPreWord : ActionConfirmRecognition.noPreWords) {
					word = noPreWord;
					//System.out.println("noword : " + word);
					int index = sentence.indexOf(word);
					if (index >= 0) {
						foundNo = true;
						break;
					}
				}
			}

			if (!foundNo) {
				for (String yesPreWord : ActionConfirmRecognition.yesPreWords) {
					word = actionWord + yesPreWord;
					int index = sentence.indexOf(word);
					if (index >= 0) {
						foundYes = true;
						break;
					}
				}
				if (!foundYes) {
					for (String yesPosWord : ActionConfirmRecognition.yesPosWords) {
						word = actionWord + yesPosWord;
						int index = sentence.indexOf(word);
						if (index >= 0) {
							foundYes = true;
							break;
						}
					}
				}
			}

			if (foundYes || foundNo) {
				break;
			}
		}
		
		if(!foundNo && !foundYes){
			String[] pouns = { "，", "。", "？", "！", ",", ".", "!", "?", " " };
			List<String> parts, tmp;
			String[] tmpParts;
			
			for (int i = 0; i < echo.length; i++) {
				sentence = sentence.replaceAll(echo[i], "");
			}

			parts = new ArrayList<String>(1);
			parts.add(sentence);

			for (String poun : pouns) {
				//System.out.println(parts.size());
				tmp = new LinkedList<String>();
				for (String partSentence : parts) {
					int pos;
					if((pos = partSentence.indexOf(poun)) >= 0 
							&& (pos < partSentence.length() - 1) ){
						tmpParts = partSentence.split(poun);
						for (String tmpString : tmpParts) {
							tmp.add(tmpString);
						}
					}else
						tmp.add(partSentence);
				}
				parts = tmp;
			}

			for (String part : parts) {
				part = part.toLowerCase();
				//System.out.println("pppp : " + part);
				for (String yesPosWord : ActionConfirmRecognition.confirmWords) {
					if (part.contains(yesPosWord)){
						if(part.length() - yesPosWord.length() <= 1) {
							foundYes = true;
							//System.out.println(Controler.knowledgeManager.uniqueIndex("不同意"));
							FactInfo<Entity> info = new FactInfo<Entity>(
									(Entity) Controler.knowledgeManager.uniqueIndex("不同意"));
							int pos = sentence.indexOf(yesPosWord);
							result.addFact(new EntryInfo<Entity>("Confirm", info,
									pos, yesPosWord.length()));
							break;
						}
					}
				}
				if(foundYes){
					break;
				}
			}
		}
		

		if (foundYes) {
			FactInfo<Entity> info = new FactInfo<Entity>(
					(Entity) Controler.knowledgeManager.uniqueIndex("确认"));
			int pos = sentence.indexOf(word);
			result.addFact(new EntryInfo<Entity>("Confirm", info, pos, word
					.length()));
		} else if (foundNo) {
			FactInfo<Entity> info = new FactInfo<Entity>(
					(Entity) Controler.knowledgeManager.uniqueIndex("不同意"));
			int pos = sentence.indexOf(word);
			result.addFact(new EntryInfo<Entity>("Confirm", info, pos, word
					.length()));
		}

		return result;
	}

}
