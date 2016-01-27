package com.xj.cnooc.biz;

import java.util.HashMap;
import java.util.Map;
/**
 * 业务处理
 * @author qinfan
 *
 * 2015-9-11
 */
public class HttpBizBase {
//	protected static HashMap<String,Object> GetParamsTable()
//	{
//		HashMap<String,Object> list=new HashMap<String,Object>();
//		
//		return list;
//	}
//	
	protected static String GetParams(HashMap<String,Object> parms)
	{
		String urlParms="";
		for(Map.Entry<String,Object> entry : parms.entrySet()){
			entry.getValue();
			String Key=entry.getKey();
			Object Value=entry.getValue();
			urlParms+=Key+"="+Value+"&";
		}
		return urlParms;
	}

}
