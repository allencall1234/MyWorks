package com.xj.cnooc.https;

public class HttpURL {
//	public static String CHAT_SERVICE_IP = "192.168.1.34";
	public static String CHAT_SERVICE_IP = "218.17.162.229";
	public static String SERVICE_IP = "218.17.162.229";
//	public static String SERVICE_IP = "192.168.1.59";
//	public static int CHAT_PORT = 8906;
	public static int CHAT_PORT = 8096;
	public static int PORT = 8096;
//	public static int PORT = 8080;
//	public static int PORT = 8081;
	public static String SERVICE_NAME = "ogrp";
	public static String SERVICE_URL = "http://" + SERVICE_IP + ":" + PORT + "/" + SERVICE_NAME+"/";
	
	
//	public static String USER_LOGIN_URL="http://192.168.1.36:8080/MySchool/UserServlet?";
	
    /** 用户登录操作方法 */
	public static String USER_LOGIN_URL = SERVICE_URL + "/phoneLogin.html?";
	
	/** 论坛版块技术区帖子数量等信息查询 */
	public static String QUERY_BBS_URL = SERVICE_URL + "/phoneBbsModuleList.html?";
	
	/** 故障支持首页数量查询 */
	public static String QUERY_FAULT_SUPPORT_UNREAD_NUM_URL = SERVICE_URL + "/phoneHitchCount.html?";
	
	/** 评估支持首页数量查询 */
	public static String QUERY_FAULT_EVALUATE_UNREAD_NUM_URL = SERVICE_URL + "/phoneAssessmentCount.html?";
	
	/** 知识库首页知识数量查询 */
	public static String QUERY_KNOWLEDGE_BASE_UNREAD_NUM_URL = SERVICE_URL + "/phoneRecordCount.html?";
	
	/** 论坛首页数量查询 */
	public static String QUERY_BBS_UNREAD_NUM_URL = SERVICE_URL + "/phoneBbsCount.html?";
	
	/** 帖子列表url */
	public static String QUERY_BBS_POST_LIST_URL = SERVICE_URL + "/PhoneBbsPostList.html?";
	
	/** 已评论帖子列表URL*/
	public static String QUERY_COMMENTED_POST_LIST_URL = SERVICE_URL + "/PhoneDoDealBbsPost.html?";
	
	/** 论坛帖子回复列表Url */
	public static String QUERY_REPLY_POST_LIST_URL = SERVICE_URL + "/phoneBbsReplyList.html?";
	 
	/** 我的收藏帖子列表URL*/
	public static String QUERY_COLLECTION_POST_LIST_URL = SERVICE_URL + "/phoneBbsStoreList.html?";
	
	/** 收藏帖子 */
	public static String COLLECTION_POST_URL = SERVICE_URL + "/phoneSaveBbsStore.html?";
	
	/** 删除我收藏的帖子 */
	public static String DELETE_COLLECTION_POST_URL = SERVICE_URL + "/phoneDelBbsStore.html?";
	
	/** 保存帖子回复的内容 */
	public static String SAVE_POST_REPLY_CONTENT_URL = SERVICE_URL + "/phoneSavePostReply.html?";
	
	/** 下载附件url */
	public static String DOWNLOAD_FILE_URL = SERVICE_URL + "/phoneDownload.html?";
	
	/**故障支持列表URL*/
	public static String FAULT_SUPPORT_LIST_URL=SERVICE_URL+"/phoneHitchSupportList.html?";
	
	/**故障评估支持*/
	public static String FAULT_EVALUATE_LIST_URL=SERVICE_URL+"/phoneAssessmentSupportList.html?";
	
	/**已回复故障支持详情*/
	public static String FINISH_FAULT_DETAIL_LIST_URL=SERVICE_URL+"/phoneHitchSupportDetail.html?";
	
	/**已回复评估支持详情*/
	public static String FINISH_EVALUATE_DETAIL_LIST_URL=SERVICE_URL+"/phoneAssessmentSupportDetail.html?";

	/**提交故障审批*/
	public static String SUBMIT_FAULT_SUPPORT_DATA_URL=SERVICE_URL+"/phoneSaveHitchContent.html?";
	
	/**提交评估审批*/
	public static String SUBMIT_EVALUATE_DATA_URL=SERVICE_URL+"/phoneSaveAssessmentContent.html?";
	
	/**电网知识库列表*/
	public static String KNOWLEDGE_BASE_LIST_DATA_URL=SERVICE_URL+"/phoneKnowledgeList.html?";
	
	/**电网知识库详情*/
	public static String KNOWLEDGE_BASE_DETAIL_URL=SERVICE_URL+"/phoneKnowledgeDetail.html?";
}
