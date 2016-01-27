package com.xj_pipe.https;

import java.io.File;
import java.util.HashMap;

import android.util.Log;

public class UserHttp extends HttpParam
{
	static HashMap<String, Object> list;
	
	/** 用户登录 */
	public static void Login(String userName, String passWord, HttpDataCallBack callBack)
	{
		list = new HashMap<String,Object>();
		list.put("userName", userName);
		list.put("password", passWord);
		
		String params = GetParams(list);// 拼接参数
		HttpConnection.getStringFromUrl(HttpURL.USER_LOGIN_URL + params, callBack);
	}
	
	/** 密码修改 */
	public static void modifyPassword(String userId, String oldPass, String newPass, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		list.put("staffId", userId);
		list.put("oldPwd", oldPass);
		list.put("newPwd", newPass);
		
		String params = GetParams(list);
		HttpConnection.getStringFromUrl(HttpURL.MODIFY_PASSWORD_URL + params, callBack);
	}
	
	/** 例行巡检任务列表查询  state :状态     0：表示未巡检   1：巡检中   2：暂时退出   3：已完成(历史记录)  4：异常结束*/
	
	/**
	 * 巡检任务统一查询接口
	 * @param order "desc"表示降序,"asc"表示升序
	 * @param curragePage 当前页码
	 * @param num	每页最多获取内容条数
	 * @param sort  排序字段
	 * @param id	当前用户id
	 * @param state	任务状态,0:表示未巡检   1:巡检中   2:暂时退出   3:已完成(历史记录)  4:异常结束
	 * @param taskType 任务类型 1:表示例行巡检任务,2:表示应急巡检任务
	 * @param callBack
	 */
	public static void routineInspectionTaskQuery(String order, int curragePage, int num, String sort, String id, String state, int taskType, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		list.put("order", order);
		list.put("page", curragePage);
		list.put("rows", num);
		list.put("sort", sort);
		list.put("staffId", id);
		list.put("state", state);
		list.put("taskType", taskType);
		
		String params = GetParams(list);// 拼接参数
		HttpConnection.getStringFromUrl(HttpURL.ROUTINE_INSPECTION_TASK_QUERY_URL + params, callBack);
		
	}
	
//	/** 应急巡检任务列表查询  */
//	public static void emergencyInspectionTaskQuery(String order, int currentPage, int num, String sort, int state, HttpDataCallBack callBack)
//	{
//		list = new HashMap<String, Object>();
//		
//		list.put("order", order);
//		list.put("page", currentPage);
//		list.put("rows", num);
//		list.put("sort", sort);
//		list.put("state", state);
//		
//		String params = GetParams(list);// 拼接参数
//		HttpConnection.getStringFromUrl(HttpURL.EMERGENCY_INSPECTION_TASK_QUERY_URL + params, callBack);
//	}
	
	/** 例行巡检任务详情查询 */
	public static void routineInspectionTaskDetailsQuery(int taskId, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		
		list.put("id", taskId);
		
		String params = GetParams(list);// 拼接参数
		HttpConnection.getStringFromUrl(HttpURL.EMERGENCY_INSPECTION_TASK_QUERY_URL + params, callBack);
	}
	
	/** 应急巡检任务详情查询 */
	public static void emergencyInspectionTaskDetailsQuery(int taskId, HttpDataCallBack callBack)
	{
        list = new HashMap<String, Object>();
		
		list.put("id", taskId);
		
		String params = GetParams(list);// 拼接参数
		HttpConnection.getStringFromUrl(HttpURL.EMERGENCY_INSPECTION_TASK_QUERY_URL + params, callBack);
	}
	
	/** 巡检图片上传 */
	public static void inspectionPhotoUpload(int taskId, File[] inspectionFile, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		
		list.put("id", taskId); 
		list.put("file", inspectionFile); 
		
		String params = GetParams(list);
		HttpConnection.getStringFromUrl(HttpURL.INSPECTION_PHOTO_UPLOAD_URL, callBack);
	}
	
	/** 图片文件是否存在 */
	public static void photoIsRepeat(String taskId, String imageName, HttpDataCallBack callBack)
	{
        list = new HashMap<String, Object>();
		
		list.put("taskId", taskId); 
		list.put("pictureName", imageName); 
		
		String params = GetParams(list);
		HttpConnection.getStringFromUrl(HttpURL.INSPECTION_PHOTO_UPLOAD_URL + params, callBack);
	}
	
	/** 巡检签到 */
	public static void inspectionPointSign(String inspectionId, String isSign, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		
		list.put("templatePointSetId", inspectionId); 
		list.put("signType", isSign); 
		
		String params = GetParams(list);
		
		HttpConnection.getStringFromUrl(HttpURL.INSPECTION_POINT_SIGN_URL + params, callBack);
	}
	
	/** 巡检任务提交 */
	public static String inspectionTaskSubmit(String taskId, String feedBack)
	{
		list = new HashMap<String, Object>();
		
		list.put("id", taskId);
		list.put("feedBackInfo", feedBack);
		
		String params = HttpURL.INSPECTION_TASK_SUBMIT_URL + GetParams(list);
		
		return params;
	}
	
	/** 事故报修提交 */
	public static String accidentHandlingSubmit(String place, String lineId, String existProblem, int faulttype, String repairTime, String reportId, 
			String content, String isDealWith, String longitude, String latitude)
	{
		list = new HashMap<String, Object>();
		
		list.put("place", place); 
		list.put("lineId", lineId); 
		list.put("existProblem", existProblem); 
		list.put("defectType", faulttype); 
		list.put("defectTime", repairTime); 
		list.put("report", reportId); 
		list.put("content", content); 
		list.put("flag", isDealWith); 
		list.put("longitude", longitude); 
		list.put("latitude", latitude); 
		
		String params = HttpURL.ACCIDENT_HANDLING_SUBMIT_URL + GetParams(list);
		
		Log.i("cdy", "事故报修 " + params);
		return params;
	}
	
	/** 事故报修历史记录查询 */
	public static void accidentHandlingHistoryQuery(int currentPage, int num, String sort, String order, String staffId, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		
		list.put("page", currentPage); 
		list.put("rows", num); 
		list.put("sort", sort); 
		list.put("order", order); 
		list.put("report", staffId); 
		
		String params = GetParams(list);
		
		HttpConnection.getStringFromUrl(HttpURL.ACCIDENT_HANDLING_HISTORY_QUERY_URL + params, callBack);
	}
	
	/** 查询设备编号 */
	public static void equipmentNumberQuery(String lineId, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		
		list.put("lineId", lineId);
		
		String params =  GetParams(list);
		
		HttpConnection.getStringFromUrl(HttpURL.EQUIPMENT_NUMBER_QUERY_URL + params, callBack);
		Log.i("cdy", "查询设备编号" + HttpURL.EQUIPMENT_NUMBER_QUERY_URL + params);
	}
	
	/** 突发事件提交 */
	public static String emergencySubmit(String title, String area, String happenDate, String longitude, String latitude, String description, String creater)
	{
		list = new HashMap<String, Object>();
		
		list.put("title", title); 
		list.put("area", area); 
		list.put("happenDate", happenDate); 
		list.put("longitude", longitude); 
		list.put("latitude", latitude); 
		list.put("description", description); 
		list.put("creater", creater); 
		
		String params = HttpURL.EMERGENCY_SUBMIT_URL + GetParams(list);
		Log.i("cdy", "突发事件 " + params);
		
		return params;
	}
	
	/** 突发事件记录查询 */
	public static void emergencyHistoryQuery(int currentPage, int num, String sort, String order, String staffId, HttpDataCallBack callBack)
	{
        list = new HashMap<String, Object>();
		
		list.put("page", currentPage); 
		list.put("rows", num); 
		list.put("sort", sort); 
		list.put("order", order); 
		list.put("creater", staffId); 
		
		String params = GetParams(list);
		
		HttpConnection.getStringFromUrl(HttpURL.EMERGENCY_HISTORY_QUERY_URL + params, callBack);
	}
	
	/** 施工反馈提交 */
	public static void buildFeedBackSubmit(String buildDepartment, String area, String beginTime, String endTime, 
			String isFire, String longitude, String latitude, String creater,HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		
		list.put("unit", buildDepartment); 
		list.put("area", area); 
		list.put("beginTime", beginTime); 
		list.put("endTime", endTime); 
		list.put("isFire", isFire); 
		list.put("longitude", longitude); 
		list.put("latitude", latitude); 
		list.put("creater", creater); 
		
		String params = GetParams(list);
		
		HttpConnection.getStringFromUrl(HttpURL.BUILD_FEEDBACK_SUBMIT_URL + params, callBack);
	}
	
	/** 施工反馈列表(历史反馈记录)查询 */
	public static void buildFeedBackHistoryQuery(int currentPage, int num, String sort, String order, String staffId, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		
		list.put("page", currentPage); 
		list.put("rows", num); 
		list.put("sort", sort); 
		list.put("order", order); 
		list.put("creater", staffId); 
		
		String params = GetParams(list);
		
		HttpConnection.getStringFromUrl(HttpURL.BUILD_FEEDBACK_HISTORY_QUERY_URL + params, callBack);
	}
	
	/** 消息列表查询 */
	public static void messageQuery(String order,int page,int rows,String sort,String receiver,HttpDataCallBack callBack)
	{
//		order=asc&page=1&rows=10&sort=id&receiver=46
		list = new HashMap<String, Object>();
		list.put("order", order);
		list.put("page", page);
		list.put("rows", rows);
		list.put("sort", sort);
		list.put("receiver", receiver);
		
		String params = GetParams(list);
		
		HttpConnection.getStringFromUrl(HttpURL.MESSAGE_QUERY_URL+params, callBack);
	}
	
	/** 更新消息状态 */
	public static void updateMessageState(int id, int state, HttpDataCallBack callBack){
		list = new HashMap<String, Object>();
		list.put("id", id);
		list.put("state", state);
		
		String params = GetParams(list);
		HttpConnection.getStringFromUrl(HttpURL.UPDATE_MESSAGE_STATE_URL+params, callBack);
	}
	
	/** 联系人查询 */
	public static void contectPersonQuery(String staffId,HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		list.put("staffId", staffId);
		String params = GetParams(list);
		
		HttpConnection.getStringFromUrl(HttpURL.CONTECT_PERSON_QUERY_URL+params, callBack);
	}
	
	/** 发送消息 */
	public static void sendMessage(String creater,String createtime,String receiver,String content,int state,HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		list.put("creater", creater);
		list.put("createtime", createtime);
		list.put("receiver", receiver);
		list.put("content", content);
		list.put("state", state);
		String params = GetParams(list);
		
		HttpConnection.sendPost(HttpURL.SEND_MESSAGE_URL, params, callBack);
	}
	
	/**主界面数量提醒*/
	public static void getMainDot(String staffId,HttpDataCallBack callBack){
		list = new HashMap<String, Object>();
		list.put("staffId", staffId); 
		
		String params = GetParams(list);
		HttpConnection.getStringFromUrl(HttpURL.GET_MAIN_DOT_URL+params, callBack);
	}
	
	/**获取巡检要素*/
	public static void getInspectionFactor(HttpDataCallBack callBack){
		HttpConnection.getStringFromUrl(HttpURL.INSPECTION_ELEMENTS_QUERY_URL, callBack);
	}
	
	/**获取巡检点信息*/
	public static void getPipeDotInfo(String taskId, HttpDataCallBack callBack){
		list = new HashMap<String, Object>();
		list.put("taskId", taskId);
		
		String params = GetParams(list);
		HttpConnection.getStringFromUrl(HttpURL.GET_PIPE_DOT_INFO_UTL+params, callBack);
		
	}
	
	/**
	 * 提交巡检人员当前轨迹点
	 * @param taskId	正在巡检的任务id
	 * @param longtitude	当前位置经度
	 * @param latitude		当前位置纬度
	 * @param callBack
	 */
	public static void submitTrackPoint(String taskId,String longtitude,String latitude,HttpDataCallBack callBack){
		list = new HashMap<String, Object>();
		list.put("taskId", taskId);
		list.put("longtitude", longtitude);
		list.put("latitude", latitude);
		list.put("useStatus", 1);
		
		String params = GetParams(list);
		HttpConnection.getStringFromUrl(HttpURL.INSPECTION_PERSON_TRAJECTORY_SUBMIT_URL+params, callBack);
	}
	
	/**
	 * 开始巡检
	 * @param taskId 当前任务id
	 * @param callBack
	 */
	public static void startCheckPoint(String taskId,HttpDataCallBack callBack){
		list = new HashMap<String,Object>();
		list.put("taskId", taskId);
		
		String params = GetParams(list);
		HttpConnection.getStringFromUrl(HttpURL.START_CHECK_PIPE_URL+params,callBack);
	}
	
}
