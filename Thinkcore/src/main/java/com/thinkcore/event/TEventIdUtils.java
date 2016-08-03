package com.thinkcore.event;

public class TEventIdUtils {
	public final static boolean Debug_Or_Release_App = true; // true调式、false发布版

	private static int indexEvent = 0;
	// 核心事件
	private final static int CoreEvent = getNextEvent(); // 跳转

	public final static int Success = getNextEvent(), Failure = getNextEvent(); // 任务中的成功/失败
	public final static int Before = getNextEvent(), Cancel = getNextEvent(),
			Update = getNextEvent(), Work = getNextEvent(); // 任务中的开始 取消 更新 工作中

	public final static int InProgress = getNextEvent(); // 运行中
	public final static int NetworkChange = getNextEvent(); // 网络状态改变

	public static int getNextEvent() {
		return indexEvent++;
	}
}
