package com.libraries.enums;

public class ENUMS {

	
	public static enum FAVORITES {
		FAVORITES_ADD,
		FAVORITES_REMOVE,
		FAVORITES_LIST,
		FAVORITES_NONE
	};
	
	public static enum TAB_1 {
		
		FRAGMENT_TAB_1_CATEGORY {
		    public String toString() {
		        return "FRAGMENT_TAB_1_CATEGORY";
		    }
		},
	};
}
