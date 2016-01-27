package com.xj_pipe.https;

/**
 * 通信返回结果
 * 抽象类
 */
public abstract class HttpDataCallBack 
{
	// 通信成功
	public abstract void HttpSuccess(String _result);
	
    // 通信失败
	public abstract void HttpFail(int ErrCode);

}
