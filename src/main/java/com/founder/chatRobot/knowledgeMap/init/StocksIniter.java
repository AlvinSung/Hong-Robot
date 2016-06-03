package com.founder.chatRobot.knowledgeMap.init;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.founder.chatRobot.common.exception.DoubleInsertException;
import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.context.fact.InternalFactType;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.manager.common.ContextManager;
import com.founder.chatRobot.knowledgeMap.conception.entity.InternalEntity;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;
import com.founder.chatRobot.recognition.comparisionMethod.ComparisionRecognition;
import com.founder.chatRobot.recognition.comparisionMethod.entryInfo.SemanticEntryInfo;

public class StocksIniter {
	private BufferedReader file;
	private KnowledgeManager knowledgeManager;
	private ComparisionRecognition recognition;
	private ContextManager contextManager;

	// private Map<String, Entity> positionMap;

	public StocksIniter(String fileName, KnowledgeManager knowledgeManager,
			ComparisionRecognition recognition, ContextManager contextManager) {
		try {
			InputStream is = Thread.currentThread().getClass()
					.getResourceAsStream("/" + fileName);

			if (is == null) {
				is = PositionIniter.class.getResourceAsStream("/" + fileName);
			}

			if (is == null) {
				is = new FileInputStream(fileName);
			}
			file = new BufferedReader(new InputStreamReader(is, "GBK"));
			/*
			 * file = new BufferedReader(new InputStreamReader( new
			 * FileInputStream(fileName), "GBK"));
			 */
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.knowledgeManager = knowledgeManager;
		this.contextManager = contextManager;
		this.recognition = recognition;
		// this.positionMap = positionMap;
	}

	private void addToRecognition(Entity entity, String typeName,
			String... marks) {
		FactInfo<Entity> factInfo = new FactInfo<Entity>(entity);
		EntryInfo<Entity> entryinfo = new EntryInfo<Entity>(typeName, factInfo);
		// System.out.println(entryinfo.hashCode());
		SemanticEntryInfo<Entity> sem = new SemanticEntryInfo<Entity>(entryinfo);
		for (String mark : marks) {
			sem.addMark(mark);
		}
		this.recognition.addOverallSemanticEntry(entryinfo, sem);
	}

	public void deal() {

		try {
			String line;
			Entity stockCode = new InternalEntity(false);
			stockCode.setName("StockCode");
			stockCode.setMainExpress("股票代码");
			FactType stockCodefactType = new InternalFactType("StockCode",
					"股票代码", true, stockCode);
			this.contextManager.addFactType(stockCodefactType);
			Entity stockName = new InternalEntity(false);
			stockName.setName("StockName");
			stockName.setMainExpress("股票名称");
			FactType stockNameFactType = new InternalFactType("StockName",
					"股票名称", true, stockName);
			this.contextManager.addFactType(stockNameFactType);

			Entity stockType = new InternalEntity(false);
			stockType.setName("Stock");
			stockType.setMainExpress("股票");
			FactType stockFactType = new InternalFactType("Stock", "股票", true,
					stockType);
			this.contextManager.addFactType(stockFactType);
			this.knowledgeManager.addUniqueIndex("股票", stockType);
			this.addToRecognition(stockType, "__ATTRIBUTE__Stock", "股票");

			while ((line = this.file.readLine()) != null) {
				if (line != null && !line.equals("")) {
					String[] stocks = line.split(" ");

					for (String stock : stocks) {
						stock = stock.trim();
						if (stock != null && !stock.equals("")) {
							String[] subs = stock.split("\\(");
							Entity stockEntity = new InternalEntity(false);
							stockEntity.setName(subs[0] + "("
									+ subs[1].replace(")", "") + ")");
							stockEntity.setMainExpress(subs[0]);
							stockEntity.setValue(subs[1].replace(
									")", ""));
							stockEntity.setParent(stockType);

							/*
							 * System.out.println(subs[0]);
							 * System.out.println(subs[1].replace(")", ""));
							 */

							Entity stockNameEntity = new InternalEntity(false);
							stockNameEntity.setName(subs[0]);
							stockNameEntity.setMainExpress(subs[0]);
							stockEntity.addProductToAttribute(stockType);
							stockEntity.addProductToValue(stockNameEntity);
							stockType.addAttributeToProduct(stockEntity);
							stockType.addAttributeToValue(stockNameEntity);
							stockNameEntity.addValueToAttribute(stockType);
							stockNameEntity.addValueToProduct(stockEntity);
							this.addToRecognition(stockNameEntity,
									stockFactType.getTypeName(), subs[0]);

							// System.out.println(subs[0]);

							Entity stockCodeEntity = new InternalEntity(false);
							stockCodeEntity.setName(subs[1].replace(")", ""));
							stockCodeEntity.setMainExpress(subs[1].replace(")",
									""));
							stockEntity.addProductToAttribute(stockCode);
							stockEntity.addProductToValue(stockCodeEntity);
							stockCode.addAttributeToProduct(stockEntity);
							stockCode.addAttributeToValue(stockCodeEntity);
							stockCodeEntity.addValueToAttribute(stockCode);
							stockCodeEntity.addValueToProduct(stockEntity);
							this.addToRecognition(stockCodeEntity,
									stockCodefactType.getTypeName(),
									subs[1].replace(")", ""));
						}
					}
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DoubleInsertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeUnmatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
