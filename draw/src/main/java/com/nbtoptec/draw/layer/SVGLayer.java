package com.nbtoptec.draw.layer;


import org.w3c.dom.Element;

public class SVGLayer{
	/**
	 * 是否锁定
	 */
	private boolean isLock;
	/**
	 * 是否可见
	 */
	private boolean isVisible;
	/**
	 * 是否处于链接状态
	 */
	private boolean isLink;
	/**
	 * 是否处于编辑状态
	 */
	private boolean isEdit;
	/**
	 * 图层名称
	 */
	private String name;
	/**
	 * 图层内容
	 */
	private Element element;
	
	public boolean isLock() {
		return isLock;
	}
	public void setLock(boolean isLock) {
		this.isLock = isLock;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	public boolean isLink() {
		return isLink;
	}
	public void setLink(boolean isLink) {
		this.isLink = isLink;
	}
	public boolean isEdit() {
		return isEdit;
	}
	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}
	public String getName() {
		if(element != null)
		{
			name = element.getAttribute("layer");
		}
		return name;
	}
	public void setName(String name) {
		this.name = name;
		if(element != null)
		{
			element.setAttribute("layer", this.name);
		}
	}
	public void addChild(Element e){
		this.element.appendChild(e);
	}
	public void removeChild(Element e){
		this.element.removeChild(e);
	}
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}
}
