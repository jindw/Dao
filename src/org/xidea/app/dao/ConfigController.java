package org.xidea.app.dao;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ConfigController extends Application{
	private static SharedPreferences config;

	public void onCreate(){
		super.onCreate();
		config = this.getSharedPreferences("main", 0);
	}
	private static boolean get(String key,boolean defaultValue){
		return config.getBoolean(key, defaultValue);
	}
	private static void set(String key,boolean value){
		Editor editor = config.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	public static boolean isReading(){
		return get("reading", true);
	}
	public static void setReading(boolean reading){
		set("reading", reading);
	}

	public static boolean isAutoFlip(){
		return get("autoFlip", false);
	}
	public static void setAutoFlip(boolean autoFlip){
		set("autoFlip", autoFlip);
	}

	public static boolean isSourceOnly(){
		return get("sourceOnly", false);
	}
	public static void setSourceOnly(boolean sourceOnly){
		set("sourceOnly", sourceOnly);
	}
	public static int getSection(){
		return config.getInt("section", 0);
	}

	public static void setSection(int section){
		Editor editor = config.edit();
		editor.putInt("section", section);
		editor.commit();
	}
	
	

}
