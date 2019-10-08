package com;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import oracle.sql.CharacterSet;

public class jsoup {
	public static String result_text="";
	public static int point(String tier) {
		int user_point = 0;
		if(tier.contains("Iron 4")) {
			user_point = 1;
		}else if(tier.contains("Iron 3")) {
			user_point = 2;
		}else if(tier.contains("Iron 2")) {
			user_point = 3;
		}else if(tier.contains("Iron 1")) {
			user_point = 4;
		}else if(tier.contains("Bronze 4")) {
			user_point = 5;
		}else if(tier.contains("Bronze 3")) {
			user_point = 6;
		}else if(tier.contains("Bronze 2")) {
			user_point = 7;
		}else if(tier.contains("Bronze 1")) {
			user_point = 8;
		}else if(tier.contains("Silver 4")) {
			user_point = 9;
		}else if(tier.contains("Silver 3")) {
			user_point = 10;
		}else if(tier.contains("Silver 2")) {
			user_point = 11;
		}else if(tier.contains("Silver 1")) {
			user_point = 12;
		}else if(tier.contains("Gold 4")) {
			user_point = 13;
		}else if(tier.contains("Gold 3")) {
			user_point = 14;
		}else if(tier.contains("Gold 2")) {
			user_point = 15;
		}else if(tier.contains("Gold 1")) {
			user_point = 16;
		}else if(tier.contains("Platinum 4")) {
			user_point = 17;
		}else if(tier.contains("Platinum 3")) {
			user_point = 18;
		}else if(tier.contains("Platinum 2")) {
			user_point = 19;
		}else if(tier.contains("Platinum 1")) {
			user_point = 20;
		}else if(tier.contains("Diamond 4")) {
			user_point = 22;
		}else if(tier.contains("Diamond 3")) {
			user_point = 24;
		}else if(tier.contains("Diamond 2")) {
			user_point = 26;
		}else if(tier.contains("Diamond 1")) {
			user_point = 28;
		}else if(tier.contains("Master")) {
			user_point = 30;
		}else if(tier.contains("Grandmaster")) {
			user_point = 35;
		}else if(tier.contains("Challenger")) {
			user_point = 40;
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
        try {
        	String url = "https://cafe.naver.com/ArticleSearchList.nhn?search.clubid=29785987&search.searchBy=0&search.query="+URLEncoder.encode(search,"MS949");  
        	
            doc = Jsoup.connect(url).get();
        } catch (Exception e) {
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
	public static String MultiRecord(String[] getMessage) {
		Document doc = null;
		String users = "";
		result_text = "";
		for(int i=1;i<getMessage.length;i++) {
			users += "%2C"+getMessage[i];
		}
        try {
        	String url = "https://www.op.gg/multi/query="+users;
        	System.out.println(url);
            doc = Jsoup.connect(url).cookie("customLocale","ko_KR").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Elements summonerName = doc.select(".Summoner .SummonerName");
        Iterator<Element> summonerNameTemp = summonerName.iterator();
        
        String kda[] = doc.select(".KDA .Content").text().split(" ");
        String position[] = doc.select(".Position span").text().split(" ");
        String main_tier[] = doc.select(".TierRank .TierRank").text().split("\\)");
        Elements Champion = doc.select(".Champion .ChampionName"); 
        Iterator<Element> ChampionTemp = Champion.iterator();
        if(getMessage.length-1 >= 10) {
        	result_text = "요청한 인원의 수가 너무 많습니다 최대인원수 : 9"; 
        	return result_text;
        }
        for(int i=0; i < getMessage.length-1; i++) {
        	String res = "";
        	String res2 = "";
        	Element cham2 = summonerNameTemp.next();
        	res2 += cham2.text();
        	for(int j=0;j<3;j++) {
        		Element cham = ChampionTemp.next();
        		res += cham.text()+" / ";
        	}
        	//제거용
        	for(int j=0; j<7; j++) {
        		Element cham = ChampionTemp.next();
        	}
        	int point = point(main_tier[i]);
        	
        	result_text += ("★"+res2+"★\n최근평점 :"+kda[i]+" 선호포지션 :"+position[i]+"\n솔랭점수 :"+main_tier[i]+") 내전점수 :"+point+"\n");
        	result_text += ("모스트 챔피언 :"+res+"\n\n");
        	
        }
        
		return result_text;
	}
}