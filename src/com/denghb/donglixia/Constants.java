package com.denghb.donglixia;

public final class Constants {

	private Constants() {
	}

	/**
	 * 服务地址
	 */
	public static class Server {
		/** 服务器 */
		private static final String HOST = "http://donglixia.sinaapp.com";
		//private static final String HOST = "http://192.168.1.102";

		/** 列表 */
		private static final String LIST = "/app/service/";

		/** 详情 */
		private static final String INFO = "/app/service/info/";

		/** 检测版本 */
		private static final String VERSION = "/app/version/";

		/** 主页 */
		private static final String HOME = "/app/";

		/**
		 * 列表
		 * 
		 * @param page
		 * @param tag
		 * @return
		 */
		public static String list(int page, String tag) {
			return HOST + LIST + "?p=" + page + "&tag=" + tag;
		}

		/**
		 * 详情
		 * 
		 * @param i
		 * @return
		 */
		public static String info(int i) {
			return HOST + INFO + "?i=" + i;
		}

		/**
		 * 版本信息
		 * 
		 * @return
		 */
		public static String version() {
			return HOST + VERSION;
		}

		/**
		 * 主页
		 * 
		 * @return
		 */
		public static String home() {
			return HOST + HOME;
		}

	}

	/**
	 * 传值
	 */
	public static class Extra {
		/** ID */
		public static final String ID = "ID";

		/** URLS */
		public static final String URLS = "URLS";

		/** position */
		public static final String IMAGE_POSITION = "IMAGE_POSITION";

		/** 文件夹名 */
		public static final String DIR_NAME = "DIR_NAME";
		
		/** 标题 */
		public static final String TITLE = "TITLE";

	}

	/**
	 * 消息类型{值唯一}
	 */
	public static class What {

		/** 检查版本 */
		public static final int VERSION = 0;
		
		/** 东篱下 */
		public static class Donglixia {
			/** 主列表 */
			public static final int LIST = 201;

			/** 详情 */
			public static final int INFO = 202;

		}
		/** 本地 */
		public static class Native{

			/** 文件夹列表 */
			public static final int GROUP = 101;

			/** 文件夹里面 */
			public static final int ITEM = 102;
		}
	}

	/**
	 * 消息标记
	 */
	public static class Status {
		/** 成功 */
		public static final int COMPLETED = 1;
		/** 失败 */
		public static final int FAILURE = 0;
	}

	/**
	 * json 返回数据key
	 */
	public static class JSON {
		/** 状态 */
		public static final String STATUS = "STATUS";

		/** 数据 */
		public static final String DATA = "DATA";

		/** 数量 */
		public static final String TOTAL = "TOTAL";

		/** 版本 */
		public static final String VERSION = "ANDROID_VERSION";

		/** 路径集合 */
		public static final String URLS = "URLS";

		/** 标签 */
		public static final String TAG = "TAG";

		/** id */
		public static final String ID = "ID";

		/** 喜欢 */
		public static final String LOVE = "LOVE";
	}
}
