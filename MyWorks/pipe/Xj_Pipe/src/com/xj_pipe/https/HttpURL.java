package com.xj_pipe.https;

public class HttpURL 
{
	public static final String SERVICE_IP = "218.17.162.229";// 内网IP
//	public static final String SERVICE_IP = "192.168.1.23";
//	public static final String SERVICE_IP = "192.168.1.52";
//	public static final String SERVICE_IP = "192.168.1.160";
//	public static final String PORT = "8093";
//	public static final String PORT = "8084";
//	public static final String PORT = "8080";
//	public static final String PORT = "8081";
	public static final String PORT = "8103";
	
	public static final String SERVICE_NAME = "pis";
	
	/** 请求网址  */
	public static final String SERVICE_URL = "http://" + SERVICE_IP + ":" + PORT + "/" + SERVICE_NAME;
	
	/** 用户登录 */
	public static final String USER_LOGIN_URL = SERVICE_URL + "/appLogin.action?";
	
	/** 首页未读信息提示数量*/
	public static final String GET_MAIN_DOT_URL = SERVICE_URL +"/appIndex.action?";
	
	/** 修改密码 */
	public static final String MODIFY_PASSWORD_URL = SERVICE_URL + "/appUpdatePassword.action?";
	
	/** 例行巡检任务查询  */
	public static final String ROUTINE_INSPECTION_TASK_QUERY_URL = SERVICE_URL + "/taskForMobil/queryTaskListForMobil.action?";
	
	/** 应急巡检任务查询  */
	public static final String EMERGENCY_INSPECTION_TASK_QUERY_URL = SERVICE_URL + "/taskForMobil/queryTaskListForMobil.action?";

	/** 例行巡检任务详情查询  */
	public static final String ROUTINE_INSPECTION_TASK_DETAILS_QUERY_URL = SERVICE_URL + "/taskForMobil/queryTaskById.action?";
	
	/** 应急巡检任务详情查询 */
	public static final String EMERGENCY_INSPECTION_TASK_DETAILS_QUERY_URL = SERVICE_URL + "/taskForMobil/queryTaskById.action?";
	
	/** 巡检要素查询 */
	public static final String INSPECTION_ELEMENTS_QUERY_URL = SERVICE_URL + "/appRoutMonitor/polingFactorAll.action?";
	
	/** 巡检图片上传 */
	public static final String INSPECTION_PHOTO_UPLOAD_URL = SERVICE_URL + "/taskForMobil/uploadPicture1.action?";
	
	/** 图片是否存在 */
	public static final String PHOTO_IS_REPEAT_URL = SERVICE_URL + "/taskForMobil/judgePicture.action?";
	
	/** 巡检点签到  */
	public static final String INSPECTION_POINT_SIGN_URL = SERVICE_URL + "/taskForMobil/taskSignIn.action?";
	
	/** 巡检任务提交 */
	public static final String INSPECTION_TASK_SUBMIT_URL = SERVICE_URL + "/taskForMobil/taskComplate.action?";
	
	/** 巡检人员轨迹提交 */
	public static final String INSPECTION_PERSON_TRAJECTORY_SUBMIT_URL = SERVICE_URL + "/appRoutMonitor/polingTaskTrack.action?";
	
	/** 事故报修 */
	public static final String ACCIDENT_HANDLING_SUBMIT_URL = SERVICE_URL + "/appRepair/saveAppRepairData.action?";
	
	/** 事故报修历史记录查询 */
	public static final String ACCIDENT_HANDLING_HISTORY_QUERY_URL = SERVICE_URL + "/appRepair/appQueryRepairList.action?";
	
	/** 查询设备编号 */
	public static final String EQUIPMENT_NUMBER_QUERY_URL = SERVICE_URL + "/appRepair/getAppEquipList.action?";
	
	/** 突发事件 */
	public static final String EMERGENCY_SUBMIT_URL = SERVICE_URL + "/appAddEventOrder.action?";
	
	/** 突发事件历史记录查询 */
	public static final String EMERGENCY_HISTORY_QUERY_URL = SERVICE_URL + "/appQueryEventOrderList.action?";
	
	/** 施工反馈 */
	public static final String BUILD_FEEDBACK_SUBMIT_URL = SERVICE_URL + "/appAddConFeedback.action?";
	
	/** 施工反馈列表查询(历史) */
	public static final String BUILD_FEEDBACK_HISTORY_QUERY_URL = SERVICE_URL + "/appQueryConFeedbackList.action?";
	
	/** 消息列表查询 */
	public static final String MESSAGE_QUERY_URL = SERVICE_URL + "/appMessage/queryMessageList.action?";
	
	/** 更新消息状态*/
	public static final String UPDATE_MESSAGE_STATE_URL = SERVICE_URL + "/appMessage/appMessageUpdate.action?";
	
	/** 消息详情查询*/
	public static final String MESSAGE_DETAILS_QUERY_URL = SERVICE_NAME + "/appMessage/queryMessageById.action?";
	
	/** 联系人查询 */
	public static final String CONTECT_PERSON_QUERY_URL = SERVICE_URL + "/appQueryStaffList.action?";
	
	/** 发送消息 */
	public static final String SEND_MESSAGE_URL = SERVICE_URL + "/appMessage/appMessageListSave.action?";
	
	/** 获取巡检点信息*/
	public static final String GET_PIPE_DOT_INFO_UTL = SERVICE_URL + "/taskForMobil/queryTaskPointSetById.action?";

	/**开始巡检*/
	public static final String START_CHECK_PIPE_URL = SERVICE_URL + "/taskForMobil/startTask.action?";
}
