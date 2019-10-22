package com;

import java.lang.reflect.Method;
import java.nio.channels.Channel;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		User author = event.getAuthor();
		if(author.isBot()) return; //봇이면 진행 안함
		if(!event.getTextChannel().getId().contains("631136929366147082")) return; // 이 채널 아니면 안돌아감
		
		Message msg = event.getMessage();
		String messageContent = msg.getContentRaw();
		int errorCode = -1;
		
		if(messageContent.indexOf("help") == 0 && messageContent.length() == 4) {
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
				event.getGuild().getTextChannelById("631136953856688158").sendMessage("["+userChannelName+"] 에서 ["+getMessage[1]+"] 하실분 "+(findPlayerLength)+"명을 구합니다\n"+userName+"님이 작성\n"+link).queue();
				
			}catch(Exception e) {
				event.getChannel().sendMessage("보이스채널 내에서 사용할수 있습니다").queue();
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
						
						event.getChannel().sendMessage("한번돔"+room[roomNum]+roomNum).queue();
					} catch (IndexOutOfBoundsException e) {
						event.getChannel().sendMessage("VoiceChannel Name 오류").queue();
						return;
					}
					roomNum++;
				}else {
					try {
						gControl.moveVoiceMember(guild.getMembersByNickname(targetRow[i],true).get(0),temp).queue();
						
					} catch (InsufficientPermissionException e) {
						event.getChannel().sendMessage("옮길수없음").queue();
						return;
					} catch (IndexOutOfBoundsException e) {
						event.getChannel().sendMessage("이름제대로 >> "+targetRow[i]).queue();
						return;
					}
				}
			}
		}
	}
	public void guild(GuildController event, MessageReceivedEvent e) {
		event.addRolesToMember(e.getMember(), event.getJDA().getRolesByName("개발자", true));
	}
}