package com;

import java.util.Scanner;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class JDABot{
	public static void main(String[] args){
		
		Scanner scanner = new Scanner(System.in);
		
		JDA jda = null;
		try {
			jda = new JDABuilder("NTM2ODYwNjc1MzEwMzU0NDQz.XZxjEA.4ozDXmRwwoXRD5ioYi8YXJ49Q9w").build();
		} catch (Exception e) {
			System.out.println("JDABot file Error");
		}
		jda.addEventListener(new Ready());
		jda.addEventListener(new RoleCreate());
	}
}