package com.xj.dms.https;

import java.lang.reflect.Field;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.json.JSONObject;

import android.annotation.SuppressLint;

public class HttpJsonUtils 
{
	/**
	 * 将一个jsonObject对象数据封装到对应的bean对象中
	 * @param json
	 * @param wholeBeanName
	 * @return
	 */
	public static Object putJsonToObject(JSONObject jsonObj,String wholeBeanName)
	{
		Object result = null;
		try 
		{
			// 根据类名来实例一个类对象
			Object user_obj = Class.forName(wholeBeanName).newInstance();
			// 获取这个实例化的类中的所有信息
			Field[] fs = user_obj.getClass().getDeclaredFields();
			putJSONToBean(user_obj, fs, jsonObj);
			result = user_obj;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressLint("DefaultLocale")
	private static void putJSONToBean(Object user_obj, Field[] fs,
			JSONObject jsonObj)
	{
		try 
		{
			for (Field f : fs) 
			{
				f.setAccessible(true);
				System.out.println(f.getName() + "\t\t" + f.getType());
				if (f.getType().toString().endsWith("String")
						|| f.getType().toString().endsWith("string")) 
				{
					f.set(user_obj, jsonObj.optString(f.getName()));
				} 
				else if (f.getType().toString().endsWith("int")
						|| f.getType().toString().endsWith("Integer"))
				{
					f.setInt(user_obj, jsonObj
							.optInt(f.getName()));
				}
				else if (f.getType().toString().endsWith("double")
						|| f.getType().toString().endsWith("Double")) 
				{
					f.setDouble(user_obj, jsonObj.optDouble(f.getName()));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 时间格式转换
	 */
	public static String getTime()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");       
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
		String send_time = sdf.format(curDate); 
		
		return send_time;
	}


	
}
