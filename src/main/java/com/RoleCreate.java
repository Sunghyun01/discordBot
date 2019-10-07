package com;

import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RoleCreate  extends ListenerAdapter{
	
	public void onRoleCreate(RoleCreateEvent event) {
		//�� ���� �α�
		System.out.println(event.getRole().getName());
	}
	public void onRoleUpdateName(RoleUpdateNameEvent event) {
		//�̸����� �α�
		System.out.println(event.getOldName() + "=>" + event.getNewName());
	}
	public void onRoleDelete(RoleDeleteEvent event) {
		System.out.println("�� : "+event.getRole().getName() + "�� �����Ǿ����ϴ�");
	}
	public void addRole() {}
}
