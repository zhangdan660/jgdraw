package fr.itris.glips.svgeditor;

import javax.swing.Icon;

public class DrawUnit {
	private String name="DrawUnit";
	private String category;
	private String shortDescription ; // used for tooltips
	private String id;
	private Icon icon;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getShortDescription() {
		return shortDescription;
	}
	
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Icon getIcon() {

		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}
}
