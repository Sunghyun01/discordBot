package com;

import java.io.IOException;
import java.util.ArrayList;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class jsoup {
	public static String result_text="";
	public static int point(String tier) {
		int user_point = 0;
		switch(tier) {
			case "Iron 4":
				user_point = 1;
				break;
			case "Iron 3":
				user_point = 2;
				break;
			case "Iron 2":
				user_point = 3;
				break;
			case "Iron 1":
				user_point = 4;
				break;
			case "Bronze 4":
				user_point = 5;
				break;
			case "Bronze 3":
				user_point = 6;
				break;
			case "Bronze 2":
				user_point = 7;
				break;
			case "Bronze 1":
				user_point = 8;
				break;
			case "Silver 4":
				user_point = 9;
				break;
			case "Silver 3":
				user_point = 10;
				break;
			case "Silver 2":
				user_point = 11;
				break;
			case "Silver 1":
				user_point = 12;
				break;
			case "Gold 4":
				user_point = 13;
				break;
			case "Gold 3":
				user_point = 14;
				break;
			case "Gold 2":
				user_point = 15;
				break;
			case "Gold 1":
				user_point = 16;
				break;	
			case "Platinum 4":
				user_point = 17;
				break;
			case "Platinum 3":
				user_point = 18;
				break;
			case "Platinum 2":
				user_point = 19;
				break;
			case "Platinum 1":
				user_point = 20;
				break;
			case "Diamond 4":
				user_point = 22;
				break;
			case "Diamond 3":
				user_point = 24;
				break;
			case "Diamond 2":
				user_point = 26;
				break;
			case "Diamond 1":
				user_point = 28;
				break;
			case "Master":
				user_point = 30;
				break;
			case "Grandmaster":
				user_point = 35;
				break;
			case "Challenger":
				user_point = 40;
				break;
		}
		return user_point;
	}
	public static String Record(String message) {
			Document doc = null;
		    String url = "https://www.op.gg/summoner/userName="+message;
		   
	        try {
	            doc = Jsoup.connect(url).cookie("customLocale","ko_KR").get();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        String title = doc.select(".Information>.Name").text();
	        String PastRankList = doc.select(".PastRankList li").text();
	        String main_tier = doc.select(".TierRank").text();
	        String sub_tier = doc.select(".sub-tier__rank-tier").text();
	        Elements mostCham = doc.select(".MostChampionContent .ChampionName");
	        Iterator<Element> temp = mostCham.iterator();
	        String KDA[] = doc.select(".KDA .KDA").text().split(" ");
	        String WinRatio[] = doc.select(".MostChampionContent .WinRatio").text().split(" "); 
	        String Played[] = doc.select(".Played .Title").text().split(" "); 
	        int point = point(main_tier);
	        int j = 0;
	        
	        result_text = "============================================\n";
	        result_text+="★"+title+"★\n";
	        result_text+=PastRankList+"\n\n";
	        result_text+="솔로랭크 : "+main_tier+"	자유랭크 : "+sub_tier+"\n";
	        result_text+="내전 점수 : "+point+"\n\n";
	        if(!main_tier.equals("Unranked")) {
		        for(int i=0;i<3;i++) {
		        	Element cham = temp.next();
		        	result_text+="모스트"+(i+1)+" : "+cham.attr("title")+"	"+KDA[i]+"	"+WinRatio[i]+"	"+Played[j]+"게임\n";
		        	j+=2;
		        }
	        }
	        result_text+="============================================";
	        
	        return result_text;
	}
	public static String notice() {
		Document doc = null;
	    String url = "https://cafe.naver.com/MyCafeIntro.nhn?clubid=29785987";  
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result_text = "**클랜 공지사항 모음**\n\n";
        Elements title = doc.select(".c-d-l12 .board-box .type_main .inner_list a");
        int i = 2;
        for(Element e: title){  
        	result_text += e.attr("title")+"\n";
        	if(i%2==0) {
        		result_text += "https://cafe.naver.com/"+e.attr("href")+"\n";
        	}
        	i++;
        }  
        
		return result_text;
    }
	public static String search_board(String search) {
		Document doc = null;
	    String url = "https://cafe.naver.com/ArticleSearchList.nhn?search.clubid=29785987&search.searchBy=0&search.query="+search;  
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result_text = "**게시글 검색결과**\n\n";
        Elements title = doc.select(".inner_list a.article");
        System.out.println(title);
        int i = 2;
        for(Element e: title){  
        	result_text += e.text()+"\n";
        	if(i%2==0) {
        		result_text += "https://cafe.naver.com/"+e.attr("href")+"\n";
        	}
        	//i++;
        }  
		
		return result_text;
	}
}