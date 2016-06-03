package com.founder.chatRobot.recognition.common;

import java.util.Set;

import com.founder.chatRobot.recognition.common.interfaces.old.PostTreader;
import com.founder.chatRobot.recognition.common.interfaces.old.Recognition;

public interface RecognitionManager extends Recognition {
	public Recognition addRecognition(String name, Recognition recognition);

	public void removeRecognition(String name);

	public void activeRecognition(String name);

	public void disactiveRecognition(String name);

	public PostTreader addPostTreader(String name, PostTreader postTreader);

	public void removePostTreader(String name);

	public void activePostTreader(String name) throws NullPointerException;

	public void disactivePostTreader(String name);

	public void cleanRecognition(Set<String> recognitionSet);

}
