package com.founder.chatRobot.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.founder.chatRobot.domain.Constants;

/**
 * 通用函数
 * 
 * @author cdluohuan
 * 
 */
public class GeneralApi {

	static String signalModalParticles = "喔、哦、吧、罢、呗、啵、的、价、家、啦、来、唻、了、嘞、哩、咧、咯、啰、喽、吗、嘛、嚜、么、麽、哪、呢、呐、否、呵、哈、不、兮、般、则、连、罗、给、噻、哉、阿、啊、呃、欸、哇、呀、也、耶、哟、欤、呕、噢、呦、嘢";
	static String removeModalParticles = "罢、价、家、来、哪、否、不、般、则、连、罗、给、么、了、的";
	static {
		signalModalParticles = signalModalParticles.replaceAll("["
				+ removeModalParticles + "]", "");
		//LogHelper.PRESALES_LOG.warn("去掉的语气词：" + signalModalParticles);
	}

	public static String removeModalParticles(String sentenceString) {
		if (null == sentenceString) {
			return "";
		}

		return sentenceString.replaceAll("[" + signalModalParticles + "]", "");
	}

	public static String getModalParticles() {
		return signalModalParticles;
	}

	public static class OneValue<A> implements Serializable {
		private static final long serialVersionUID = -2733890305960675941L;
		private A a;

		public OneValue() {
		}

		public OneValue(A a) {
			this.a = a;
		}

		public A getA() {
			return a;
		}

		public void setA(A a) {
			this.a = a;
		}

		@Override
		public int hashCode() {
			return getA().hashCode();
		}

		@Override
		public boolean equals(Object object) {
			return (object instanceof OneValue)
					&& ((OneValue<?>) object).getA().equals(getA());
		}

		@Override
		public String toString() {
			return getA().toString();
		}
	}

	public static class TwoValue<A, B> extends OneValue<A> {
		private static final long serialVersionUID = 1063304218274521761L;
		private B b;

		public TwoValue() {
		}

		public TwoValue(A a, B b) {
			super(a);
			this.b = b;
		}

		public B getB() {
			return b;
		}

		public void setB(B b) {
			this.b = b;
		}

		@Override
		public boolean equals(Object object) {
			return (object instanceof TwoValue)
					&& ((TwoValue<?, ?>) object).getB().equals(getB())
					&& super.equals(object);
		}

		@Override
		public int hashCode() {
			return super.hashCode() * 37 + getB().hashCode();
		}

		@Override
		public String toString() {
			return super.toString() + "+" + getB().toString();
		}
	}

	public static class ThreeValue<A, B, C> extends TwoValue<A, B> {
		private static final long serialVersionUID = -7456159826566200834L;
		private C c;

		public ThreeValue() {
		}

		public ThreeValue(A a, B b, C c) {
			super(a, b);
			this.c = c;
		}

		public C getC() {
			return c;
		}

		public void setC(C c) {
			this.c = c;
		}

		@Override
		public int hashCode() {
			return super.hashCode() * 37 + getC().hashCode();
		}

		@Override
		public boolean equals(Object object) {
			return (object instanceof ThreeValue)
					&& ((ThreeValue<?, ?, ?>) object).getC().equals(getC())
					&& super.equals(object);
		}

		@Override
		public String toString() {
			return super.toString() + "+" + getC().toString();
		}
	}

	/**
	 * 读取流
	 * 
	 * @param fileName
	 * @return
	 */
	public static InputStream readInputStream(String fileName) {
		if (fileName == null || fileName.length() == 0)
			return null;

		InputStream inputStream = null;
		try {
			inputStream = GeneralApi.class.getClassLoader()
					.getResourceAsStream(fileName);
		} catch (Exception e) {
			//LogHelper.PRESALES_LOG.error("读取输入流失败", e);
		}

		return inputStream;
	}

	/**
	 * 从输入流中读取内容到String中, 输入流需要自己关闭
	 * 
	 * @param inputStream
	 *            ：输入流
	 * @param quitString
	 *            ：结束字符串, null表示读到文件结尾
	 * @param bFilter
	 *            : 是否使用##进行行注释
	 * @param charSet
	 *            ：字符集，默认"UTF-8"
	 * @return
	 * @throws IOException
	 */
	public static String inputStreamToString(InputStream inputStream,
			String quitString, boolean bFilter, String charSet)
			throws IOException {

		if (charSet == null) {
			charSet = "UTF-8";
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				inputStream, charSet));
		StringBuilder sb = new StringBuilder("");
		String tmpString;
		while ((tmpString = br.readLine()) != null) {

			if (quitString != null && tmpString.indexOf(quitString) != -1) {
				break;
			}

			int annotationIndex = -1;
			if (bFilter && (annotationIndex = tmpString.indexOf("##")) != -1) {
				tmpString = tmpString.substring(0, annotationIndex);
			}
			sb.append(tmpString + "\n");
		}
		// br.close();//br不关，输入流自己关闭，会出问题么？

		return sb.toString();
	}

	/**
	 * 从输入流中读取内容到String中, 输入流需要自己关闭, 字符集，默认"UTF-8"
	 * 
	 * @param inputStream
	 *            ：输入流
	 * @param quitString
	 *            ：结束字符串, null表示读到文件结尾
	 * @param bFilter
	 *            : 是否使用##进行行注释
	 * @return
	 * @throws IOException
	 */
	public static String inputStreamToString(InputStream inputStream,
			String quitString, boolean bFilter) throws IOException {
		return inputStreamToString(inputStream, quitString, bFilter, null);
	}

	/**
	 * 从输入流中读取内容到String中,##表示行注释, 输入流需要自己关闭, 字符集，默认"UTF-8"
	 * 
	 * @param inputStream
	 *            ：输入流
	 * @param quitString
	 *            ：结束字符串, null表示读到文件结尾
	 * @return
	 * @throws IOException
	 */
	public static String inputStreamToString(InputStream inputStream,
			String quitString) throws IOException {
		return inputStreamToString(inputStream, quitString, true);
	}

	/**
	 * 从文件中读取内容到String中, 输入流需要自己关闭
	 * 
	 * @param fileName
	 *            : 文件名
	 * @param bFilter
	 *            : 是否使用##进行行注释
	 * @param charSet
	 *            : 字符集，默认"UTF-8"
	 * @return
	 * @throws IOException
	 */
	public static String readFileToString(String fileName, boolean bFilter,
			String charSet) throws IOException {
		String strResult = null;
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(fileName);
			strResult = inputStreamToString(fileInputStream, null, bFilter,
					charSet);

		} finally {
			if (null != fileInputStream) {
				fileInputStream.close();
			}
		}
		return strResult;
	}

	/**
	 * 读取文件内容到String中, ##表示行注释
	 * 
	 * @param fileName
	 *            ：文件路径
	 * @param charSet
	 *            ：字符集，默认"UTF-8"
	 * @throws IOException
	 */
	public static String readFileToString(String fileName, String charSet)
			throws IOException {
		return readFileToString(fileName, true, charSet);
	}

	/**
	 * 读取文件内容到String中, ##表示行注释, 字符集，默认"UTF-8"
	 * 
	 * @throws IOException
	 */
	public static String readFileToString(String fileName) throws IOException {
		return readFileToString(fileName, null);
	}

	/**
	 * 读取console输入内容到String中,以单行quitString 表示结束
	 */
	public static String readConsoleToString(String quitString) {
		try {
			return inputStreamToString(System.in, quitString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取一行console输入内容到String中
	 */
	public static String readConsoleLineToString() {
		String strResult = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			strResult = br.readLine();
			// br.close();//console 不能关闭
		} catch (Exception e) {
			//LogHelper.PRESALES_LOG.error("readConsoleLineToString失败", e);
		}
		return strResult;
	}

	/**
	 * 从配置文件字符串里查找指定字符串 格式如下： owlFile:
	 * "E:/ontologiesRdf/SelfPickup/Ontology1352803558333-RDF.owl"
	 * 
	 * @param str
	 *            ：配置文件字符串
	 * @param strPre
	 *            ：匹配字符串前缀
	 * @return ：查找的字符串
	 */
	public static String ReadProfile(String str, String strPre) {

		int startIndex = 0;

		startIndex = str.indexOf(strPre);
		if (startIndex == -1)
			return null;

		startIndex = str.indexOf("\"", startIndex + strPre.length()) + 1;
		if (startIndex == -1)
			return null;

		int endIndex = str.indexOf("\"", startIndex);
		if (endIndex == -1)
			return null;

		return str.substring(startIndex, endIndex).trim();
	}

	/**
	 * 从配置文件字符串里查找指定字符串集合 格式如下： owlFile:
	 * "E:/ontologiesRdf/SelfPickup/Ontology1352803558333-RDF.owl"
	 * 
	 * @param str
	 *            ：配置文件字符串
	 * @param strPre
	 *            ：匹配字符串前缀
	 * @return ：查找的字符串
	 */
	public static List<String> ReadProfileList(String str, String strPre) {

		List<String> profileList = new ArrayList<String>();
		String allProfileString = ReadProfile(str, strPre);
		for (String profile : allProfileString.split(",")) {
			profileList.add(profile.trim());
		}
		return profileList;
	}

	/**
	 * 删除某个文件夹下的所有文件夹和文件
	 * 
	 * @param delpath
	 *            String
	 * @throws Exception
	 * @return boolean
	 */
	public static boolean deleteAllFiles(String delpath) {
		if (null == delpath) {
			//LogHelper.PRESALES_LOG.error("文件路径为空");
			return false;
		}
		File file = new File(delpath);
		if (!file.exists()) {
			//LogHelper.PRESALES_LOG.debug("文件不存在");
			return true;
		}

		if (!file.isDirectory()) {
			file.delete();
		} else {
			// if (file.isDirectory()) {
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				File delfile = new File(delpath + File.separator + filelist[i]);
				if (!delfile.isDirectory()) {
					delfile.delete();
				} else {
					// if (delfile.isDirectory()) {
					deleteAllFiles(delpath + File.separator + filelist[i]);
				}
			}
			file.delete();
		}
		return true;
	}

	/**
	 * 文件路径加上后缀 
	 *@param filePath
	 *@return
	 * Date:2013年11月8日
	 * Time:下午1:37:53
	 */
	public static String addFileLastSeparator(String filePath) {
		if (!(filePath.endsWith(File.separator) || filePath.endsWith("/") || filePath
				.endsWith("\\"))) {
			filePath += File.separator;
		}
		return filePath;
	}

	/**
	 * 递归复制文件或文件夹到指定目录
	 * 
	 * @param oldPath
	 *            String 原文件路径可以是文件也可以是文件夹
	 * @param newPath
	 *            String 复制后路径文件夹
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			newPath = addFileLastSeparator(newPath);
			File newfile = new File(newPath);
			newfile.mkdirs();
			File oldfile = new File(oldPath);

			if (oldfile.isFile()) {
				// int bytesum = 0;
				// int byteread = 0;
				if (oldfile.exists()) { // 文件存在时
					@SuppressWarnings("resource")
					FileChannel inChannel = new FileInputStream(oldPath)
							.getChannel();
					@SuppressWarnings("resource")
					FileChannel outChannel = new FileOutputStream(newPath
							+ oldfile.getName()).getChannel();
					inChannel.transferTo(0, inChannel.size(), outChannel);
					outChannel.close();
					inChannel.close();
					// InputStream inStream = new FileInputStream(oldPath); //
					// 读入原文件
					// FileOutputStream fs = new FileOutputStream(newPath +
					// oldfile.getName());
					// byte[] buffer = new byte[1024];
					// while ((byteread = inStream.read(buffer)) != -1) {
					// bytesum += byteread; // 字节数 文件大小
					// fs.write(buffer, 0, byteread);
					// }
					// inStream.close();
					// fs.close();
				}
				// System.out.println("复制文字节个数:" + bytesum);
			} else if (oldfile.isDirectory()
					&& (newfile.isDirectory() || !newfile.exists())) {
				for (File file : oldfile.listFiles()) {
					if (file.isFile()) {
						copyFile(file.getPath(), newPath);
					} else if (file.isDirectory()) {
						copyFile(file.getPath(), newPath + file.getName());
					}
				}
			}

		} catch (Exception e) {
			//LogHelper.PRESALES_LOG.error("复制文件操作出错", e);
		}
	}

	// 文件内容的总行数。
	public static int getTotalLines(String file) throws IOException {
		if (null == file || !new File(file).isFile()) {
			return 0;
		}

		int lines = 0;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			while (null != reader.readLine()) {
				lines++;
			}
		} finally {
			if (null != reader) {
				reader.close();
			}
		}
		return lines;
	}

	/**
	 * 全角转半角
	 * 
	 * @param QJstr
	 * @return
	 */
	public static String full2HalfChange(String QJstr) {
		StringBuilder outStrBuf = new StringBuilder("");
		String Tstr = "";
		byte[] b = null;
		for (int i = 0; i < QJstr.length(); i++) {
			Tstr = QJstr.substring(i, i + 1);
			// 全角空格转换成半角空格
			if (Tstr.equals("　")) {
				outStrBuf.append(" ");
				continue;
			}
			try {
				b = Tstr.getBytes("unicode");
				// 得到 unicode 字节数据
				if (b[2] == -1) {
					// 表示全角
					b[3] = (byte) (b[3] + 32);
					b[2] = 0;
					outStrBuf.append(new String(b, "unicode"));
				} else {
					outStrBuf.append(Tstr);
				}
			} catch (UnsupportedEncodingException e) {
				/*LogHelper.PRESALES_LOG.error("UnsupportedEncodingException失败",
						e);*/
			}

		} // end for.
		return outStrBuf.toString();
	}

	/**
	 * 半角转全角
	 * 
	 * @param QJstr
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static final String half2Fullchange(String QJstr)
			throws UnsupportedEncodingException {

		StringBuilder outStrBuf = new StringBuilder("");

		String Tstr = "";

		byte[] b = null;

		for (int i = 0; i < QJstr.length(); i++) {

			Tstr = QJstr.substring(i, i + 1);

			if (Tstr.equals(" ")) {

				// 半角空格

				outStrBuf.append("　");

				continue;

			}

			b = Tstr.getBytes("unicode");

			if (b[2] == 0) {

				// 半角?

				b[3] = (byte) (b[3] - 32);

				b[2] = -1;

				outStrBuf.append(new String(b, "unicode"));

			} else {

				outStrBuf.append(Tstr);

			}

		}

		return outStrBuf.toString();

	}

	/**
	 * 比较两个对象是否相等，可以为null
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equalAB(Object a, Object b) {
		return null == a ? null == b : a.equals(b);
	}

	/**
	 * 得到忽略空格的最长公共子串，返回<最大长度， 最大长度i位置， 最大长度j位置>
	 * 
	 * @param firstString
	 * @param secendString
	 * @param lengthSign
	 * @param backSign
	 * @return
	 */
	private static ThreeValue<Integer, Integer, Integer> getLCSMark(
			String firstString, String secendString, int[][] lengthSign,
			int[][] backSign) {

		int maxI = 0, maxJ = 0;
		int maxLength = -1;

		int m = firstString.length();
		int n = secendString.length();
		for (int i = 1; i <= m; ++i) {
			for (int j = 1; j <= n; ++j) {
				if (firstString.charAt(i - 1) == secendString.charAt(j - 1)) {
					lengthSign[i][j] = ' ' == firstString.charAt(i - 1) ? lengthSign[i - 1][j - 1]
							: lengthSign[i - 1][j - 1] + 1;
					if (maxLength < lengthSign[i][j]) {
						maxLength = lengthSign[i][j];
						maxI = i;
						maxJ = j;
					}
					backSign[i][j] = 0;
				} else if (firstString.charAt(i - 1) == ' ') {
					lengthSign[i][j] = lengthSign[i - 1][j];
					backSign[i][j] = 1;
				} else if (secendString.charAt(j - 1) == ' ') {
					lengthSign[i][j] = lengthSign[i][j - 1];
					backSign[i][j] = -1;
				} else {
					lengthSign[i][j] = 0;
					backSign[i][j] = -2;
				}
			}
		}
		return new ThreeValue<Integer, Integer, Integer>(maxLength, maxI, maxJ);
	}

	/**
	 * 得到忽略空格的最长公共子串长度
	 * 
	 * @param firstString
	 * @param secendString
	 * @return
	 */
	public static int getLCSLength(String firstString, String secendString) {
		int m = firstString.length();
		int n = secendString.length();
		return getLCSMark(firstString, secendString, new int[m + 1][n + 1],
				new int[m + 1][n + 1]).getA();
	}

	/**
	 * 得到忽略空格的最长公共子串<长度, 第一字符串匹配开始位置， 第一个字符串匹配结束位置>
	 * 
	 * @param firstString
	 * @param secendString
	 * @return
	 */
	public static ThreeValue<Integer, Integer, Integer> getLCSFirstStringIndex(
			String firstString, String secendString) {
		int m = firstString.length();
		int n = secendString.length();
		int lengthSign[][] = new int[m + 1][n + 1];
		int backSign[][] = new int[m + 1][n + 1];

		ThreeValue<Integer, Integer, Integer> lcsMatch = getLCSMark(
				firstString, secendString, lengthSign, backSign);

		Integer startIndex = lcsMatch.getB(), endIndex = lcsMatch.getB();
		int i = lcsMatch.getB(), j = lcsMatch.getC();
		while (i != 0 && j != 0) {
			if (0 == backSign[i][j]) {
				startIndex = i;
				endIndex = endIndex > i ? endIndex : i;
				--i;
				--j;
			} else if (1 == backSign[i][j]) {
				--i;
			} else if (-1 == backSign[i][j]) {
				--j;
			} else {
				break;
			}
		}

		return new ThreeValue<Integer, Integer, Integer>(lcsMatch.getA(),
				startIndex - 1, endIndex);
	}

	/**
	 * 判断是否是数字字符串
	 * 
	 * @param number
	 * @return
	 */
	public static String NUMBER_REG = "[-+]?(([0-9]+)([.]([0-9]+))?|[.]([0-9]+))";

	public static boolean isNumber(String number) {
		if (null == number || number.trim().isEmpty()) {
			return false;
		}
		return number.matches("^ *" + NUMBER_REG + " *$");
	}

	/**
	 * 判断是否是数词字符串,包含中文数字和大写
	 * 
	 * @param numeral
	 * @return
	 */
	public static String WORD_FIGURE_REG = "[零壹贰叁肆伍陆柒捌玖拾佰仟万亿]+|[零另一二两三四五六七八九十百千万亿]+";

	public static boolean isNumeral(String numeral) {
		if (null == numeral || numeral.trim().isEmpty()) {
			return false;
		}
		numeral = numeral.trim();
		return numeral.matches("^ *(" + WORD_FIGURE_REG + ") *$")
				|| isNumber(numeral);
	}

	/**
	 * 判断是否是纯中文字符串
	 * 
	 * @param chinese
	 * @return
	 */
	public static boolean isChinese(String chinese) {
		if (null == chinese) {
			return false;
		}
		return chinese.trim().matches("^[\u4e00-\u9fa5 ]+$");
	}

	/**
	 * 判断是否是 英文、数字、_、空格 字符串
	 * 
	 * @param english
	 * @return
	 */
	public static boolean isWords(String english) {
		if (null == english || english.trim().isEmpty()) {
			return false;
		}
		return english.matches("^[\\w ]+$");
	}

	public static String REMOVE_SPECAIL_CHAR = "！!：:＂“”\"；;＇‘’'？?，,．。、";

	/**
	 * 全角转换半角，转小写，替换连续空白字符为一个空格,去掉语气词，去掉html标签 User:cdzhangxi Date:2013-5-24
	 * Time:下午07:53:19
	 * 
	 * @param inString
	 * @return
	 */
	public static String stringPreproccess(String inString) {
		if (null == inString || inString.isEmpty()) {
			//LogHelper.PRESALES_LOG.warn("参数为空");
			return "";
		}
		String outString = inString;
		outString = full2HalfChange(outString);
		outString = outString.toLowerCase();
		// outString = removeModalParticles(outString);
		outString = removeHtmlLabel(outString);
		outString = outString.replaceAll("\\s+", " ");
		outString = Traditional.TwtoCN(outString);
		return outString;
	}

	/**
	 * 忽略空格的字符串包含,sub是空串时，mother有值返回true
	 * 
	 * @param mother
	 * @param sub
	 * @return
	 */
	public static boolean containsIgnoreBlank(String mother, String sub) {
		boolean bool = null == mother ? null == sub : mother.replaceAll(" +",
				"").contains(sub.replaceAll(" +", ""));
		return bool;
	}

	/**
	 * 忽略空格的字符串包含,sub是空串时，返回false
	 * 
	 * @param mother
	 * @param sub
	 * @return
	 */
	public static boolean containsIgnoreBlank3(String mother, String sub) {
		boolean bool = null == mother ? null == sub : mother.replaceAll(" +",
				"").contains(sub.replaceAll(" +", ""));
		sub = sub.replaceAll(" +", "");
		if ("".equals(sub)) {
			return false;
		}
		return bool;
	}

	/**
	 * 判断两字符串是否有交集
	 * 
	 * @param mother
	 * @param sub
	 * @return
	 */
	public static boolean containsIntersect(String mother, String sub) {
		mother = mother.replaceAll(" +", "");
		sub = sub.replaceAll(" +", "");
		Boolean bool = false;
		if (mother.contains(sub) || sub.contains(mother) || mother.equals(sub)) {
			bool = true;
			return bool;
		}
		int lena = mother.length();
		for (int i = 0; i < lena - 1; i++) {
			String c = mother.substring(i, i + 2);
			int t = sub.indexOf(c);
			if (t != -1) {
				bool = true;
				break;
			}
		}
		System.out.println(bool);
		return bool;
	}

	/**
	 * 忽略空格的字符串比较
	 * 相等，返回0
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static int compareIgnoreBlank(String s1, String s2) {

		return null == s1 ? (null == s2 ? 0 : -1) : (null == s2 ? 1 : s1
				.replace(" ", "").compareTo(s2.replace(" ", "")));
	}

	/**
	 * 忽略空格的字符串查找,从母串<startIndex>开始，返回所有匹配到的 <第一个字符串的开始位置， 第一个字符串的结束位置>
	 * 
	 * @param motherString
	 * @param subString
	 * @param startIndex
	 * @param findNumber
	 *            ：控制查找个数
	 * @return
	 */
	public static List<TwoValue<Integer, Integer>> indexOfIgnoreBlank(
			String motherString, String subString, int startIndex,
			int findNumber) {
		List<TwoValue<Integer, Integer>> indexOfs = new ArrayList<TwoValue<Integer, Integer>>();
		if (null == motherString || null == subString || motherString.isEmpty()
				|| subString.trim().isEmpty() || findNumber <= 0) {
			return indexOfs;
		}

		subString = subString.trim();

		int matcherIndex = -1;
		int matcherIndexEnd = -1;
		for (int i = startIndex; i < motherString.length(); ++i) {
			int j = 0;
			int k = i;
			if (' ' == motherString.charAt(k)) {
				continue;
			}
			while (k < motherString.length() && j < subString.length()) {
				if (motherString.charAt(k) == subString.charAt(j)) {
					++k;
					++j;
				} else if (' ' == motherString.charAt(k)) {
					++k;
				} else if (' ' == subString.charAt(j)) {
					++j;
				} else {
					break;
				}
			}
			if (j == subString.length()) {
				matcherIndex = i;
				matcherIndexEnd = k;
				indexOfs.add(new TwoValue<Integer, Integer>(matcherIndex,
						matcherIndexEnd));
				if (--findNumber == 0) {
					break;
				}
				// i = k - 1;
			}
		}
		return indexOfs;
	}

	/**
	 * 忽略空格的字符串查找，返回第一个匹配的 <第一个字符串的开始位置， 第一个字符串的结束位置>
	 * 
	 * @param matherString
	 * @param subString
	 * @return
	 */
	public static TwoValue<Integer, Integer> indexOfFirstIgnoreBlank(
			String matherString, String subString) {
		List<TwoValue<Integer, Integer>> indexOfs = indexOfIgnoreBlank(
				matherString, subString, 0, 1);
		return indexOfs.size() > 0 ? indexOfs.get(0) : null;
	}

	/**
	 * 忽略空格的字符串查找,从母串<startIndex>开始，返回第一个匹配的 <第一个字符串的开始位置， 第一个字符串的结束位置>,没ull
	 * 
	 * @param matherString
	 * @param subString
	 * @param startIndex
	 * @return
	 */
	public static TwoValue<Integer, Integer> indexOfFirstIgnoreBlank(
			String matherString, String subString, int startIndex) {
		List<TwoValue<Integer, Integer>> indexOfs = indexOfIgnoreBlank(
				matherString, subString, startIndex, 1);
		return indexOfs.size() > 0 ? indexOfs.get(0) : null;
	}

	/**
	 * 忽略空格的字符串查找，返回所有匹配到的 <第一个字符串的开始位置， 第一个字符串的结束位置>
	 * 
	 * @param matherString
	 * @param subString
	 * @return
	 */
	public static List<TwoValue<Integer, Integer>> indexOfAllIgnoreBlank(
			String matherString, String subString) {
		return indexOfIgnoreBlank(matherString, subString, 0, Integer.MAX_VALUE);
	}

	/**
	 * 忽略空格的字符串查找,从母串<startIndex>开始，返回所有匹配到的 <第一个字符串的开始位置， 第一个字符串的结束位置>
	 * 
	 * @param matherString
	 * @param subString
	 * @param startIndex
	 * @return
	 */
	public static List<TwoValue<Integer, Integer>> indexOfAllIgnoreBlank(
			String matherString, String subString, int startIndex) {
		return indexOfIgnoreBlank(matherString, subString, startIndex,
				Integer.MAX_VALUE);
	}

	/**
	 * 查找文件中首行包含<containedString>的行， <startLineFlag>为行开始标志，
	 * 结果放到<outFileName>文件夹下的<containedString>.log文件中
	 * 
	 * @param inFileName
	 * @param outFileName
	 * @param containedString
	 * @param startLineFlag
	 * @throws IOException
	 */
	public static void grepFile(String inFileName, String outFileName,
			String containedString, String startLineFlag) throws IOException {
		outFileName += containedString + ".log";
		BufferedReader br = new BufferedReader(new FileReader(inFileName));
		try {
			FileWriter outFile = new FileWriter(outFileName);
			try {
				String readString;
				boolean bRead = false;
				while (null != (readString = br.readLine())) {
					if (readString.contains(startLineFlag)) {
						bRead = false;
					}
					if (readString.contains(containedString)) {
						outFile.write(readString + "\n");
						bRead = true;
					} else if (bRead) {
						outFile.write(readString + "\n");
					}
				}
			} finally {
				outFile.close();
			}
		} finally {
			br.close();
		}

	}

	/**
	 * 使用<separator>分割字符串
	 * 
	 * @param separator
	 * @param connectString
	 * @return
	 */
	public static String[] splitConnectString(char[] separator,
			String connectString) {
		return null != connectString ? connectString.split(String
				.valueOf(separator)) : new String[0];
	}

	/**
	 * 使用<separator>连接字符串到<connectStringBuilder>后面，
	 * 保证<connectStringBuilder>开头不为分隔符
	 * 
	 * @param separator
	 * @param connectStringBuilder
	 * @param appendStrings
	 * @return
	 */
	public static StringBuilder connectString(char[] separator,
			StringBuilder connectStringBuilder, String... appendStrings) {
		if (appendStrings.length == 0) {
			return connectStringBuilder;
		}
		if (0 != connectStringBuilder.length()) {
			connectStringBuilder.append(separator);
		}
		connectStringBuilder.append(appendStrings[0]);
		for (int i = 1; i < appendStrings.length; ++i) {
			connectStringBuilder.append(separator).append(appendStrings[i]);
		}
		return connectStringBuilder;
	}

	/**
	 * 使用<separator>连接字符串
	 * 
	 * @param separator
	 * @param connectStrings
	 * @return
	 */
	public static String connectString(char[] separator,
			String... connectStrings) {
		StringBuilder sb = new StringBuilder("");
		connectString(separator, sb, connectStrings);
		return sb.toString();
	}

	/**
	 * 使用<Constants.GENERAL_FIRST_SPLIT_SIGN>作为默认分隔符分割字符串
	 * 
	 * @param connectString
	 * @return
	 */
	public static String[] splitConnectString(String connectString) {
		return splitConnectString(Constants.GENERAL_FIRST_SPLIT_SIGN,
				connectString);
	}

	/**
	 * 使用<Constants.GENERAL_FIRST_SPLIT_SIGN>作为默认分隔符连接字符串
	 * 
	 * @param connectStrings
	 * @return
	 */
	public static String connectString(String... connectStrings) {
		return connectString(Constants.GENERAL_FIRST_SPLIT_SIGN, connectStrings);
	}

	/**
	 * 使用<Constants.GENERAL_FIRST_SPLIT_SIGN>连接字符串到<connectStringBuilder>后面，
	 * 保证<connectStringBuilder>开头不为分隔符
	 * 
	 * @param connectStringBuilder
	 * @param appendStrings
	 * @return
	 */
	public static StringBuilder connectString(
			StringBuilder connectStringBuilder, String... appendStrings) {
		return connectString(Constants.GENERAL_FIRST_SPLIT_SIGN,
				connectStringBuilder, appendStrings);
	}

	public static String urlRegex = "((http://)?([\\w]+[.])|(www.))\\w+[.]([a-z]{2,4})?[[.]([a-z]{2,4})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z]{2,4}+|/?)";
	private static String A1 = " <a target=\"_bank\" href={0}>";
	private static String A2 = " </a>";

	/**
	 * 检索出URL并附加<a></a>标记
	 * 
	 * @param title
	 * @return
	 */
	public static String toHref(String title) {
		StringBuffer sb = new StringBuffer(title);
		Pattern pat = Pattern.compile(urlRegex);
		Matcher mat = pat.matcher(title);
		int index = 0;
		int index1 = 0;
		while (mat.find()) {
			String url = mat.group();
			// System.out.println(url);
			if (url.indexOf("http://") != 0)
				url = "http://" + url;
			Object obj[] = { "'" + url + "'" };
			String a = MessageFormat.format(A1, obj);
			int l = a.length();
			index += index1;
			sb.insert(mat.start() + index, a);
			index += l;
			sb.insert((mat.end()) + index, A2);
			index1 = A2.length();
		}
		return sb.toString();
	}

	public static Integer indexRexOf(String mother, String regs, Integer start) {
		Integer regStart = -1;
		Pattern pattern = Pattern.compile(regs);
		Matcher matcher = pattern.matcher(mother);
		if (matcher.find(start)) {
			regStart = matcher.start();
		}
		return regStart;
	}

	public static ThreeValue<String, Integer, Integer> getFirstUrls(
			String oriString) {
		Pattern pat = Pattern.compile(urlRegex);
		Matcher mat = pat.matcher(oriString);
		int startIndex = Integer.MAX_VALUE;
		int endIndex = -1;
		String url = null;
		while (mat.find()) {
			url = mat.group();
			startIndex = mat.start();
			endIndex = mat.end();
		}
		return new ThreeValue<String, Integer, Integer>(url, startIndex,
				endIndex);
	}

	public static String removeHtmlLabel(String input) {
		return removeHtmlLabel(input, input.length());
	}

	/**
	 * 删除input字符串中的html格式
	 * 
	 * @param input
	 * @param length
	 * @return
	 */
	public static String removeHtmlLabel(String input, int length) {
		if (input == null || input.equals("")) {
			return "";
		}
		// 去掉所有html元素,
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
				"<[^>]*>", " ");
		// str = str.replaceAll("[(/>)<]", "");
		int len = str.length();
		if (len <= length) {
			return str;
		} else {
			str = str.substring(0, length);
			str += "......";
		}
		return str;
	}

	public static void main(String[] arg) throws IOException,
			InterruptedException {
	}
}
