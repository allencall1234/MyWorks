package com.xj.dms.https;

public class HttpUrl
{
	public static String SERVICE_IP = "192.168.1.34";
//	public static String SERVICE_IP = "192.168.1.11";
	public static String PORT = "8084";
//	public static String PORT = "8080";
	public static String SERVICE_NAME = "jzdms";
	public static String SERVICE_URL = "http://" + SERVICE_IP + ":" + PORT + "/" + SERVICE_NAME;
	
	/** 用户登录  */
	public static String USER_LOGIN_URL = SERVICE_URL + "/mobileLogin.html?";

	//获取电网信息
	public static String NETPOWER_INFO_URL = SERVICE_URL + "/mobileGridInfo.html";
	//获取电网计划
	public static String GET_PLAN_INFO_URL = SERVICE_URL + "/mobilePlan.html?";
	//获取通知公告
	public static String GER_NOTICE_URL = SERVICE_URL + "/mobileNotice.html?";
	//修改用户密码
	public static String ALERT_USER_PWD_URL = SERVICE_URL + "/mobileModifyPassword.html?";

	/** 待审批任务查询  */
	public static String UNAPPROVAL_TASK_QUERY_URL = SERVICE_URL + "/mobileTaskApproveList.html?";
	
	/** 已审批任务查询  */
	public static String APPROVALED_TASK_QUERY_URL = SERVICE_URL + "/mobileTaskApprovedList.html?";
	
	/** 任务审批 */
	public static String TASK_APPROVAL_URL = SERVICE_URL + "/mobileTaskApprove.html?";
	
	/** 任务查询  */
	public static String TASK_QUERY_URL = SERVICE_URL + "/mobileTaskList.html?";
	
	/** 任务详情查询  */
	public static String TASK_DETAILS_QUERY_URL = SERVICE_URL + "/mobileTaskDetail.html?";
	
	/** 任务统计查询  */
	public static String TASK_STATISTICAL_QUERY_URL = SERVICE_URL + "/mobileTaskCount.html?";
	
	/** 知识库查询 */ 
	public static String KNOWLEDGE_BASE_QUERY_URL = SERVICE_URL + "/mobileKnowledgeList.html?";
	
	/** 知识库详情查询 */
	public static String KNOWLEDGE_BASE_DETAILS_QUERY_URL = SERVICE_URL + "/mobileKnowledgeDetail.html?";
	
	/** 故障追溯查询 */
	public static String FAULT_TRACE_QUERY_URL = SERVICE_URL + "/mobileShutdownList.html?";
	
	/** 故障追溯详情查询 */
	public static String FAULT_TRACE_DETAILS_QUERY_URL = SERVICE_URL + "/mobileShutdownDetail.html?";
}
