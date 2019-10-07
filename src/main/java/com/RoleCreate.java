package com;

import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RoleCreate  extends ListenerAdapter{
	
	public void onRoleCreate(RoleCreateEvent event) {
		//룰 생성 로그
		System.out.println(event.getRole().getName());
	}
	public void onRoleUpdateName(RoleUpdateNameEvent event) {
		//이름변경 로그
		System.out.println(event.getOldName() + "=>" + event.getNewName());
	}
	public void onRoleDelete(RoleDeleteEvent event) {
		System.out.println("룰 : "+event.getRole().getName() + "가 삭제되었습니다");
	}
	public void addRole() {}
}
