package com.founder.chatRobot.recognition.manage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.recognition.common.RecognitionManager;
import com.founder.chatRobot.recognition.common.interfaces.old.PostTreader;
import com.founder.chatRobot.recognition.common.interfaces.old.Recognition;

public class InternalRecognitionManager implements RecognitionManager {
	private Map<String, Recognition> recognitionMap = new HashMap<String, Recognition>();
	private List<Recognition> recognitionChain = new LinkedList<Recognition>();
	private Map<String, PostTreader> postTreaderMap = new HashMap<String, PostTreader>();
	private List<PostTreader> postTreaderChain = new LinkedList<PostTreader>();

	public Recognition addRecognition(String name, Recognition recognition) {
		return this.recognitionMap.put(name, recognition);
	}

	public void removeRecognition(String name) {
		this.recognitionMap.remove(name);
	}

	public void activeRecognition(String name) throws NullPointerException {
		Recognition recognition = this.recognitionMap.get(name);
		if (recognition != null) {
			if (this.recognitionChain.contains(recognition))
				this.recognitionChain.remove(recognition);
			this.recognitionChain.add(0, recognition);
		} else {
			throw new NullPointerException("can't find recognition.");
		}

	}

	public void disactiveRecognition(String name) {
		Recognition recognition = this.recognitionMap.get(name);
		if (recognition != null) {
			if (this.recognitionChain.contains(recognition))
				this.recognitionChain.remove(recognition);
		}
	}

	public PostTreader addPostTreader(String name, PostTreader postTreader) {
		return this.postTreaderMap.put(name, postTreader);
	}

	public void removePostTreader(String name) {
		this.postTreaderMap.remove(name);
	}

	public void activePostTreader(String name) throws NullPointerException {
		PostTreader postTreader = this.postTreaderMap.get(name);
		if (postTreader != null) {
			if (this.postTreaderChain.contains(postTreader))
				this.postTreaderChain.remove(postTreader);
			this.postTreaderChain.add(0, postTreader);
		} else {
			throw new NullPointerException("can't find recognition.");
		}

	}

	public void disactivePostTreader(String name) {
		PostTreader postTreader = this.postTreaderMap.get(name);
		if (postTreader != null) {
			if (this.recognitionChain.contains(postTreader))
				this.recognitionChain.remove(postTreader);
		}
	}

	public ConditionBean recogn(String sentence, String sessionId,
			String userId, String position) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			TypeUnmatchException {
		ConditionBean result = new ConditionBean(sentence), tmp;
		result.setPosition(position);
		result.setSessionId(sessionId);
		result.setUserId(userId);

		for (Recognition recognition : this.recognitionChain) {
			tmp = recognition.recogn(sentence.trim(), sessionId, userId,
					position);
			result.addFacts(tmp.getFactSet());
			//System.out.println(recognition.getClass().getName());
		}
		//System.out.println("111 : " + result.getSentence());

		for (PostTreader postTreader : this.postTreaderChain) {
			result = postTreader.tread(result);
			//System.out.println("222 : " + postTreader + " : " + result.getSentence());
		}
		//System.out.println("222 : " + result.getSentence());

		return result;
	}

	public void cleanRecognition(Set<String> recognitionSet) {
		for (String name : recognitionSet) {
			this.disactiveRecognition(name);
			this.removeRecognition(name);
		}

	}

}
