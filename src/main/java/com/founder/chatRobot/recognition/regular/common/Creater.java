package com.founder.chatRobot.recognition.regular.common;

import java.lang.reflect.InvocationTargetException;

public class Creater<T> {
	private java.lang.reflect.Constructor<T> construct;

	@SuppressWarnings("unchecked")
	public Creater(@SuppressWarnings("rawtypes") Class type) {
		try {
			this.construct = type.getConstructor(String.class);
			this.construct.setAccessible(true);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public T create(String express) {
		try {
			return this.construct.newInstance(express);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
