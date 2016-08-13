import javax.xml.crypto.Data;

public class MainCrawler {

	public static void main(String[] args) {

		if (args != null && args.length == 3) {
			int hour = Integer.parseInt(args[0]);
			int minutes = Integer.parseInt(args[1]);
			int second = Integer.parseInt(args[2]);
			System.out.println("爬虫在每天   " + hour + ":" + minutes + ":" + second + "  启动");
			CrawlerTimerTask.StartCrawlerTask(hour, minutes, second);
			System.out.println("本次爬去完毕  " );
		}if (args != null && args.length == 1) {
			int hour = Integer.parseInt(args[0]);
			
			System.out.println("爬虫在每   " + hour + "  小时爬一次");
			CrawlerTimerTask.StartCrawlerTask(hour);
			System.out.println("本次爬去完毕，将会在  " + hour + "  小时后再启动    "+new Data() {
			});
		} else {
			System.out.println("爬虫在每  2 小时爬一次");
			CrawlerTimerTask.StartCrawlerTask(2);
			System.out.println("本次爬去完毕，将会在 2  小时后再启动    +new Data()");

		}
	}
}
