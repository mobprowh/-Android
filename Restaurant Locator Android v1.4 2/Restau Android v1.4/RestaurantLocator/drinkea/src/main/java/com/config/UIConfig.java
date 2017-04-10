package com.config;

import com.models.Menu;
import com.project.drinkea.R;

public class UIConfig {

	public static int[] INNER_TAB_TITLE = {
		R.string.sub_tab_details,
		R.string.sub_tab_map,
		R.string.sub_tab_gallery,
	};
	
	public static int[] SELECTED_INNER_TAB_BG = {
		R.drawable.inner_tab_left_selected,
		R.drawable.inner_tab_left_selected,
		R.drawable.inner_tab_left_selected
	};
	
	public static int[] UNSELECTED_INNER_TAB_BG = {
		R.drawable.inner_tab_left,
		R.drawable.inner_tab_left,
		R.drawable.inner_tab_left
	};

	public static int DEFAULT_CATEGORY_COLOR = R.color.list_text_color;
	public static int THEME_COLOR = R.color.theme_color;

	public static int TAB_UNSELECTED_TEXT_COLOR = R.color.unselected_text_color;
	public static int TAB_SELECTED_TEXT_COLOR = R.color.theme_color;
	public static int BORDER_THICKNESS = 0;

	public static float BORDER_RADIUS = 10;
	public static float BORDER_WIDTH = 5;
	public static int SLIDER_PLACEHOLDER = R.drawable.placeholder;
	public static int RESTAURANT_LIST_PLACEHOLDER = R.drawable.restaurant_placeholder;
	public static int FAVE_ADD = R.drawable.ic_fave_add;
	public static int FAVE_REMOVE = R.drawable.ic_fave_remove;
	public static int FAVE_LIST = R.drawable.ic_fave;
	public static int LIST_ARROW_NORMAL = R.drawable.list_arrow;
	public static int LIST_ARROW_SELECTED = R.drawable.list_arrow_selected;

	public static Menu MENU_FAVE = new Menu(R.string.favorites, R.drawable.ic_fave, R.drawable.ic_fave, Menu.HeaderType.HeaderType_CATEGORY);
	public static Menu MENU_HOME = new Menu(R.string.home, R.drawable.ic_home, R.drawable.ic_home, Menu.HeaderType.HeaderType_CATEGORY);
	public static Menu MENU_CATEGORIES = new Menu(R.string.tab_categories, R.drawable.ic_restaurant, R.drawable.ic_restaurant, Menu.HeaderType.HeaderType_CATEGORY);
	public static Menu MENU_FEATURED = new Menu(R.string.tab_featured, R.drawable.ic_featured, R.drawable.ic_featured, Menu.HeaderType.HeaderType_CATEGORY);
	public static Menu MENU_MAP = new Menu(R.string.tab_map, R.drawable.ic_map, R.drawable.ic_map, Menu.HeaderType.HeaderType_CATEGORY);
	public static Menu MENU_SEARCH = new Menu(R.string.search, R.drawable.ic_search, R.drawable.ic_search, Menu.HeaderType.HeaderType_CATEGORY);
	public static Menu MENU_GALLERY = new Menu(R.string.galleries, R.drawable.ic_gallery, R.drawable.ic_gallery, Menu.HeaderType.HeaderType_CATEGORY);
	public static Menu HEADER_CATEGORIES = new Menu(R.string.menu, -1, -1, Menu.HeaderType.HeaderType_LABEL);

	public static Menu[] MENUS_NOT_LOGGED = {
			HEADER_CATEGORIES,
			MENU_HOME,
			MENU_CATEGORIES,
			MENU_FAVE,
			MENU_FEATURED,
			MENU_MAP,
			MENU_SEARCH,
			MENU_GALLERY,
	};
}
