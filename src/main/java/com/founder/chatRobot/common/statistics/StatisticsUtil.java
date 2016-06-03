package com.founder.chatRobot.common.statistics;

public class StatisticsUtil {
	/** 
	* @Name: getU 
	* @Description: U分布求解
	* @param y
	* @param a
	* @param b
	* @return
	* @throws 
	* @date 2010-10-9
	*/
	public static double getU(double x, double a, double b) {
		double U = 1;
		while (a > 1) {
			a--;
			U *= x * (a + b) / a;
		}
		while (b > 1) {
			b--;
			U *= (1 - x) * (a + b) / b;
		}
		if (a == 0.5 && b == 0.5) {
			U *= Math.sqrt(x * (1 - x)) / Math.PI;
		}
		else if (a == 0.5 && b == 1) {
			U *= Math.sqrt(x) * (1 - x) / 2;
		}
		else if (a == 1 && b == 0.5) {
			U *= x * Math.sqrt(1 - x) / 2;
		}
		else if (a == 1 && b == 1) {
			U *= x * (1 - x);
		}
		else {
			System.out.println("something wrong occure in getU!");
			U = -1;
		}
		return U;
	}
	
	/** 
	* @Name: getI 
	* @Description: Beta分布求解
	* @param x
	* @param a
	* @param b
	* @return
	* @throws 
	* @date 2010-10-9
	*/
	public static double getI(double x, double a, double b) {
		double I = 0;
		while (a > 1) {
			--a;
			I += -getU(x, a, b) / a;
		}

		while (b > 1) {
			--b;
			I += getU(x, a, b) / b;
		}
		
		if (a == 0.5 && b == 0.5) {
			I += 1 - 2.0 / Math.PI * Math.atan(Math.sqrt((1 - x) / x));
		}
		else if (a == 0.5 && b == 1) {
			I += Math.sqrt(x);
		}
		else if (a == 1 && b == 0.5) {
			I += 1 - Math.sqrt(1 - x);
		}
		else if (a == 1 && b == 1) {
			I += x;
		}
		else {
			System.out.println("something wrong occure in getI!");
			I = -1;
		}
		return I;
	}
	
	/** 
	* @Name: getF 
	* @Description: 二分法求Beta分布的分位数
	* @param p
	* @param m
	* @param n
	* @throws 
	* @date 2010-10-9
	*/
	public static double getBeta(double p, double m, double n) {
		double a = 0, b = 1, c = 0.0000000001, x; 
		while (Math.abs(b - a) >= c) {
			x = (a + b) / 2;
			double I = getI(x, m, n);
			if (I - p != 0) {
				if ((getI(a, m, n) - p) * (I - p) < 0) {
					b = x;
				}
				else {
					a = x;
				}
			}
			else {
				a = b = x;
			}
		}
		return (a + b) / 2;		
	}
	
	public static double getF(double a, double n1, double n2) {
		//得到F分布显著水平为a，自由度为n1， n2的临界值（单尾）
		double p = 1 - a;
		double B = getBeta(p, 0.5 * n1, 0.5 * n2);
		double F = n2 * B / (n1 * (1 - B));
		return F;
	}
	public static double getT(double a, double n) {
		//得到t分布显著水平为a，自由度为n的数临界值（双尾）
		double T = Math.sqrt(n / getBeta(a, 0.5 * n, 0.5) - n);
		return T;
	}
	
	public static double getR(double a, double n) {
		//得到相关系数显著水平为a，自由度为n的相关系数临界值（双尾）
		double R = Math.sqrt(1 - getBeta(a, 0.5 * n, 0.5));
		return R;
	}
}
