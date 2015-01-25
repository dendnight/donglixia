package com.denghb.donglixia;

public final class Constants {

	private Constants() {
	}

	public static final String URL_SERVER = "http://donglixia.sinaapp.com/app/service/";
	
	// 配置
	public static class Config 
	{
		public static final boolean DEVELOPER_MODE = true;
	}
	
	// 传值
	public static class Extra 
	{
		/** ID */
		public static final String ID = "ID";

		/** URLS */
		public static final String URLS = "URLS";
		
		/** position */
		public static final String IMAGE_POSITION = "IMAGE_POSITION";

	}

	// 消息WHAT
	public static class WHAT 
	{
		/** 成功 */
		public static final int COMPLETED = 1;
		/** 失败 */
		public static final int FAILURE = 0;

	}
}
