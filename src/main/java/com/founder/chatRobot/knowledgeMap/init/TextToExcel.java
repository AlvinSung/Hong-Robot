package com.founder.chatRobot.knowledgeMap.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.founder.chatRobot.constants.ConfigConstants;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class TextToExcel {

	private void treadDirection(File currentDirection, WritableWorkbook book,
			int sheetNumber) {
		WritableSheet sheet = book.createSheet(currentDirection.getName(),
				sheetNumber);
		int maxColumNumber = 0, rowNumber = 1, columnNumber;
		Map<String, Integer> attributeMap = new HashMap<String, Integer>();

		/*System.out.println(currentDirection.getName());
		System.out.println(currentDirection.getAbsolutePath());*/

		for (String nextLevel : currentDirection.list()) {
			// int columnNumber = 0;
			BufferedReader br = null;
			try {
				br = new BufferedReader(
						new InputStreamReader(new FileInputStream(
								currentDirection + "\\" + nextLevel)));
				String treadCell = "";
				String line;
				while ((line = br.readLine()) != null) {
					if (!line.equals("")) {
						if (line.endsWith("<p class=\"article\">")) {
							treadCell += line.replaceAll(
									"<p class=\"article\">", "");
						} else {
							treadCell += line;
							treadCell = treadCell.trim();
							/*
							 * System.out.println(treadCell);
							 * System.out.println(treadCell.indexOf("$"));
							 */
							if (treadCell.contains("$")
									&& !treadCell.equals("$ ")) {
								String attribute = treadCell.substring(0,
										treadCell.indexOf("$"));
								if (attributeMap.containsKey(attribute)) {
									columnNumber = attributeMap.get(attribute);
								} else {
									columnNumber = maxColumNumber;
									attributeMap.put(attribute, maxColumNumber);
									maxColumNumber++;
									Label label = new Label(columnNumber, 0,
											attribute);
									sheet.addCell(label);
								}

								String tmp = treadCell.substring(treadCell
										.indexOf("$") + 1);
								//System.out.println(tmp);
								String[] tmpArray = tmp
										.split("(([<][^<>]{1,}[>])[ ]*){1,}");
								// tmp =
								// tmp.replaceAll("(([<][^<>]{1,}[>])[ ]*){1,}",
								// "\n");

								tmp = "";
								for (String temp : tmpArray) {
									temp = temp.trim();
									if (temp.length() > 0) {
										tmp += temp + "\n";
									}
								}

								if (tmp.length() > 0)
									tmp = tmp.substring(0, tmp.length() - 1);

								//System.out.println(tmp);

								Label label = new Label(columnNumber,
										rowNumber, tmp);
								sheet.addCell(label);

								/*
								 * if (lineNumber == 1) {
								 * 
								 * tmp = treadCell.substring(0,
								 * treadCell.indexOf("$")); label = new
								 * Label(rowNumber, 0, tmp);
								 * sheet.addCell(label); }
								 */

								columnNumber++;
							}
							treadCell = "";
						}
					}
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RowsExceededException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (null != br) {
					try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			rowNumber++;
		}
	}

	public void deal(String textFileDirection, String excelFileName) {
		WritableWorkbook book = null;
		File sourceFileDirection;
		int count = 0;

		sourceFileDirection = new File(textFileDirection);
		//System.out.println(sourceFileDirection.getName());
		try {
			book = Workbook.createWorkbook(new File(excelFileName));
			for (String nextDirection : sourceFileDirection.list()) {
				this.treadDirection(
						new File(sourceFileDirection.getAbsolutePath() + "\\"
								+ nextDirection), book, count);
				count++;
			}
			book.write();
			book.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TextToExcel transform = new TextToExcel();

		transform.deal(ConfigConstants.META_DATA_PATH + File.separator
				+ "product", ConfigConstants.META_DATA_PATH + File.separator
				+ "product.xls");
	}
}
