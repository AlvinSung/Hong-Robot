package com.founder.chatRobot.knowledgeMap.init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.founder.chatRobot.common.exception.DoubleInsertException;
import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.context.fact.InternalFactType;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.manager.common.ContextManager;
import com.founder.chatRobot.knowledgeMap.conception.entity.InternalEntity;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.init.common.Loader;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;
import com.founder.chatRobot.recognition.comparisionMethod.ComparisionRecognition;

public class NationLoader extends Loader {
	BufferedReader file;

	public NationLoader(String fileName, KnowledgeManager knowledgeManager,
			ComparisionRecognition recognition, ContextManager contextManager) {
		super(fileName, knowledgeManager, recognition, contextManager);
		try {
			file = new BufferedReader(new InputStreamReader(is, "GBK"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deal() {
		try {
			Entity nation = new InternalEntity(false);
			nation.setName("Nation");
			FactType nationType = new InternalFactType("Nation", "民族", true,
					nation);

			this.contextManager.addFactType(nationType);

			this.knowledgeManager.addUniqueIndex("Nation", nation);

			String line;
			while ((line = this.file.readLine()) != null) {
				line = line.trim();

				Entity entity = new InternalEntity(false);
				entity.setName(line);
				entity.setMainExpress(line);
				entity.setParent(nation);
			}

		} catch (DoubleInsertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeUnmatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
