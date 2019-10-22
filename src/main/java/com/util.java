package com;

public class util {
	public static boolean isNumber(String input) {
		try {
			Long.parseLong(input);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public static boolean isNumber(char input) {
		int temp = input - '0';
		if(temp>9)return false;
		return true;
	}
	
}
