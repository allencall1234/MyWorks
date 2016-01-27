package com.xj.dms.https;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求参数拼接
 *
 */
public class HttpBase 
{
	protected static String GetParams(HashMap<String,Object> parms)
	{
		String urlParms="";
		for(Map.Entry<String,Object> entry : parms.entrySet())
		{
			String Key = entry.getKey();
			Object Value = entry.getValue();
			urlParms += Key + "=" + Value + "&";
		}
		return urlParms;
	}

}
