package fr.itris.glips.svgeditor;

import java.util.ArrayList;

public class Group {
	
	private String groupName;
	
	ArrayList<EUnit> list = new ArrayList<EUnit>();


	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public ArrayList<EUnit> getList() {
		return list;
	}

	public void setList(ArrayList<EUnit> list) {
		this.list = list;
	}
	
	public void add(EUnit unit)
	{
		this.list.add(unit);
	}
}
