package com.founder.chatRobot.recognition.nameRecognition;

import java.io.IOException;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.recognition.common.interfaces.old.Recognition;

public class NameRecognition implements Recognition {

	@Override
	public ConditionBean recogn(String sentence, String sessionId,
			String userId, String position) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			TypeUnmatchException {
		ConditionBean result = new ConditionBean(sentence);
		int currentPos = 0;
		
		result.setPosition(position);
		result.setSessionId(sessionId);
		result.setUserId(userId);
		
		List<Term> terms = ToAnalysis.parse(sentence);
	    new NatureRecognition(terms).recognition() ;
	    
	    for(Term term : terms){
	    	if(term.getNatrue().natureStr.equals("nr")
	    			|| term.getNatrue().natureStr.equals("nr2")
	    			|| term.getNatrue().natureStr.equals("nrj")
	    			|| term.getNatrue().natureStr.equals("nrf")){
	    		FactInfo<String> info = new FactInfo<String>(term.getName());
				result.addFact(new EntryInfo<String>("FullName", info, currentPos,
						term.getName().length()));
	    	}
	    	currentPos += term.getName().length();
	    }
	    
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		 List<Term> terms = ToAnalysis.parse("张良瑞");
		    new NatureRecognition(terms).recognition() ;
		    System.out.println(terms.get(0).getNatrue().natureStr);
		    System.out.println(terms);

	}

}
