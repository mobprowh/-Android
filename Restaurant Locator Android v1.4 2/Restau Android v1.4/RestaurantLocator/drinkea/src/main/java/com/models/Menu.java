package com.models;

public class Menu {
	
	private int menuResTitle;
	private int menuResIconSelected;
	private int menuResIconUnselected;
	private HeaderType headerType;
	
	public enum HeaderType {
		HeaderType_CATEGORY,
		HeaderType_LABEL
	}
	
	public Menu(int menuResTitle, int menuResIconSelected, int menuResIconUnselected, HeaderType headerType) {
		this.menuResTitle = menuResTitle;
		this.menuResIconSelected = menuResIconSelected;
		this.menuResIconUnselected = menuResIconUnselected;
		this.headerType = headerType;
	}
	
	public HeaderType getHeaderType() {
		return headerType;
	}
	
	public void setHeaderType(HeaderType headerType) {
		this.headerType = headerType;
	}
	
	
	public int getMenuResTitle() {
		return menuResTitle;
	}
	
	public int getMenuResIconSelected() {
		return menuResIconSelected;
	}
	
	public int getMenuResIconUnselected() {
		return menuResIconUnselected;
	}
	
	public void setMenuResTitle(int menuResTitle) {
		this.menuResTitle = menuResTitle;
	}
	
	public void setMenuResIconSelected(int menuResIconSelected) {
		this.menuResIconSelected = menuResIconSelected;
	}
	
	public void setMenuResIconUnselected(int menuResIconUnselected) {
		this.menuResIconUnselected = menuResIconUnselected;
	}
}
