package com.founder.chatRobot.knowledgeMap.init.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.context.manager.common.ContextManager;
import com.founder.chatRobot.knowledgeMap.conception.common.ExpressConception;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;
import com.founder.chatRobot.recognition.comparisionMethod.ComparisionRecognition;
import com.founder.chatRobot.recognition.comparisionMethod.entryInfo.SemanticEntryInfo;

public abstract class Loader {
	protected KnowledgeManager knowledgeManager;
	protected ComparisionRecognition recognition;
	protected ContextManager contextManager;
	protected InputStream is;

	public Loader(String fileName, KnowledgeManager knowledgeManager,
			ComparisionRecognition recognition, ContextManager contextManager) {
		// file = new File(fileName);
		this.knowledgeManager = knowledgeManager;
		this.contextManager = contextManager;
		this.recognition = recognition;
		if(fileName != null){
			try {
				is = Thread.currentThread().getClass()
						.getResourceAsStream("/" + fileName);
				if (is == null) {
					//is = new FileInputStream(fileName);
					is = Loader.class.getResourceAsStream("/" + fileName);
				}
				if (is == null) {
					is = new FileInputStream(fileName);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void addToRecognition(ExpressConception entity, String typeName,
			String mark) {
		FactInfo<ExpressConception> factInfo = new FactInfo<ExpressConception>(
				entity);
		EntryInfo<ExpressConception> entryinfo = new EntryInfo<ExpressConception>(
				typeName, factInfo);
		SemanticEntryInfo<ExpressConception> sem = new SemanticEntryInfo<ExpressConception>(
				entryinfo);
		sem.addMark(mark);
		this.recognition.addOverallSemanticEntry(entryinfo, sem);

		this.knowledgeManager.addUniqueIndex(mark, entity);
	}

	public abstract void deal();
}
