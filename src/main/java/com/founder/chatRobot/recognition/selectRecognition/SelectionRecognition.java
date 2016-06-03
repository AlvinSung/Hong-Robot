package com.founder.chatRobot.recognition.selectRecognition;

import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.recognition.common.interfaces.old.Recognition;

public class SelectionRecognition implements Recognition {
	String[] selectionItemArray = { "10", "1", "2", "3", "4", "5", "6", "7",
			"8", "9", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十" };

	@Override
	public ConditionBean recogn(String sentence, String sessionId,
			String userId, String position) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			TypeUnmatchException {
		ConditionBean condition = new ConditionBean(sentence);

		for (String item : selectionItemArray) {
			int pos;
			if ((pos = sentence.indexOf(item)) >= 0) {
				Integer select = null;
				if (item.equals("1") || item.equals("2") || item.equals("3")|| item.equals("4")
						|| item.equals("5")|| item.equals("6")|| item.equals("7")|| item.equals("8")
						|| item.equals("9") || item.equals("10"))
					select = new Integer(item);
				else if (item.equals("一"))
					select = new Integer(1);
				else if (item.equals("二"))
					select = new Integer(2);
				else if (item.equals("三"))
					select = new Integer(3);
				else if (item.equals("四"))
					select = new Integer(4);
				else if (item.equals("五"))
					select = new Integer(5);
				else if (item.equals("六"))
					select = new Integer(6);
				else if (item.equals("七"))
					select = new Integer(7);
				else if (item.equals( "八"))
					select = new Integer(8);
				else if (item.equals("九"))
					select = new Integer(9);
				else if (item.equals("十"))
					select = new Integer(10);
				else
					select = new Integer(0);
				FactInfo<Integer> info = new FactInfo<Integer>(select);
				condition.addFact(new EntryInfo<Integer>("SelectItem", info,
						pos, item.length()));
				break;
			}
		}

		return condition;
	}

}
