/**
 * 
 */
package com.founder.chatRobot.entityFinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.founder.chatRobot.constants.ConfigConstants;

/**
 * @author zyc_x_000
 *
 */
public class EntityFinderByName {
	private List<String> getWords(String line, int size) {
		List<String> result = new ArrayList<String>(line.length());
		String tmp = "", current = "";
		boolean isInTmp = false;
		int length = 0;
		int startPos = 0;
		int tmpStart = -1;

		// System.out.println(line);

		for (int i = 0; i < line.length(); i++) {
			char ch = line.charAt(i);
			// System.out.println(i);
			// System.out.println(line.charAt(i));
			if ((ch >= '0' && ch < '9') || (ch >= 'a' && ch < 'z')
					|| (ch >= 'A' && ch < 'Z')) {
				if (isInTmp) {
					tmp += ch;
				} else {
					tmp = "" + ch;
					isInTmp = true;
					tmpStart = i;
				}
			} else {
				if (ch == ' ') {
					if (isInTmp) {
						isInTmp = false;
						// tmp = "";
					}
					current = "";
					length = 0;
					startPos = i + 1;
				} else if (isInTmp) {
					current += tmp;
					isInTmp = false;
					// System.out.println(tmp);
					// System.out.println(tmp.length());
					if (length + 1 == size) {
						// System.out.println("1 : " + current);
						result.add(current);
						length = 0;
						current = "";
						if (tmpStart == startPos) {
							i = startPos + tmp.length() - 1;
							startPos = startPos + tmp.length();
						} else {
							i = startPos;
							startPos++;
						}
					} else if (length + 2 == size) {
						current += ch;
						// System.out.println("2 : " + current);
						result.add(current);
						length = 0;
						current = "";
						if (tmpStart == startPos) {
							i = startPos + tmp.length() - 1;
							startPos = startPos + tmp.length();
						} else {
							i = startPos;
							startPos++;
						}
					} else {
						length += 2;
						current += ch;
					}
				} else {
					current += ch;
					length++;
					if (length == size) {
						// System.out.println("3 : " + current);
						result.add(current);
						current = "";
						length = 0;
						// System.out.println(startPos);
						// System.out.println(tmpStart);
						if (tmpStart == startPos) {
							i = startPos + tmp.length() - 1;
							startPos = startPos + tmp.length();
						} else {
							i = startPos;
							startPos++;
						}
					} else {

					}
				}
			}
		}

		return result;
	}

	public List<String> find(List<String> names, int maxLength) {
		List<String> result = new LinkedList<String>();
		Map<Integer, Map<String, Integer>> partCount = new HashMap<Integer, Map<String, Integer>>();
		// String part;
		int count;
		List<String> splitLine;

		for (int i = 2; i <= maxLength; i++) {
			partCount.put(i, new HashMap<String, Integer>());
		}

		for (String name : names) {
			for (int i = 2; i <= maxLength; i++) {
				// System.out.println(name);
				splitLine = this.getWords(name, i);
				// System.out.println(splitLine.size());
				if (!splitLine.isEmpty()) {
					for (String part : splitLine) {
						// System.out.println(part);
						// part = name.substring(j, j + i);
						if (partCount.get(i).containsKey(part)) {
							count = partCount.get(i).get(part);
							// System.out.println(part + " : " + count);
						} else {
							count = 0;
						}
						partCount.get(i).put(part, count + 1);
					}
				}
			}
		}

		for (int i = 2; i <= maxLength; i++) {
			// System.out.println(partCount.get(i).size());
			Map<String, Integer> current = partCount.get(i);
			Map<String, Integer> tmp = new HashMap<String, Integer>(current);
			for (String candidate : tmp.keySet()) {
				if (current.get(candidate) < 5) {
					current.remove(candidate);
				} else {
					for (int j = i + 1; j <= maxLength; j++) {
						Map<String, Integer> compare = partCount.get(j);
						for (String comparePart : compare.keySet()) {
							/*
							 * if (comparePart.contains(candidate)) {
							 * System.out.println(comparePart + " : " +
							 * compare.get(comparePart));
							 * System.out.println(candidate + " : " +
							 * current.get(candidate)); System.out
							 * .println(compare.get(comparePart) >= current
							 * .get(candidate)); }
							 */
							if (comparePart.contains(candidate)
									&& compare.get(comparePart) >= current
											.get(candidate)) {
								current.remove(current);
							}
						}
					}
				}
			}
		}

		for (int i = 2; i <= maxLength; i++) {
			// System.out.println(partCount.get(i).size());
			result.addAll(partCount.get(i).keySet());
			/*
			 * for (String key : partCount.get(i).keySet()) {
			 * System.out.println("aaa : " + key + " : " +
			 * partCount.get(i).get(key)); }
			 */
		}

		return result;
	}

	public List<String> findFromFile(String fileName) {
		List<String> result = null;
		try {
			File file = new File(fileName);
			List<String> names = new LinkedList<String>();
			Workbook book = null;

			book = Workbook.getWorkbook(file);
			Sheet sheet = book.getSheet(0);

			for (int i = 0; i < sheet.getRows(); i++) {
				String fullName, companyName;

				fullName = sheet.getCell(2, i).getContents();
				companyName = sheet.getCell(3, i).getContents();

				companyName = companyName.replaceAll("基金", "");
				fullName = fullName.replaceAll("基金", " ");
				fullName = fullName.replaceAll(companyName, "");
				fullName = fullName.replaceAll("货币市场", " ");
				fullName = fullName.replaceAll("债券型", " ");
				fullName = fullName.replaceAll("指数", " ");
				fullName = fullName.replaceAll("票据", " ");

				fullName = fullName.replace('(', ' ');
				fullName = fullName.replace(')', ' ');
				fullName = fullName.replace('（', ' ');
				fullName = fullName.replace('）', ' ');

				String[] fullNames = fullName.split(" ");

				for (String s : fullNames) {
					names.add(s);
				}
			}

			/*
			 * while ((line = br.readLine()) != null) { line = line.replace('(',
			 * ' '); line = line.replace(')', ' '); line = line.replace('（',
			 * ' '); line = line.replace('）', ' '); names.add(line); }
			 */

			result = this.find(names, 6);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = ConfigConstants.META_DATA_PATH + File.separator
				+ "entityFinder.xls";
		EntityFinderByName finder = new EntityFinderByName();

		List<String> characters = finder.findFromFile(fileName);

		for (String character : characters) {
			System.out.println(character);
		}

		// System.out.println("深证300".contains("证300"));

	}
}
