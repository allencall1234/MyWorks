package com.xj.cnooc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserHelper 
{
	 private static UserHelper self;
	 private SharedPreferences mPreferences;

	 public static Context mContext;
	 
	 /** 单例模式  start **/
     public static UserHelper getInstance() 
     {
        return self;
     }
    
     private UserHelper()
     {
    	
     }
    
     private UserHelper(Context context) 
     {
        mContext = context;
        self = this;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
     }
     
     public static void init(Context context) {
	        self = new UserHelper(context);
	    }
     /** 单例模式  end **/
    
     public SharedPreferences getSharedPreferences() {
         return mPreferences;
     }

     public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.mPreferences = sharedPreferences;
    }
}

