package com.nbtoptec.symbol.status;

import java.awt.Component;

public class Snapshot {
	
	
	//记录右键菜单对应的组件
	public  Component c;
	
	/**
	 * 元件大类名字
	 */
	public  String groupName;
	
	/**
	 * 当前画布中打开的元件Id
	 */
	public  String symbolId;
	
	/**
	 * 元件名
	 */
	public  String symbolName;
	
	//元件状态id号,默认为0状态
	public  String statusId = "0";
	
	/**
	 * 元件状态名
	 */
	public  String statusName;
	
	//操作命令
	public  String command;
	
	//在面板中的顺序
	public int seqid;
}
