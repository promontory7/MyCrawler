import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class CrawlerTimerTask {
	public static void StartCrawlerTask(int hour,int minute,int second) {
		// 得到时间类
		Calendar date = Calendar.getInstance();
		// 设置时间为 xx-xx-xx 00:00:00
		date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), hour, minute, second);
		// 一天的毫秒数
		long daySpan = 24 * 60 * 60 * 1000;
		// 得到定时器实例
		Timer t = new Timer();
		// 使用匿名内方式进行方法覆盖
		t.schedule(new TimerTask() {
			public void run() {
				CrawlerTask.startCrawlerTask();
			}
		}, date.getTime(), daySpan);
	};
}
