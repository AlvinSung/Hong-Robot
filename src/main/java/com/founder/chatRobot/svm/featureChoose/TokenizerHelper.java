package com.founder.chatRobot.svm.featureChoose;

import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.lang3.StringUtils;

import com.founder.chatRobot.common.CharacterHelper;
import com.founder.chatRobot.domain.Constants;

public class TokenizerHelper {

	/**
	 * 把内容按ngram进行切分，切分时只对中文进行切分，相邻英文或数字连到一起，即连续英文数字算一个字符
	 * 
	 * @param sentence
	 * @param minLen
	 *            token最小长度
	 * @param maxLen
	 *            token最大长度
	 * @param needEnglish
	 *            是否考虑英文和数字
	 * @return
	 */
	public static List<String> ngramTokenize(String sentence, int minLen,
			int maxLen, boolean needEnglish) {
		List<String> tokens = new ArrayList<String>();
		if (sentence == null || sentence.length() == 0)
			return tokens;

		final String splitString = " ";
		List<String> charsList = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < sentence.length(); i++) {
			if (CharacterHelper.isArabicNumber(sentence.charAt(i))
					|| CharacterHelper.isEnglishLetter(sentence.charAt(i))) {
				sb.append(sentence.charAt(i));
			} else {
				if (sb.length() > 0) {
					charsList.add(sb.toString());
				}
				if (!CharacterHelper.isPunctuation(sentence.charAt(i))
						&& sentence.charAt(i) != '\n'
						&& sentence.charAt(i) != '\t') {
					charsList.add(String.valueOf(sentence.charAt(i)));
				} else {
					charsList.add(splitString); // 句子分割符
				}
				sb = new StringBuilder();
			}
		}
		if (sb.length() > 0) {
			charsList.add(sb.toString());
		}

		//由于每个实体两边自动添加空格，需要把这种空格去掉
		for (int i = 0; i < charsList.size(); i++) {
			if (charsList.get(i).endsWith(Constants.entitySuffix)
					|| charsList.get(i).endsWith(Constants.factSuffix)) {
				if (i > 0 && splitString.equals(charsList.get(i - 1))) {
					charsList.remove(i - 1);
					--i;
				}
				if (i + 1 < charsList.size() && splitString.equals(charsList.get(i + 1))) {
					charsList.remove(i + 1);
					--i;
				}
			}
		}

		for (int i = 0; i < charsList.size(); i++) {
			StringBuilder token = new StringBuilder();
			int len = 0;
			for (int j = i; j < charsList.size(); j++) {
				if (!needEnglish
						&& CharacterHelper.containsNumberOrEnglish(charsList
								.get(j)))
					continue;
				if (splitString.equals(charsList.get(j)))
					break;
				token.append(charsList.get(j));
				len++;
				if (len >= minLen && len <= maxLen && StringUtils.isNotEmpty(token))
					tokens.add(token.toString());
				if (len > maxLen)
					break;
			}
		}

		return tokens;
	}

	/**
	 * 把内容按ansj分词进行切分
	 * 
	 * @param sentence
	 * @return
	 */
	public static List<String> ansjTokenize(String sentence) {
		List<String> tokens = new ArrayList<String>();
		if (sentence == null || sentence.length() == 0)
			return tokens;
		List<Term> terms = ToAnalysis.parse(sentence);
		for (Term term : terms) {
			if (CharacterHelper.containsChar(term.getName()))
				continue;
			tokens.add(term.getName());
		}
		return tokens;
	}

	public static void main(String[] args) {
		List<String> terms = ngramTokenize("\t中国\t可奈曙\tkdaslfj\tsss\t", 1, 2,
				true);
		for (String term : terms)
			System.out.println(term);
	}
}
