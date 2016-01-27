package com.xj.dms.https;

import java.util.HashMap;

public class UserHttp extends HttpBase
{
	static HashMap<String, Object> list;
	
	/** 登录 */
	public static void Login(String userName,String passWord,HttpDataCallBack callBack){
		list = new HashMap<String,Object>();
		list.put("username", userName);
		list.put("password", passWord);
		
		String params = GetParams(list);// 拼接参数
		HttpConnection.getStringFromUrl(HttpUrl.USER_LOGIN_URL+params.replaceAll(" ", "%20"),callBack);
	}
	
	/** 获取电网信息*/
	public static void getNetPowerInfo(HttpDataCallBack callBack){
		HttpConnection.getStringFromUrl(HttpUrl.NETPOWER_INFO_URL,callBack);
	}
	
	/** 获取电网计划*/
	public static void getNetPowerPlan(int page, int rows,HttpDataCallBack callBack){
		list = new HashMap<String,Object>();
		list.put("page", page);
		list.put("rows", rows);
		
		String params = GetParams(list);// 拼接参数
		HttpConnection.getStringFromUrl(HttpUrl.GET_PLAN_INFO_URL+params, callBack);
	}
	
	/** 获取电网计划*/
	public static void getNetPowerNotice(int page, int rows,HttpDataCallBack callBack){
		list = new HashMap<String,Object>();
		list.put("page", page);
		list.put("rows", rows);
		
		String params = GetParams(list);// 拼接参数
		HttpConnection.getStringFromUrl(HttpUrl.GER_NOTICE_URL+params, callBack);
	}
	
	/** 任务查询 */
	public static void taskQuery(int curragePage, int num, String taskName, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		
		list.put("page", curragePage);
		list.put("rows", num);
		list.put("tname", taskName);
		
		// 拼接请求参数
		String params = GetParams(list);
		HttpConnection.getStringFromUrl(HttpUrl.TASK_QUERY_URL + params,  callBack);
		
	}
	
	/** 知识库查询 */
	public static void knowledgeBaseQuery(int curragePage, int num, String keyword, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		
		list.put("page", curragePage);
		list.put("rows", num);
		list.put("keyword", keyword);
		
		String params = GetParams(list);
		HttpConnection.getStringFromUrl(HttpUrl.KNOWLEDGE_BASE_QUERY_URL + params,  callBack);
	}
	
	/** 故障查询 */
	public static void faultTraceQuery(int curragePage, int num, String index, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		
		list.put("page", curragePage);
		list.put("rows", num);
		list.put("breakdownIndex", index);
		
		String params = GetParams(list);
		HttpConnection.getStringFromUrl(HttpUrl.FAULT_TRACE_QUERY_URL + params, callBack);
	}
	
	/** 待审批任务查询 */
	public static void unApprovalTaskQuery(int curragePage, int num, String taskName, int loginId, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		
		list.put("page", curragePage);
		list.put("rows", num);
		list.put("tname", taskName);
		list.put("userid", loginId);
		
		String params = GetParams(list);
		HttpConnection.getStringFromUrl(HttpUrl.UNAPPROVAL_TASK_QUERY_URL + params,  callBack);
	}
	
	/** 已审批任务查询  */
	public static void approvaledTaskQuery(int curragePage, int num, String taskName, int loginId, HttpDataCallBack callBack)
	{
        list = new HashMap<String, Object>();
		
		list.put("page", curragePage);
		list.put("rows", num);
		list.put("tname", taskName);
		list.put("userid", loginId);
		
		String params = GetParams(list);
		HttpConnection.getStringFromUrl(HttpUrl.APPROVALED_TASK_QUERY_URL + params,  callBack);
	}
	
	/** 任务详情查询 */
	public static void taskDetailsQuery(int taskId , HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		
		list.put("id", taskId);
		
		String params = GetParams(list);
		HttpConnection.getStringFromUrl(HttpUrl.TASK_DETAILS_QUERY_URL + params, callBack);
	}
	
	/** 任务统计 */
	public static void taskStatisticalQuery(int curragePage, int num, String keyword, HttpDataCallBack callBack)
	{
        list = new HashMap<String, Object>();
		
		list.put("page", curragePage);
		list.put("rows", num);
		list.put("keyword", keyword);
		
		String params = GetParams(list);
		HttpConnection.getStringFromUrl(HttpUrl.TASK_STATISTICAL_QUERY_URL + params,  callBack);
	}
	
	/** 任务审批 */
	public static void taskApproval(int taskId, String opinion, String isAgree, int loginId, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		
		list.put("id", taskId);
		list.put("comment", opinion);
		list.put("isAgree", isAgree);
		list.put("userid", loginId);
		
		String params = GetParams(list);
		HttpConnection.getStringFromUrl(HttpUrl.TASK_APPROVAL_URL + params, callBack);
	}
	
	/**修改密码*/
	public static void alertUserPwd(String oldPsd, String newPsd, int userid, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		
		list.put("oldPsd", oldPsd);
		list.put("newPsd", newPsd);
		list.put("userid", userid);
		
		String params = GetParams(list);
		HttpConnection.getStringFromUrl(HttpUrl.ALERT_USER_PWD_URL + params, callBack);
	}
}
