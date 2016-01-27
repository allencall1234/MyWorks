package com.xj_pipe.utils;

import java.lang.reflect.Field;

import org.json.JSONObject;

import android.annotation.SuppressLint;

public class JsonUtils 
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
	
	/**
	 * 将一个jsonObject对象数据封装到对应的bean对象中(泛型方法实现)
	 * @param jObject	json对象
	 * @param clazz	类名
	 * @return
	 */
	public static <T> T putJsonToObject(JSONObject jObject,Class<T> clazz) {
		T object = null;
		
		try {
			object = clazz.newInstance();
			
			Field[] fs = clazz.getDeclaredFields();
			
			putJSONToBean(object, fs, jObject);
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return object;
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
//				System.out.println(f.getName() + "\t\t" + f.getType());
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

}
