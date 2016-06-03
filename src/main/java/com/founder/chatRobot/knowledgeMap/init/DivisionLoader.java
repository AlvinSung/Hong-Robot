package com.founder.chatRobot.knowledgeMap.init;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import com.founder.chatRobot.common.exception.DoubleInsertException;
import com.founder.chatRobot.common.exception.TypeMissException;
import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.context.fact.InternalFactType;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.manager.common.ContextManager;
import com.founder.chatRobot.knowledgeMap.conception.common.Conception;
import com.founder.chatRobot.knowledgeMap.conception.entity.InternalEntity;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.init.common.Loader;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;
import com.founder.chatRobot.recognition.comparisionMethod.ComparisionRecognition;
import com.founder.chatRobot.recognition.comparisionMethod.entryInfo.SemanticEntryInfo;

public class DivisionLoader extends Loader {
	BufferedReader file;

	public DivisionLoader(String fileName, KnowledgeManager knowledgeManager,
			ComparisionRecognition recognition, ContextManager contextManager) {
		super(fileName, knowledgeManager, recognition, contextManager);
		try {
			file = new BufferedReader(new InputStreamReader(is, "GBK"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void deal() {
		try {
			Entity mainPos = new InternalEntity(false);
			mainPos.setName("Division");
			FactType divisionType = new InternalFactType("Division", "区划",
					true, mainPos);
			this.contextManager.addFactType(divisionType);
			this.knowledgeManager.addUniqueIndex("Division", mainPos);

			this.contextManager.addTypeRelation("position", "Division",
					"CreateAccount");

			Entity province = new InternalEntity(false);
			province.setName("Province");
			province.setMainExpress("省");
			province.setParent(mainPos);
			this.addToRecognition(province, "Division", "省");

			Entity city = new InternalEntity(false);
			city.setName("City");
			city.setMainExpress("市");
			city.setParent(mainPos);
			this.addToRecognition(city, "Division", "市");

			Entity county = new InternalEntity(false);
			county.setName("County");
			county.setMainExpress("县");
			county.setParent(mainPos);
			this.addToRecognition(city, "Division", "县");

			Entity situated = new InternalEntity(false);
			situated.setName("Situated");
			situated.setMainExpress("所在位置");
			// situated.setParent(mainPos);
			this.knowledgeManager.addUniqueIndex("Situated", situated);

			Entity nation = (Entity) this.knowledgeManager
					.uniqueIndex("Nation");
			Set<String> nationNames = new HashSet<String>(56);
			for (Conception entity : nation.getChildren()) {
				nationNames.add(entity.getName());
			}

			Entity lastProvince = null, lastCity = null, taiwan = null, honkong = null;
			String line;
			while ((line = this.file.readLine()) != null) {
				line = line.trim();
				// System.out.println(line);
				if (line != null && !line.equals("")) {
					// System.out.println(line);
					line = line.replaceAll("[ ]+", ":");
					// System.out.println(line);
					String[] parts = line.split(":");
					// System.out.println(parts[0] + " : " + parts[1]);
					if (!parts[1].equals("市辖区") && !parts[1].equals("县")
							&& !parts[1].equals("自治区直辖县级行政区划")) {
						// System.out.println(parts[0]);
						// System.out.println(parts[1]);
						Entity position = new InternalEntity(false);
						// System.out.println(parts[0]);
						// System.out.println(parts[1]);
						position.setValue(parts[0]);
						//position.setName(parts[1]);

						position.setMainExpress(parts[1]);

						this.addToRecognition(position, "Division", parts[1]);

						if (parts[0].endsWith("0000")) {
							position.setParent(province);
							lastProvince = position;
							lastCity = null;
							String tmp = parts[1];
							if (!(tmp = parts[1].replace("省", ""))
									.equals(parts[1])) {
								this.addToRecognition(position, "Division", tmp);
								if (tmp.equals("台湾")) {
									taiwan = position;
								}
								// System.out.println(0);
							} else if (!(tmp = parts[1].replace("市", ""))
									.equals(parts[1])) {
								this.addToRecognition(position, "Division", tmp);
								// this.knowledgeManager.addUniqueIndex(tmp,
								// entity)
								// System.out.println(1);
							} else if (!(tmp = parts[1].replace("壮族自治区", ""))
									.equals(parts[1])) {
								this.addToRecognition(position, "Division", tmp);
								// System.out.println(2);
							} else if (!(tmp = parts[1].replace("回族自治区", ""))
									.equals(parts[1])) {
								this.addToRecognition(position, "Division", tmp);
								// System.out.println(3);
							} else if (!(tmp = parts[1].replace("维吾尔自治区", ""))
									.equals(parts[1])) {
								this.addToRecognition(position, "Division", tmp);
								// System.out.println(4);
							} else if (!(tmp = parts[1].replace("自治区", ""))
									.equals(parts[1])) {
								this.addToRecognition(position, "Division", tmp);
								// System.out.println(5);
							} else if (!(tmp = parts[1].replace("特别行政区", ""))
									.equals(parts[1])) {
								this.addToRecognition(position, "Division", tmp);
								if (tmp.equals("香港")) {
									honkong = position;
								}
								// System.out.println(5);
							} else {
								tmp = parts[1];
								// System.out.println(6);
							}
							
							position.setName(tmp);
							// System.out.println(tmp);
							this.knowledgeManager.addUniqueIndex(tmp, position);

							// System.out.println(lastProvince);
						} else if (parts[0].endsWith("00")) {
							// System.out.println(position.getName());
							String tmp, posString = parts[1];
							if (!(tmp = parts[1].replace("市", ""))
									.equals(parts[1])) {
								posString = tmp;
								this.addToRecognition(position, "Division", tmp);
								// System.out.println(0);
							} else if (!(tmp = parts[1].replace("地区", ""))
									.equals(parts[1])) {
								posString = tmp;
								this.addToRecognition(position, "Division", tmp);
								// System.out.println(0);
							} else if (!(tmp = parts[1].replace("自治州", ""))
									.equals(parts[1])) {
								posString = tmp;
								this.addToRecognition(position, "Division", tmp);
								// System.out.println(0);
							} else if (!(tmp = parts[1].replace("州", ""))
									.equals(parts[1])) {
								posString = tmp;
								this.addToRecognition(position, "Division", tmp);
								// System.out.println(0);
							}

							String tmpString = posString;
							for (String name : nationNames) {
								if (!(tmp = tmpString.replace(name, ""))
										.equals(tmpString)) {
									tmpString = tmp;
								}
							}
							if (!tmpString.equals(posString)
									&& tmpString.length() > 0) {
								this.addToRecognition(position, "Division",
										tmpString);
								position.setName(tmpString);
								//System.out.println(tmpString);
							}else
								position.setName(posString);
							
							

							position.setParent(city);
							lastCity = position;
							position.addProductToAttribute(situated);
							position.addProductToValue(lastProvince);
							situated.addAttributeToProduct(position);
							situated.addAttributeToValue(lastProvince);
							lastProvince.addValueToAttribute(situated);
							lastProvince.addValueToProduct(position);
						} else {
							String tmp, posString = parts[1];
							if (!(tmp = parts[1].replace("市", ""))
									.equals(parts[1])) {
								posString = tmp;
								this.addToRecognition(position, "Division", tmp);
								// System.out.println(tmp);
							} else if (!(tmp = parts[1].replace("自治县", ""))
									.equals(parts[1])) {
								posString = tmp;
								this.addToRecognition(position, "Division", tmp);
								// System.out.println(0);
							} else if (!(tmp = parts[1].replace("新区", ""))
									.equals(parts[1])
									&& parts[1].replace("新区", "").length() > 1) {
								posString = tmp;
								this.addToRecognition(position, "Division", tmp);
								this.addToRecognition(position, "Division", tmp
										+ "新");
								// System.out.println(0);
							} else if (!(tmp = parts[1].replace("区", ""))
									.equals(parts[1])
									&& parts[1].replace("区", "").length() > 1) {
								posString = tmp;
								this.addToRecognition(position, "Division", tmp);
								// System.out.println(0);
							} else if (!(tmp = parts[1].replace("县", ""))
									.equals(parts[1]) && tmp.length() > 1) {
								posString = tmp;
								this.addToRecognition(position, "Division", tmp);
								// System.out.println(0);
							} else if (!(tmp = parts[1].replace("矿区", ""))
									.equals(parts[1])) {
								posString = tmp;
								this.addToRecognition(position, "Division", tmp);
								// System.out.println(0);
							}

							String tmpString = posString;
							for (String name : nationNames) {
								if (!(tmp = tmpString.replace(name, ""))
										.equals(tmpString)) {
									tmpString = tmp;
								}
							}
							if (!tmpString.equals(posString)
									&& tmpString.length() > 0) {
								position.setName(tmpString);
								this.addToRecognition(position, "Division",
										tmpString);
								//System.out.println(tmpString);
							}else
								position.setName(posString);

							if (lastCity != null) {
								position.setParent(county);
								position.addProductToAttribute(situated);
								position.addProductToValue(lastCity);
								situated.addAttributeToProduct(position);
								situated.addAttributeToValue(lastCity);
								lastCity.addValueToAttribute(situated);
								lastCity.addValueToProduct(position);
							} else {
								// System.out.println(position.getName());
								position.setParent(city);
								position.addProductToAttribute(situated);
								position.addProductToValue(lastProvince);
								situated.addAttributeToProduct(position);
								situated.addAttributeToValue(lastProvince);
								lastProvince.addValueToAttribute(situated);
								lastProvince.addValueToProduct(position);
							}
						}

						this.knowledgeManager
								.addUniqueIndex(parts[1], position);
					}
				}
			}

			FactInfo<Entity> factInfo = new FactInfo<Entity>(taiwan, honkong);
			EntryInfo<Entity> entryinfo = new EntryInfo<Entity>("Division",
					factInfo);
			SemanticEntryInfo<Entity> sem = new SemanticEntryInfo<Entity>(
					entryinfo);
			sem.addMark("港台");
			this.recognition.addOverallSemanticEntry(entryinfo, sem);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DoubleInsertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeUnmatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeMissException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != file) {
				try {
					file.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
