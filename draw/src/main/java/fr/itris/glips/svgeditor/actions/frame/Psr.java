package fr.itris.glips.svgeditor.actions.frame;

import java.util.ArrayList;
import java.util.List;

public class Psr {

	private int id;
	private String name;
	private int pid;
	private List<Psr> children = new ArrayList<Psr>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public List<Psr> getChildren() {
		return children;
	}

	public void setChildren(List<Psr> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
