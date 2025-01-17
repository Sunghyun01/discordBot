package com;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.channels.Channel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.GuildController;
import net.dv8tion.jda.api.requests.restaction.InviteAction;
import oracle.net.aso.a;
import com.util.*;

public class Ready extends ListenerAdapter{
	StringBuffer result = new StringBuffer();
	String result_text = "";
	String errorMessage="";
	String lastMessage = "";
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		User author = event.getAuthor();
		if(author.isBot()) return; //봇이면 진행 안함
		if(!event.getTextChannel().getId().contains("636186383622602782")) return; // 이 채널 아니면 안돌아감
		
		Message msg = event.getMessage();
		String messageContent = msg.getContentRaw();
		int errorCode = -1;
		
		if(messageContent.equals("명령어")) {
			event.getChannel().sendMessage(title.help_text()).queue();	
		}else if(messageContent.contains("디비") ) {
			result_text = DBConnection.showDB(messageContent);
			event.getChannel().sendMessage("```"+result_text+"```").queue();
		}else if(messageContent.contains("전적")) {
			String getMessage[] = messageContent.split(" ");
			String user = "";
			for(int i=1;i<getMessage.length;i++) {
				user += getMessage[i];
			}
			result_text = jsoup.Record(user);
			event.getChannel().sendMessage("```"+result_text+"```").queue();
		}else if(messageContent.contains("멀티")) {
			String getMessage[] = messageContent.split(" ");
			result_text = jsoup.MultiRecord(getMessage);
			event.getChannel().sendMessage("```"+result_text+"```").queue();
		}else if(messageContent.contains("데이터")) {
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ("yyyy.MM.dd", Locale.KOREA );
			Date currentTime = new Date();
			String today = mSimpleDateFormat.format ( currentTime );
			today = "'"+today+"'";
			
			String getMessage[] = messageContent.split(" ");
			String table = getMessage[1];
			String value="";
			for(int i=2; i<getMessage.length;i++) {
				value += "'"+getMessage[i]+"',";
			}
			//value = value.substring(0, value.length()-1); //맨뒤 컴마 지울거면 키셈
			String query = "insert into "+table+" values("+value+today+")";
			System.out.println(query);
			result_text = DBConnection.insertQuery(query);
			event.getChannel().sendMessage(result_text).queue();
		}else if(messageContent.contains("권한")) {
			String getMessage[] = messageContent.split(" ");
			//봇한테 상위역할 부여 해야함
			event.getGuild().getController().addRolesToMember(event.getMember(), event.getJDA().getRolesByName(getMessage[1], true)).complete();
			event.getChannel().sendMessage(event.getChannel().getName()+"한테 권한추가됨").queue();
		}else if(messageContent.contains("쿼리")) {
			String getMessage[] = messageContent.split(" ");
			String query = "";
			for(int i=1;i<getMessage.length;i++) {
				query += getMessage[i]+" ";
			}
			result_text = DBConnection.insertQuery(query);
			event.getChannel().sendMessage(result_text).queue();
		}else if(messageContent.equals("공지")) {
			result_text = jsoup.notice();
			event.getChannel().sendMessage(result_text).queue();
		}else if(messageContent.contains("검색")) {
			String getMessage[] = messageContent.split(" ");
			result_text = jsoup.search_board(getMessage[1]);
			event.getChannel().sendMessage(result_text).queue();
		}else if(messageContent.contains("인게임")) {
			String getMessage[] = messageContent.split(" ");
			result_text = jsoup.inGame(getMessage[1]);
			//event.getChannel().sendMessage(result_text).queue();
		}else if(messageContent.contains("구인")) {
			String getMessage[] = messageContent.split(" ");
			//보낸사람
			String userName = msg.getAuthor().getName();
			lastMessage = msg.getId();
			delMessage(event, lastMessage);
			try {
				//보낸사람이 보이스 채널에 있나?
				String userChannelName = msg.getMember().getVoiceState().getChannel().getName();
				//보낸사람이 있는 보이스채널
				List<VoiceChannel> c = event.getGuild().getVoiceChannelsByName(userChannelName, true);
				int userChannerLenght = c.size();
				//몇명 구인하는지
				
				int findPlayerLength = 5-userChannerLenght;
				//초대링크
				String link = event.getGuild().getVoiceChannelsByName(userChannelName, true).get(0).createInvite().complete().getUrl();
				//보낼 채널 id
				String bm = event.getGuild().getTextChannelById("631136953856688158").sendMessage("["+userChannelName+"] 에서 ["+getMessage[1]+"] 하실분 "+(findPlayerLength)+"명을 구합니다\n"+userName+"님이 작성\n"+link).complete().getId();
				
				DBConnection.insertQuery("insert into findMember values(fmsequence.nextval,'"+userChannelName+"','"+bm+"')");
			}catch(NullPointerException e) {
				event.getChannel().sendMessage("보이스채널 내에서 사용할 수 있습니다").queue();
				return;
			}
		}else if(messageContent.equals("마감")) {
			lastMessage = msg.getId();
			delMessage(event, lastMessage);
			//보낸사람이 보이스 채널에 있나?
			try {
				String userChannelName = msg.getMember().getVoiceState().getChannel().getName();
				
				String query = "select code from findMember where room ='"+userChannelName+"'";
				ResultSet res = DBConnection.sendQuery(query);
				res.next();
				findMemberDel(event, res.getString(1));
			}catch(NullPointerException e) {
				event.getChannel().sendMessage("보이스채널 내에서 사용하실 수 있습니다").queue();
				return;
			}catch(SQLException e) {
				event.getChannel().sendMessage("SQL : 잘못된 구분입니다\n보이스채널 내에 있으면 게시한 글이 있는지 확인해주세요").queue();
				return;
			}
		}
		else if(messageContent.contains("팀결정")) {
			String[] contentRow = msg.getContentRaw().split(" ");
			//방이름설정
			String[] room = {"롤1","롤2"};
			int roomNum = 0;
			
			TextChannel textChannel = msg.getTextChannel();
			Guild guild = textChannel.getGuild();
			
			GuildController gControl = new GuildController(guild);
			String mssageId = contentRow[1];
			String targetContent = textChannel.retrieveMessageById(mssageId).complete().getContentRaw();
			String[] targetRow = targetContent.split("\n");
			VoiceChannel temp = null;
			
			for(int i = 0,system = 1;i<targetRow.length;i++) {
				if(util.isNumber(targetRow[i].charAt(0))&&targetRow[i].charAt(1) == '팀') {
//					if(++system+1 > contentRow.length) {
//						event.getChannel().sendMessage("함수래퍼런스 부족").queue();
//						return;
//					}
					try {
						temp = guild.getVoiceChannelsByName(room[roomNum], true).get(0);
						event.getChannel().sendMessage(roomNum+1+"팀 배분중").queue();
					} catch (IndexOutOfBoundsException e) {
						event.getChannel().sendMessage("VoiceChannel Name 오류").queue();
						return;
					}
					roomNum++;
				}else {
					try {
						gControl.moveVoiceMember(guild.getMembersByNickname(targetRow[i],true).get(0),temp).queue();
						
					} catch (InsufficientPermissionException e) {
						event.getChannel().sendMessage("옮길수없음 >> "+targetRow[i]).queue();
						return;
					} catch (IndexOutOfBoundsException e) {
						event.getChannel().sendMessage("이름 제대로 >> "+targetRow[i]).queue();
						return;
					}catch(IllegalStateException e) {
						event.getChannel().sendMessage("대기방 들가라 >> "+targetRow[i]).queue();
						return;
					}
				}
			}
		}else if(messageContent.contains("데려와")) {
			String[] contentRow = msg.getContentRaw().split(" ");
			TextChannel textChannel = msg.getTextChannel();
			Guild guild = textChannel.getGuild();
			GuildController gControl = new GuildController(guild);
			String userChannelName = msg.getMember().getVoiceState().getChannel().getName();
			VoiceChannel temp =  event.getGuild().getVoiceChannelsByName(userChannelName, true).get(0);
			
			gControl.moveVoiceMember(guild.getMembersByNickname(contentRow[1],true).get(0),temp).queue();
			
		}else if(messageContent.contains("파일")) {
			File file = new File("D:\\Users\\조성현\\Downloads\\invoice.xls");
			long lastModify = file.lastModified();
			String pattern = "yyyy-MM-dd";
			SimpleDateFormat simpledateformet = new SimpleDateFormat(pattern);
			Date lastModifyDate = new Date(lastModify);
			event.getChannel().sendMessage("최근수정날짜 :"+simpledateformet.format(lastModifyDate)).addFile(file).queue();
		}else if(messageContent.equals("멤버")) {
			String userChannelName = msg.getMember().getVoiceState().getChannel().getName();
			List<VoiceChannel> c = event.getGuild().getVoiceChannelsByName(userChannelName, true);
			List<Member> a = c.get(0).getMembers();
			
			String memberList = "";
			for(int i = 0; i < a.size(); i++) {
				memberList += a.get(i).getNickname()+"\n";
			}
			event.getChannel().sendMessage("```참여자 목록\n"+memberList+"```").queue();
		}else if(messageContent.equals("동전")){
			lastMessage = event.getChannel().sendMessage("동전도는중 스탑을 해주세요").complete().getId();
		}else if(messageContent.equals("스탑")) {
			int randNum = (int) ((Math.random()*10)+1);
			String result = randNum == 1?"앞":"뒤";
			try {
				event.getChannel().editMessageById(lastMessage, "결과 : "+result).queue();
				
				lastMessage = "";
			}catch(IllegalArgumentException e) {
				event.getChannel().sendMessage("동전먼저 굴려라").queue();
			}
		}else if(messageContent.contains("삭제")) {
			String[] contentRow = msg.getContentRaw().split(" ");
			delMessage(event,contentRow[1]);
		}else if(messageContent.contains("지워")) {
			String[] contentRow = msg.getContentRaw().split(" ");
			String last = "";
			
		}
		
	}
	public void guild(GuildController event, MessageReceivedEvent e) {
		event.addRolesToMember(e.getMember(), event.getJDA().getRolesByName("개발자", true));
	}
	public void delMessage(MessageReceivedEvent event,String delId) {
		event.getChannel().deleteMessageById(delId).queue();
	}
	public void findMemberDel(MessageReceivedEvent event,String delId) {
		event.getGuild().getTextChannelById("631136953856688158").deleteMessageById(delId).queue();
		String query = "delete from findMember where code = '"+delId+"'";
		DBConnection.insertQuery(query);
	}
}
