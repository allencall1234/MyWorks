package com.xj.cnooc.https;
/*
 * 回调
 */
public abstract class HttpDataCallBack {
	/*
	 * 通信成功
	 * _result:通信返回结果
	 * */
	public abstract void HttpSuccess(String _result);
	/*
	 * 通信失败*/
	public abstract void HttpFail(int ErrCode);
}
