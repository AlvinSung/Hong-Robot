package com.founder.chatRobot.common.structures;

import java.util.Arrays;

class DoubleMapIndex implements Comparable<DoubleMapIndex>{
	static double[][] values;
	int index;
	static int order;//1 asc -1 desc
	static int cmp;
	
	public DoubleMapIndex(int index) {
		this.index = index;
	}

	public static int[] getOrderIndex(double[][] values, int order, int selectedAmount) {
		int numAttributes = values[0].length;
		int numClasses = values.length;
		DoubleMapIndex.values = values;
		DoubleMapIndex.order = order;
		DoubleMapIndex[] doubleMapIndexs = new DoubleMapIndex[numAttributes];
		for (int i = 0; i < numAttributes; i++) {
			doubleMapIndexs[i] = new DoubleMapIndex(i); 
		}
		boolean[] isSelected = new boolean[numAttributes];
		for (int i = 0; i < numAttributes; i++) {
			isSelected[i] = false; 
		}
		int[] index = new int[selectedAmount];
		for (int j = 0; j < numClasses; j++) {
			cmp = j;
			Arrays.sort(doubleMapIndexs);
			
			for (int i = 0, k = 0; i * numClasses + j < selectedAmount; i++, k++) {
				while(isSelected[doubleMapIndexs[k].index] == true)
					k++;
				index[i * numClasses + j] = doubleMapIndexs[k].index;
				isSelected[index[i * numClasses + j]] = true;
			}
		}
		DoubleMapIndex.values = null;
		return index;
	}
	
	public int compareTo(DoubleMapIndex arg0) {
		if(values[cmp][this.index] > values[cmp][arg0.index])
			return 1 * order;
		else
			return -1 * order;
	}
}

public class DoubleIndex implements Comparable<DoubleIndex>{
	static double[] values;
	static int order;//1 asc -1 desc
	private int index;
	public int compareTo(DoubleIndex o) {
		if (values[index] > values[o.index])
			return 1 * order;
		else {
			return -1 * order;
		}
	}
	public DoubleIndex(int index) {
		this.index = index;
	}
	public static void InitialValues(double[] values, int order) {
		DoubleIndex.values = values;
		DoubleIndex.order = order;
	}
	public static void free() {
		DoubleIndex.values = null;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
}
