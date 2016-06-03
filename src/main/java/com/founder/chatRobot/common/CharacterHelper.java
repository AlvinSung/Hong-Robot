package com.founder.chatRobot.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符集识别辅助工具类
 */
public class CharacterHelper {

	private static final String FULL_CHARS = "～！＠＃％＾＆×（）｀　｛｝［］【】：＂｜；＇＼＜＞？，．／。《》“”‘’＋－＝—．、￥";
	private static final String HALF_CHARS = "~!@#%^&*()` {}[]:\"|;'\\<>?,./.<>\"\"''+-=_.$";

	/**
	 * 是否包含英文字母或数字
	 * 
	 * @param text
	 * @return
	 */
	public static boolean containsNumberOrEnglish(String text) {
		if (text == null || text.length() == 0) {
			return false;
		}
		for (char ch : text.toCharArray()) {
			if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
				return true;
		}
		return false;
	}

	/**
	 * 大写转小写
	 * 
	 * @param text
	 * @return
	 */
	public static String convertToLower(String text) {
		if (text == null || text.length() == 0)
			return text;
		char[] array = text.toCharArray();
		for (int i = 0; i < array.length; i++) {
			if (array[i] >= 'A' && array[i] <= 'Z')
				array[i] -= 'A' - 'a';

		}

		return String.valueOf(array);
	}

	/**
	 * 按标点符号分句
	 * 
	 * @param text
	 * @return
	 */
	public static String[] splitSentence(String text) {
		if (text == null || text.length() == 0)
			return null;
		List<String> senList = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for (char c : text.toCharArray()) {
			if (FULL_CHARS.contains(String.valueOf(c)) || HALF_CHARS.contains(String.valueOf(c))) {
				if (sb.toString().trim().length() != 0) {
					senList.add(sb.toString());
					sb = new StringBuilder();
				}
			} else {
				sb.append(c);
			}
		}
		if (sb.toString().length() > 0)
			senList.add(sb.toString());

		return senList.toArray(new String[0]);
	}

	/**
	 * 从text中去除标点符号
	 * 
	 * @param text
	 * @return
	 */
	public static String removePunctuation(String text) {
		StringBuilder sb = new StringBuilder();
		for (char c : text.toCharArray()) {
			if (c != ' ' && !isPunctuation(c)) {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * 判断输入的字符串是否包含上述符号字符
	 * 
	 * @param text
	 * @return
	 */
	public static boolean containsChar(String text) {
		if (null == text)
			return false;
		for (char c : (FULL_CHARS + HALF_CHARS).toCharArray()) {
			if (text.indexOf(c) != -1)
				return true;
		}
		return false;
	}

	/**
	 * 判断字符是否标点符号
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isPunctuation(char input) {
		for (char c : (FULL_CHARS + HALF_CHARS).toCharArray()) {
			if (c == input)
				return true;
		}

		return false;
	}

	public static boolean isEnglishLetter(char input) {
		return (input >= 'a' && input <= 'z') || (input >= 'A' && input <= 'Z');
	}

	public static boolean isArabicNumber(char input) {
		return input >= '0' && input <= '9';
	}

	public static boolean isCJKCharacter(char input) {
		return Character.UnicodeBlock.of(input) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;
	}

	// 保留cjk字符集，ascii字符集
	public static String Regularize(String input) {
		char[] chararr = input.toCharArray();
		int j = 0;
		int i = 0;
		for (i = 0; i < chararr.length; i++) {
			char cTmpCh = Standardization(chararr[i]);
			boolean bIsCJK = isCJKCharacter(cTmpCh);
			if (bIsCJK == true || (cTmpCh <= 127 && cTmpCh >= 0)) {
				chararr[j++] = cTmpCh;
			} else {// 无法识别的字符替换为空格，再交由分词系统处理
				chararr[j++] = ' ';
			}
		}
		String sRet = "";
		if (j > 0) {
			sRet = String.valueOf(chararr);
		}
		return sRet;
	}

	/**
	 * 进行字符规格化（全角转半角，大写转小写处理）
	 * 
	 * @param input
	 * @return char
	 */
	public static char Standardization(char input) {
		if (input == 12288) {
			input = (char) 32;

		} else if (input > 65280 && input < 65375) {
			input = (char) (input - 65248);
		}
		return input;
	}

	public static void main(String[] args) {
		String[] sentences = splitSentence("我买了一个耳机,赠品是20元电子东券,我在哪里查看赠品啊 怎样使用这个券");
		for (String sentence : sentences)
			System.out.println(sentence);
	}
}