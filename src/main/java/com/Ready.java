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

public class Ready extends ListenerAdapter{
	StringBuffer result = new StringBuffer();
	String result_text = "";
	String errorMessage="";
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		User author = event.getAuthor();
		if(author.isBot()) return; //봇이면 진행 안함
		if(!event.getTextChannel().getId().contains("609715691972591637")) return; // 이 채널 아니면 안돌아감
		
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
				
				int findPlayerLength = 5-userChannerLenght;
				//보낼 채널 id
				event.getGuild().getTextChannelById("606035632451747850").sendMessage("["+userChannelName+"] 에서 ["+getMessage[1]+"] 하실분 "+(findPlayerLength)+"명을 구합니다\n"+userName+"님이 작성").queue();
			}catch(Exception e) {
				event.getChannel().sendMessage("보이스채널 내에서 사용할수 있습니다").queue();
				return;
			}
		}
	}
	
	public void guild(GuildController event, MessageReceivedEvent e) {
		event.addRolesToMember(e.getMember(), event.getJDA().getRolesByName("개발자", true));
	}
}