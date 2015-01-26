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

		/** 列表 */
		private static final String LIST = "/app/service/";

		/** 详情 */
		private static final String INFO = "/app/service/info/";

		/** 检测版本 */
		private static final String VERSION = "/app/version/";

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

	}

	/**
	 * 消息类型
	 */
	public static class WHAT {
		/** 成功 */
		public static final int COMPLETED = 1;
		/** 失败 */
		public static final int FAILURE = 0;

	}
}
