package com.founder.chatRobot.recognition.comparisionMethod.entryInfo;

import java.util.LinkedList;
import java.util.List;

import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.knowledgeMap.conception.common.Conception;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;

public class SemanticEntryInfo<T extends Conception> {
	private EntryInfo<T> entryInfo;
	private List<String> marks;
	@SuppressWarnings("rawtypes")
	private List<SemanticEntryInfo> subEntrys;

	@SuppressWarnings("rawtypes")
	public SemanticEntryInfo(EntryInfo<T> entryInfo) {
		this.entryInfo = entryInfo;
		this.marks = new LinkedList<String>();
		this.subEntrys = new LinkedList<SemanticEntryInfo>();
	}

	public List<String> getMarks() {
		return this.marks;
	}

	public void addMarks(List<String> marks) {
		this.marks.addAll(marks);
	}

	public void addMark(String mark) {
		this.marks.add(mark);
	}

	public void cleanMarks() {
		this.marks.clear();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<SemanticEntryInfo> getSubEntrys(boolean islocalSentence) {
		List<SemanticEntryInfo> result = new LinkedList<SemanticEntryInfo>(
				subEntrys);
		if (islocalSentence) {
			SemanticEntryInfo info;
			List<Object> entityList = (List) this.entryInfo
					.getInfo().getInfo();
			for (Object entity : entityList)
				if (entity instanceof Entity) {
					if (((Entity) entity).isValueComparable()) {
						for (String word : ((Entity) entity).getAdverbList()) {
							//System.out.println(word);
							info = new SemanticEntryInfo(new EntryInfo(
									((Entity) entity).getName(),
									new FactInfo<Entity>(((Entity) entity)
											.getAttributeToValueByWord(word))));
							info.addMark(word);
							result.add(info);
						}
					}
				}
		}
		//System.out.println("gggg : " +result.size());
		return result;
	}

	public void addSubEntrys(
			@SuppressWarnings("rawtypes") List<SemanticEntryInfo> subEntrys) {
		this.subEntrys.addAll(subEntrys);
	}

	public void addSubEntry(
			@SuppressWarnings("rawtypes") SemanticEntryInfo subEntry) {
		this.subEntrys.add(subEntry);
	}

	@SuppressWarnings("rawtypes")
	public EntryInfo getEntryInfo() {
		return entryInfo;
	}

}
