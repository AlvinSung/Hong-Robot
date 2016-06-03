package com.founder.chatRobot.recognition.comparisionMethod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.manager.common.ContextManager;
import com.founder.chatRobot.recognition.common.interfaces.old.Recognition;
import com.founder.chatRobot.recognition.comparisionMethod.entryInfo.SemanticEntryInfo;

public class ComparisionRecognition implements Recognition {
	@SuppressWarnings("rawtypes")
	Map<Integer, SemanticEntryInfo> overallSemanticEntrys;
	ContextManager contextManager;

	@SuppressWarnings("rawtypes")
	public ComparisionRecognition(ContextManager contextManager) {
		this.contextManager = contextManager;
		overallSemanticEntrys = new Hashtable<Integer, SemanticEntryInfo>();
	}

	/*
	 * private class Result {
	 * 
	 * @SuppressWarnings("rawtypes") private Set<EntryInfo> entryInfo; private
	 * String sentence;
	 * 
	 * private Result(@SuppressWarnings("rawtypes") Set<EntryInfo> entryInfo,
	 * String sentence) { this.entryInfo = entryInfo; this.sentence = sentence;
	 * }
	 * 
	 * @SuppressWarnings("rawtypes") private Set<EntryInfo> getEntryInfo() {
	 * return entryInfo; }
	 * 
	 * private String getSentence() { return sentence; } }
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Set<EntryInfo> subEntryRecogn(String sentence,
			SemanticEntryInfo overallEntry, boolean isLocalSentence) {
		Set<EntryInfo> result = new HashSet<EntryInfo>();
		Map<String, EntryInfo> marks = new Hashtable<String, EntryInfo>();
		List<SemanticEntryInfo> subEntryList;

		// System.out.println("fffffffffffffffffff : " + marks.keySet().size());

		if (overallEntry == null
				|| (subEntryList = overallEntry.getSubEntrys(isLocalSentence)) == null) {
			return null;
		}

		for (SemanticEntryInfo subEntry : subEntryList) {
			Iterator<String> iterator = subEntry.getMarks().iterator();
			while (iterator.hasNext()) {
				String mark = iterator.next();
				marks.put(mark, subEntry.getEntryInfo());
			}
		}
		// System.out.println("fffffffffffffffffff : " + marks.keySet().size());

		for (String overAllEntry : marks.keySet()) {
			/*
			 * if (sentence.contains(overAllEntry)) { EntryInfo entryInfo =
			 * marks.get(overAllEntry); int pos = 0; for (int i = 0; pos == -1
			 * || i < sentence.length(); i = pos + overAllEntry.length()) { pos
			 * = sentence.indexOf(overAllEntry, i); result.add(new
			 * EntryInfo(entryInfo.getTypeName(), entryInfo .getInfo(), pos,
			 * overAllEntry.length())); } }
			 */

			// System.out.println(overAllEntry);
			EntryInfo entryInfo = marks.get(overAllEntry);
			for (int pos = 0; pos < sentence.length(); pos += overAllEntry
					.length()) {
				pos = sentence.indexOf(overAllEntry, pos);
				// System.out.println(pos);
				if (pos >= 0) {
					// System.out.println(entryInfo.getTypeName());
					result.add(new EntryInfo(entryInfo.getTypeName(), entryInfo
							.getInfo(), pos, overAllEntry.length()));
				} else
					break;
			}

		}

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Set<EntryInfo> getOverallEntry(String sentence) {
		List<EntryInfo> result = new LinkedList<EntryInfo>();
		Map<String, EntryInfo> marks = new Hashtable<String, EntryInfo>();
		LinkedList<String> indexs = new LinkedList<String>();
		List<List<String>> hash = new ArrayList<List<String>>(100);

		for (int i = 0; i < 100; i++) {
			hash.add(new LinkedList<String>());
		}
		// System.out.println(1);

		for (SemanticEntryInfo semanticEntryInfo : this.overallSemanticEntrys
				.values()) {
			for (String mark : (List<String>) semanticEntryInfo.getMarks()) {
				marks.put(mark, semanticEntryInfo.getEntryInfo());
				// System.out.println(mark);
				hash.get(mark.length()).add(mark);
			}
		}

		// System.out.println(2);

		for (int i = 99; i >= 0; i--) {
			for (String tmp : hash.get(i))
				indexs.addLast(tmp);
		}

		// System.out.println(3);

		// System.out.println("rrrrr : " + indexs.size());

		for (String overAllEntry : indexs) {
			if (overAllEntry.equals("")) {
				// System.out.println("Null");
				continue;
			}
			EntryInfo entryInfo = marks.get(overAllEntry);
			for (int pos = 0; pos < sentence.length(); pos += overAllEntry
					.length()) {
				// System.out.println("aa : " + overAllEntry);
				pos = sentence.indexOf(overAllEntry, pos);
				// System.out.println("bb" + pos);
				if (pos == -1)
					break;
				else {
					result.add(new EntryInfo(entryInfo.getTypeName(), entryInfo
							.getInfo(), pos, overAllEntry.length()));
				}
			}

		}

		// System.out.println(4);

		// System.out.println("2222222 : " + result.size());

		return new HashSet<EntryInfo>(result);
	}

	@SuppressWarnings("rawtypes")
	private List<Fact> getOverallEntryBySession(String sessionId) {
		List<Fact> result = null;

		result = contextManager.getOverallEntryBySession(sessionId);

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Set<EntryInfo> getEntryByOverAll(String sentence, String sessionId,
			Set<EntryInfo> overall) throws TypeUnmatchException {
		Set<EntryInfo> result = new HashSet<EntryInfo>();
		Set<EntryInfo> sessionOverall = new HashSet<EntryInfo>();
		List<Fact> overallFactBySession = this
				.getOverallEntryBySession(sessionId);

		// System.out.println("bbbbbbbb : " + overall.size());

		// here need change for voerallEntry;
		for (EntryInfo info : overall) {
			SemanticEntryInfo overallEntry = this.overallSemanticEntrys
					.get(info.hashCode());
			Set<EntryInfo> tempResult = this.subEntryRecogn(sentence,
					overallEntry, true);
			if (tempResult != null) {
				// System.out.println("jjjj : ");
				result.addAll(tempResult);
			}
		}

		// System.out.println("oooooooooooo");

		boolean isContinue;
		for (Fact fact : overallFactBySession) {
			isContinue = false;
			for (EntryInfo entryInfo : overall) {
				/*
				 * System.out.println("kkkkkkk : " + entryInfo.getInfo() + " : "
				 * + fact.getInfo());
				 */
				if (entryInfo.getInfo().equals(fact.getInfo())) {
					isContinue = true;
					break;
				}
			}
			if (isContinue) {
				continue;
			} else {
				// System.out.println("llllll : " +
				// fact.getType().getTypeName());
				sessionOverall.add(new EntryInfo(fact.getType().getTypeName(),
						fact.getInfo()));
			}
		}

		// here need change for getting overallEntry;
		for (EntryInfo info : sessionOverall) {
			SemanticEntryInfo overallEntry = this.overallSemanticEntrys
					.get(info.hashCode());
			Set<EntryInfo> tempResult = this.subEntryRecogn(sentence,
					overallEntry, false);
			if (tempResult != null) {
				result.addAll(tempResult);
			}
		}

		/*
		 * if (!result.isEmpty()) System.out.println("hhhhhhhh : " +
		 * result.iterator().next().getInfo().getInfo().size());
		 */
		return result;

	}

	public ConditionBean recogn(String sentence, String sessionId,
			String userId, String position) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			TypeUnmatchException {
		ConditionBean condition = new ConditionBean(sentence);
		@SuppressWarnings("rawtypes")
		Set<EntryInfo> overallEntries;

		condition.setSessionId(sessionId);
		condition.setPosition(position);
		condition.setUserId(userId);

		// System.out.println("ffff");
		overallEntries = this.getOverallEntry(sentence);
		// System.out.println("ccccc");

		// System.out.println("ffff : " + overallEntries.size());
		/*
		 * for (@SuppressWarnings("rawtypes") EntryInfo info : overallEntries) {
		 * System.out.println("ffff : " + info.getTypeName()); }
		 */
		/*
		 * for (EntryInfo entry : overallEntries) {
		 * System.out.println(entry.getTypeName() + " : " + entry.startPos() +
		 * " : " + entry.length());
		 * 
		 * }
		 */

		condition.addFacts(overallEntries);
		overallEntries = this.getEntryByOverAll(sentence, sessionId,
				overallEntries);
		// System.out.println("ggggg : ");
		condition.addFacts(overallEntries);
		// condition.setSentence(tmpResult.sentence);
		// System.out.println("aaaa" + condition.getChangedSentence());
		// System.out.println("ddddd");

		return condition;
	}

	public void addOverallSemanticEntrys(
			@SuppressWarnings("rawtypes") Map<EntryInfo, SemanticEntryInfo> overallSemanticEntrys) {
		for (@SuppressWarnings("rawtypes")
		EntryInfo entryInfo : overallSemanticEntrys.keySet()) {
			this.addOverallSemanticEntry(entryInfo,
					overallSemanticEntrys.get(entryInfo));
		}

		// this.overallSemanticEntrys.putAll(overallSemanticEntrys);
	}

	@SuppressWarnings("unchecked")
	public void addOverallSemanticEntry(
			@SuppressWarnings("rawtypes") EntryInfo info,
			@SuppressWarnings("rawtypes") SemanticEntryInfo sem) {
		@SuppressWarnings("rawtypes")
		SemanticEntryInfo old = this.overallSemanticEntrys.get(info.hashCode());
		if (old == null) {
			this.overallSemanticEntrys.put(info.hashCode(), sem);
		} else {
			old.addMarks(sem.getMarks());
		}
	}

	@SuppressWarnings("unchecked")
	public void addOverallSemanticEntry(
			@SuppressWarnings("rawtypes") EntryInfo info,
			@SuppressWarnings("rawtypes") SemanticEntryInfo sem,
			@SuppressWarnings("rawtypes") SemanticEntryInfo... subSemanticEntityInfos) {
		@SuppressWarnings("rawtypes")
		SemanticEntryInfo old = this.overallSemanticEntrys.get(info.hashCode());
		if (old == null) {
			this.overallSemanticEntrys.put(info.hashCode(), sem);
			old = sem;
		} else {
			old.addMarks(sem.getMarks());
		}

		for (@SuppressWarnings("rawtypes")
		SemanticEntryInfo sub : subSemanticEntityInfos) {
			old.addSubEntry(sub);
		}
	}

}
