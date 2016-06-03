package com.founder.chatRobot.recognition.normalizer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.recognition.common.interfaces.old.PostTreader;

public class RecognitionNormalizer implements PostTreader {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ConditionBean tread(ConditionBean condition) {
		Map<Integer, EntryInfo> infoMap = new TreeMap<Integer, EntryInfo>();
		ConditionBean result;
		EntryInfo entryInfo;
		String sentence = condition.getSentence();
		List<EntryInfo> entryResult = new LinkedList<EntryInfo>();

		for (EntryInfo info : condition.getFactSet()) {
			entryInfo = infoMap.get(info.startPos());
			if (entryInfo == null || entryInfo.length() < info.length()) {
				infoMap.put(info.startPos(), info);
			}
		}

		int current = 0, next = 0, offset = 0;
		EntryInfo last = null;

		for (EntryInfo info : infoMap.values()) {
			if (info.startPos() < next) {
				current = info.startPos();
				next = current + info.length();
				/*
				 * System.out.println("aaaa : " + info.getTypeName());
				 * 
				 * System.out.println(((Entity) info.getInfo().getInfo().get(0))
				 * .getMainExpress());
				 */
				continue;
			}

			boolean isParent = false;

			if (last != null
					&& last.getInfo().getInfo().get(0) instanceof Entity) {
				for (Entity entity : (List<Entity>) last.getInfo().getInfo()) {
					
					/*System.out.println("aaaa :");
					System.out.println(entity.getParent());
					System.out.println(info.getInfo().getInfo().get(0));*/
					
					if (info.getInfo().getInfo().contains(entity.getParent())
							&& info.startPos()
									- (last.startPos() + last.length()) <= 1) {
						isParent = true;
					}
				}
			}
			
			if (sentence.length() + offset > info.startPos() + info.length()) {
				/*System.out.println("bbbb :");
				System.out.println(info.getTypeName());
				System.out.println(info.startPos());
				System.out.println(info.length());
				System.out.println(((Entity) info.getInfo().getInfo().get(0))
					  .getMainExpress()); 
				System.out.println(isParent);*/
				 

				if (isParent) {
					sentence = sentence.substring(0, info.startPos() + offset)
							+ sentence.substring(info.startPos()
									+ info.length() + offset);
					offset -= info.length();
				} else {
					String entityString = " ";
					for(Object entity : info.getInfo().getInfo()){
						if(entity instanceof Entity){
							entityString += ((Entity)entity).getName() + " ";
						}else{
							entityString += entityString.toString() + " ";
						}
					}
					
					sentence = sentence.substring(0, info.startPos() + offset)
							+ " "
							+ info.getTypeName() + entityString
							+ " "
							+ sentence.substring(info.startPos()
									+ info.length() + offset);
					offset += info.getTypeName().length() + entityString.length() - info.length() + 2;
				}
			} else {
				/*System.out.println("cccc :");
				System.out.println(info.getTypeName());
				System.out.println(info.startPos());
				System.out.println(info.length());*/
				
				String entityString = " ";
				for(Object entity : info.getInfo().getInfo()){
					if(entity instanceof Entity){
						entityString += ((Entity)entity).getName() + " ";
					}else{
						entityString += entityString.toString() + " ";
					}
				}
				
				if (isParent) {
					sentence = sentence.substring(0, info.startPos() + offset);
				} else {
					sentence = sentence.substring(0, info.startPos() + offset)
							+ " " + info.getTypeName() + entityString;
				}
			}
			last = info;
			current = info.startPos();
			next = current + info.length();
			if (!isParent)
				entryResult.add(info);
		}

		result = new ConditionBean(sentence);
		result.setSessionId(condition.getSessionId());
		result.setPosition(condition.getPosition());
		result.setUserId(condition.getUserId());
		result.addFacts(entryResult);

		return result;
	}
}
