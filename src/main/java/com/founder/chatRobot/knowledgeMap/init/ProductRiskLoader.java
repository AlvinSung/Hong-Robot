package com.founder.chatRobot.knowledgeMap.init;

import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.founder.chatRobot.common.exception.DoubleInsertException;
import com.founder.chatRobot.common.exception.TypeMissException;
import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.context.fact.InternalFactType;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.manager.common.ContextManager;
import com.founder.chatRobot.knowledgeMap.conception.common.ExpressConception;
import com.founder.chatRobot.knowledgeMap.conception.entity.InternalEntity;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.init.common.Loader;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;
import com.founder.chatRobot.recognition.comparisionMethod.ComparisionRecognition;
import com.founder.chatRobot.recognition.comparisionMethod.entryInfo.SemanticEntryInfo;

public class ProductRiskLoader extends Loader {

	public ProductRiskLoader(String fileName,
			KnowledgeManager knowledgeManager,
			ComparisionRecognition recognition, ContextManager contextManager) {
		super(fileName, knowledgeManager, recognition, contextManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void deal() {
		try {
			Entity riskLevel = new InternalEntity(false);
			riskLevel.setName("RiskLevel");
			riskLevel.setMainExpress("风险等级");

			FactType factType = new InternalFactType("RiskLevel", "风险等级", true,
					riskLevel);
			this.contextManager.addFactType(factType);
			this.contextManager.addTypeRelation("RiskLevel", "RiskLevel",
					"SaleTask");

			Entity lowRisk = new InternalEntity(false);
			lowRisk.setName("LowRiskLevel");
			lowRisk.setMainExpress("低风险");
			lowRisk.addValueToAttribute(riskLevel);
			riskLevel.addAttributeToValue(lowRisk);

			Entity middleRisk = new InternalEntity(false);
			middleRisk.setName("MiddleRiskLevel");
			middleRisk.setMainExpress("中风险");
			middleRisk.addValueToAttribute(riskLevel);
			riskLevel.addAttributeToValue(middleRisk);

			Entity highRisk = new InternalEntity(false);
			highRisk.setName("HighRiskLevel");
			highRisk.setMainExpress("高风险");
			highRisk.addValueToAttribute(riskLevel);
			riskLevel.addAttributeToValue(highRisk);

			FactInfo<ExpressConception> factInfo = new FactInfo<ExpressConception>(
					riskLevel);
			EntryInfo<ExpressConception> riskEntryinfo = new EntryInfo<ExpressConception>(
					"__ATTRIBUTE__" + factType.getTypeName(), factInfo);
			SemanticEntryInfo<ExpressConception> riskSem = new SemanticEntryInfo<ExpressConception>(
					riskEntryinfo);
			riskSem.addMark("风险等级");
			riskSem.addMark("风险");

			factInfo = new FactInfo<ExpressConception>(lowRisk);
			EntryInfo<ExpressConception> entryinfo = new EntryInfo<ExpressConception>(
					factType.getTypeName(), factInfo);
			SemanticEntryInfo<ExpressConception> sem = new SemanticEntryInfo<ExpressConception>(
					entryinfo);
			sem.addMark("低");
			sem.addMark("小");
			riskSem.addSubEntry(sem);

			factInfo = new FactInfo<ExpressConception>(middleRisk);
			entryinfo = new EntryInfo<ExpressConception>(
					factType.getTypeName(), factInfo);
			sem = new SemanticEntryInfo<ExpressConception>(entryinfo);
			sem.addMark("中");
			sem.addMark("合适");
			sem.addMark("适中");
			riskSem.addSubEntry(sem);

			factInfo = new FactInfo<ExpressConception>(highRisk);
			entryinfo = new EntryInfo<ExpressConception>(
					factType.getTypeName(), factInfo);
			sem = new SemanticEntryInfo<ExpressConception>(entryinfo);
			sem.addMark("高");
			sem.addMark("大");
			riskSem.addSubEntry(sem);

			this.recognition.addOverallSemanticEntry(riskEntryinfo, riskSem);

			Entity principalRisk = new InternalEntity(false);
			riskLevel.setName("PrincipalRisk");
			riskLevel.setMainExpress("本金风险");
			factType = new InternalFactType("PrincipalRisk", "本金风险", true,
					principalRisk);
			this.contextManager.addFactType(factType);
			this.contextManager.addTypeRelation("PrincipalRisk", "PrincipalRisk",
					"SaleTask");

			Entity preservation = new InternalEntity(false);
			preservation.setName("Preservation");
			preservation.setMainExpress("保本");
			this.addToRecognition(preservation, "PrincipalRisk", "保本");

			Entity riskType = new InternalEntity(false);
			riskType.setName("RiskType");
			riskType.setMainExpress("风险类型");
			factType = new InternalFactType("RiskType", "风险类型", true, riskType);
			this.contextManager.addFactType(factType);
			this.addToRecognition(riskType, "__ATTRIBUTE__RiskType", "风险类型");
			this.contextManager.addTypeRelation("RiskType", "RiskType",
					"SaleTask");

			Entity conservative = new InternalEntity(false);
			conservative.setName("Conservative");
			conservative.setMainExpress("保守型");
			conservative.addProductToAttribute(riskLevel);
			conservative.addProductToValue(lowRisk);
			riskLevel.addAttributeToProduct(conservative);
			lowRisk.addValueToProduct(conservative);
			conservative.addValueToAttribute(riskType);
			riskType.addAttributeToValue(conservative);
			this.addToRecognition(conservative, "RiskType", "保守型");
			this.addToRecognition(conservative, "RiskType", "保守");
			this.knowledgeManager.addUniqueIndex("Conservative", conservative);

			Entity steady = new InternalEntity(false);
			steady.setName("Steady");
			steady.setMainExpress("稳健型");
			steady.addProductToAttribute(riskLevel);
			steady.addProductToValue(middleRisk);
			riskLevel.addAttributeToProduct(steady);
			middleRisk.addValueToProduct(steady);
			steady.addValueToAttribute(riskType);
			riskType.addAttributeToValue(steady);
			this.addToRecognition(steady, "RiskType", "稳健型");
			this.addToRecognition(steady, "RiskType", "稳健");
			this.knowledgeManager.addUniqueIndex("Steady", steady);

			Entity aggressive = new InternalEntity(false);
			aggressive.setName("Aggressive");
			aggressive.setMainExpress("积极型");
			aggressive.addProductToAttribute(riskLevel);
			aggressive.addProductToValue(highRisk);
			riskLevel.addAttributeToProduct(aggressive);
			highRisk.addValueToProduct(aggressive);
			aggressive.addValueToAttribute(riskType);
			riskType.addAttributeToValue(aggressive);
			this.addToRecognition(aggressive, "RiskType", "积极型");
			this.addToRecognition(aggressive, "RiskType", "积极");
			this.knowledgeManager.addUniqueIndex("Aggressive", aggressive);

			Workbook book;
			book = Workbook.getWorkbook(is);
			Sheet sheet = book.getSheet(0);
			for (int i = 0; i < sheet.getRows(); i++) {
				Cell cell = sheet.getCell(1, i);
				Cell rCell = sheet.getCell(2, i);

				Entity entity = (Entity) knowledgeManager.uniqueIndex(cell
						.getContents().trim());

				if (entity != null) {
					Entity product = entity.getValueToProduct().iterator()
							.next();
					product.addProductToAttribute(riskType);
					riskType.addAttributeToProduct(product);

					if (rCell.getContents().trim().equals("稳健型")) {
						product.addProductToValue(steady);
						steady.addValueToProduct(product);
					} else if (rCell.getContents().trim().equals("保守型")) {
						product.addProductToValue(conservative);
						conservative.addValueToProduct(product);
					} else if (rCell.getContents().trim().equals("积极型")) {
						product.addProductToValue(aggressive);
						aggressive.addValueToProduct(product);
					}
				}
			}
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DoubleInsertException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TypeUnmatchException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TypeMissException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
