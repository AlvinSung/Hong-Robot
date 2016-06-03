package com.founder.chatRobot.knowledgeMap.init;

import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.founder.chatRobot.context.manager.common.ContextManager;
import com.founder.chatRobot.knowledgeMap.conception.entity.InternalEntity;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.init.common.Loader;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;
import com.founder.chatRobot.recognition.comparisionMethod.ComparisionRecognition;

public class WhatLoader extends Loader {
	public WhatLoader(String fileName, KnowledgeManager knowledgeManager,
			ComparisionRecognition recognition, ContextManager contextManager) {
		super(fileName, knowledgeManager, recognition, contextManager);
	}

	public void deal() {
		Workbook book = null;

		Entity descript = new InternalEntity(false);
		// fund.setMainExpress(new Express<Entity>("基金"));
		descript.setName("Descript");
		descript.setMainExpress("描述");
		this.addToRecognition(descript, "Descript", "描述");
		// this.addToMainRecognition(descript, "Descript", "描述");
		// this.addToRecognition(descript, "Descript", "是什么");
		this.addToRecognition(descript, "Descript", "什么是");
		this.addToRecognition(descript, "Descript", "解释");
		this.addToRecognition(descript, "Descript", "说明");

		try {
			book = Workbook.getWorkbook(this.is);
			Sheet sheet = book.getSheet(0);

			for (int i = 0; i < sheet.getRows(); i++) {
				// System.out.println(i);
				// System.out.println(sheet.getCell(0, i).getContents());
				// System.out.println(sheet.getCell(1, i).getContents());
				Cell name = sheet.getCell(0, i);
				Cell description = sheet.getCell(1, i);
				Cell other = sheet.getCell(2, i);

				String entityName = name.getContents().replaceAll("？", "")
						.replaceAll("什么是", "").trim();
				// System.out.println(entityName);

				Entity entity = (Entity) this.knowledgeManager
						.uniqueIndex(entityName);
				if (entity == null) {
					entity = new InternalEntity(false);
					entity.setMainExpress(entityName);
					this.addToRecognition(entity, "Entity", entityName);
				}

				String[] others = other.getContents().trim().split(" ");
				for (String s : others) {
					if (!s.trim().equals(""))
						this.addToRecognition(entity, "Entity", s.trim());
				}

				Entity descriptionEntity = new InternalEntity(false);
				descriptionEntity.setValue(description.getContents());

				entity.addProductToAttribute(descript);
				entity.addProductToValue(descriptionEntity);
				descript.addAttributeToProduct(entity);
				descript.addAttributeToValue(descriptionEntity);
				descriptionEntity.addValueToAttribute(descript);
				descriptionEntity.addValueToProduct(entity);
			}

		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
