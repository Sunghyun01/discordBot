package com;

public class title {	
	public static String help_text() {
		String help_text = "```";
		help_text += "메로봇 사용 설명서\n\n";
		help_text += "전적 + [롤닉네임] : 전적 찾아줌\n";
		help_text += "멀티 + [롤닉네임] [롤닉네임] : 여러명 전적 검색가능 최대 9명\n";
		help_text += "공지 : 클랜 카페의 공지사항을 가져옴\n";
		help_text += "디비 + [테이블명] : 테이블 안에 데이터를 보여줌\n";
		help_text += "데이터 + [테이블명] + [데이터] : 데이터넣어주는거 \n";
		help_text += "구인 + [게임모드] : 보이스채널에서 가능 구인글 올려줌\n";
		help_text += "개발중인것들\n";
		help_text += "구인 - 인원수 조정기능\n";
		help_text += "디비 - 쿼리빌더\n";
		help_text += "검색 - 카페 내 게시글 검색 (오류많음)\n";
		help_text += "\nCopyright 2019 SHyun";
		help_text += "```";
		return help_text;
	}
}
