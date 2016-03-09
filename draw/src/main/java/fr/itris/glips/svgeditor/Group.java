package fr.itris.glips.svgeditor;

import java.util.ArrayList;

public class Group {
	
	private String name;
	
	ArrayList<EUnit> list = new ArrayList<EUnit>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
