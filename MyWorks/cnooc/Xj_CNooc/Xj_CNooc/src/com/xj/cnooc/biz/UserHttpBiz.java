package com.xj.cnooc.biz;

import java.util.HashMap;

import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.https.HttpDataCallBack;
import com.xj.cnooc.https.HttpURL;
import com.xj.cnooc.https.HttpUntilImpl;

public class UserHttpBiz extends HttpBizBase{
	static HashMap<String,Object> list;
	
	/** 登录 */
	public static void Login(String userName,String passWord,HttpDataCallBack callBack){
		list = new HashMap<String,Object>();
		list.put("staffname", userName);
		list.put("pwd", passWord);
		
		String params = GetParams(list);// 拼接参数
		HttpUntilImpl.getStringFromUrl(HttpURL.USER_LOGIN_URL+params.replaceAll(" ", "%20"),callBack);
	}
	
	/**查询论坛版块 */
	public static void queryBBS(int loginId, HttpDataCallBack callBack)
	{

		list = new HashMap<String,Object>();
		list.put("accountid", loginId);
		
		String params = GetParams(list);// 拼接参数
		HttpUntilImpl.getStringFromUrl(HttpURL.QUERY_BBS_URL + params.replaceAll(" ", "%20"), callBack);
	}
	
	/** 查询帖子列表 **/
	public static void queryPostList(int curragePage, int num, int bbsId, String post_title, String poster, HttpDataCallBack callBack)
	{
		list = new HashMap<String,Object>();
		list.put("page", curragePage);
		list.put("rows", num);
		list.put("bbsModuleId", bbsId);
		list.put("title", post_title);
		list.put("createname", poster);
		
		String params = GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.QUERY_BBS_POST_LIST_URL + params.replaceAll(" ", "%20"), callBack);
	}
	
	/** 查询已评论的帖子列表 **/
	public static void queryCommentedPostList(int curragePage, int num, int loginId, HttpDataCallBack callBack)
	{
		list = new HashMap<String,Object>();
		list.put("page", curragePage);
		list.put("rows", num);
		list.put("accountid", loginId);
		
		String params = GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.QUERY_COMMENTED_POST_LIST_URL + params.replaceAll(" ", "%20"), callBack);
	}
	
	/** 查询回复的内容 */
	public static void queryReplyPostList(int curragePage, int num, int postId, int loginId, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		list.put("page", curragePage);
		list.put("rows", num);
		list.put("bbsPostId", postId);
		list.put("accountid", loginId);
		
		String params = GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.QUERY_REPLY_POST_LIST_URL + params.replaceAll(" ", "%20"), callBack);
	}
	
	/** 收藏帖子 */
	public static void collectionPost(int postId, int loginId, HttpDataCallBack callBack)
	{
		list = new HashMap<String,Object>();
		list.put("bbsPostId", postId);
		list.put("accountid", loginId);
		
		String params = GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.COLLECTION_POST_URL + params.replaceAll(" ", "%20"), callBack);
	}
	
	/** 查询我的收藏帖子列表*/ 
	public static void queryCollectionPostList(int curragePage, int num, int loginId, HttpDataCallBack callBack)
	{
		list = new HashMap<String,Object>();
		list.put("page", curragePage);
		list.put("rows", num);
		list.put("accountid", loginId);
		
		String params = GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.QUERY_COLLECTION_POST_LIST_URL + params.replaceAll(" ", "%20"), callBack);
	}
	
	/** 删除我收藏的帖子 */
	public static void deleteCollectionPost(int postId, int loginId, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		list.put("bbsPostId", postId);
		list.put("accountid", loginId);
		
		String params = GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.DELETE_COLLECTION_POST_URL + params.replaceAll(" ", "%20"), callBack);
	}
	
	/** 保存帖子回复内容 */
	public static void savePostReplyContent(int postId, String replyContent, int loginId, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		list.put("bbspost_id", postId);
		list.put("content", replyContent);
		list.put("creater", loginId);
		String params = GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.SAVE_POST_REPLY_CONTENT_URL + params.replaceAll(" ", "%20"), callBack);
	}
	
	public static void downloadFile(String downloadPath, String fileName, HttpDataCallBack callBack)
	{
		list = new HashMap<String, Object>();
		list.put("downLoadPath", downloadPath);
		list.put("fileName", fileName);
		
		String params = GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.DOWNLOAD_FILE_URL + params.replaceAll(" ", "%20"), callBack);
	}
	
	/**
	 * 故障支持列表
	 */
	public static void getFaultSupportListData(int curragePage,int num,int factoryUser,int status,HttpDataCallBack callBack){
		list=new HashMap<String,Object>();
		list.put("page", curragePage);
		list.put("rows", num);
//		list.put("type", type);
		list.put("status", status);
		String rolename=MyApp.globelUser.getRolename();
		if(rolename.equals("系统专家")){
			list.put("proficientId", factoryUser);
		}
		if(rolename.equals("厂家支持人员")){
			list.put("factoryUser", factoryUser);
		}
		String params=GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.FAULT_SUPPORT_LIST_URL+params.replaceAll(" ", "%20"),callBack);
	}
	
	/**
	 * 故障评估列表
	 */
	public static void getFaultEvaluateListData(int curragePage,int num,int factoryUser,int status,HttpDataCallBack callBack){
		list=new HashMap<String,Object>();
		list.put("page", curragePage);
		list.put("rows", num);
//		list.put("type", type);
		list.put("status", status);
//		String rolename=MyApp.globelUser.getRolename();
//		if(rolename.equals("系统专家")){
			list.put("proficientId", factoryUser);
//		}
//		if(rolename.equals("厂家支持人员")){
//			list.put("factoryUser", factoryUser);
//		}
		String params=GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.FAULT_EVALUATE_LIST_URL+params.replaceAll(" ", "%20"),callBack);
	}
	
	/**
	 * 已回复故障支持详情
	 */
	public static void getFinishFaultDetailData(String id,HttpDataCallBack callBack){
		list = new HashMap<String,Object>();
		list.put("id", id);
		String params=GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.FINISH_FAULT_DETAIL_LIST_URL+params.replaceAll(" ", "%20"),callBack);
	}
	
	/**
	 * 已回复评估支持详情
	 */
	public static void getFinishEvaluateDetailData(String id,HttpDataCallBack callBack){
		list = new HashMap<String,Object>();
		list.put("id", id);
		String params=GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.FINISH_EVALUATE_DETAIL_LIST_URL+params.replaceAll(" ", "%20"),callBack);
	}
	
	/**故障支持提交审批*/
	public static void submitFaultSupportData(int id,int status,String approvalContent,String title,int accountId,HttpDataCallBack callBack){
		list = new HashMap<String, Object>();
		list.put("id", id);
		list.put("status", status);
		list.put("approvalContent", approvalContent);
		list.put("title", title);
		list.put("accountid", accountId);
		String params=GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.SUBMIT_FAULT_SUPPORT_DATA_URL+params.replaceAll(" ", "%20"), callBack);
	}
	
	/**评估支持提交审批*/
	public static void submitEvaluateSupportData(int id,int status,String approvalContent,String title,int accountId,HttpDataCallBack callBack){
		list = new HashMap<String, Object>();
		list.put("id", id);
		list.put("status", status);
		list.put("approvalContent", approvalContent);
		list.put("title", title);
		list.put("accountid", accountId);
		String params=GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.SUBMIT_EVALUATE_DATA_URL+params.replaceAll(" ", "%20"), callBack);
	}
	
	/**电网知识库列表*/
	public static void getKnowledgeBseListData(int curragePage,int num,String title,HttpDataCallBack callBack){
		list = new HashMap<String, Object>();
		list.put("page", curragePage);
		list.put("rows", num);
		list.put("title", title);
		String params=GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.KNOWLEDGE_BASE_LIST_DATA_URL+params.replaceAll(" ", "%20"), callBack);
	}
	
	/**电网知识库详情*/
	public static void getKnowledgeBaseDetailData(String baseType,int id,HttpDataCallBack callBack){
		list = new HashMap<String, Object>();
		if(baseType.equals("1")){
			list.put("id", id);
			String params=GetParams(list);
			HttpUntilImpl.getStringFromUrl(HttpURL.FINISH_FAULT_DETAIL_LIST_URL+params.replaceAll(" ", "%20"), callBack);
		}
		if(baseType.equals("2")){
			list.put("id", id);
			String params=GetParams(list);
			HttpUntilImpl.getStringFromUrl(HttpURL.FINISH_EVALUATE_DETAIL_LIST_URL+params.replaceAll(" ", "%20"), callBack);
		}
		if(baseType.equals("3")){
			list.put("id", id);
			list.put("type", "1");
			String params=GetParams(list);
			HttpUntilImpl.getStringFromUrl(HttpURL.KNOWLEDGE_BASE_DETAIL_URL+params.replaceAll(" ", "%20"), callBack);
		}
		if(baseType.equals("4")){
			list.put("id", id);
			list.put("type", "2");
			String params=GetParams(list);
			HttpUntilImpl.getStringFromUrl(HttpURL.KNOWLEDGE_BASE_DETAIL_URL+params.replaceAll(" ", "%20"), callBack);
		}
	}
	
	/** 故障支持首页数量 */
	public static void getFaultSupportNum(int loginId, String login_type, HttpDataCallBack callBack)
	{
		list = new HashMap<String,Object>();
		list.put("accountid", loginId);
		list.put("type", login_type);
		
		String params = GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.COLLECTION_POST_URL + params, callBack);
	} 
	
	/** 评估支持首页数量 */
	public static void getFaultEvaluateNum(int loginId, String login_type, HttpDataCallBack callBack)
	{
		list = new HashMap<String,Object>();
		list.put("accountid", loginId);
		list.put("type", login_type);
		
		String params = GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.COLLECTION_POST_URL + params, callBack);
	} 
	
	/** 知识库首页数量 */
	public static void getKnowledgeBaseNum(int knowledgeBaseId, int loginId, String login_type, HttpDataCallBack callBack)
	{
		list = new HashMap<String,Object>();
		list.put("id", knowledgeBaseId);
		list.put("accountid", loginId);
		list.put("type", login_type);
		
		String params = GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.COLLECTION_POST_URL + params, callBack);
	} 
	
	/** 论坛首页数量 */
	public static void getBBSNum(int loginId, int postId, HttpDataCallBack callBack)
	{
		list = new HashMap<String,Object>();
		list.put("accountid", loginId);
		list.put("bbsPostId", postId);
		
		String params = GetParams(list);
		HttpUntilImpl.getStringFromUrl(HttpURL.COLLECTION_POST_URL + params, callBack);
	} 
	
	
}
