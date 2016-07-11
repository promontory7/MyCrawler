public class MainCrawler {

	public static void main(String[] args) {

		if (args != null && args.length == 3) {
			int hour = Integer.parseInt(args[0]);
			int minutes = Integer.parseInt(args[1]);
			int second = Integer.parseInt(args[2]);
			System.out.println("爬虫在每天   " + hour + ":" + minutes + ":" + second + "  启动");
			CrawlerTimerTask.StartCrawlerTask(hour, minutes, second);
		} else {
//			CrawlerTimerTask.StartCrawlerTask(02, 00, 00);
//			System.out.println("爬虫在每天   " + 02 + ":" + 00 + ":" + 00 + "  启动");
			CrawlerTask.startCrawlerTask();

		}
	}
}
