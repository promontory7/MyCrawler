import pageprocessor.ZhongshanProcessor;
import pageprocessoruncomplate.FoShangProcessor;
import pageprocessoruncomplate.GuangDongTransportationProcessor;
import pageprocessoruncomplate.GuanghzouPublicResourceProcessor;
import us.codecraft.webmagic.Spider;

public class MainCrawler {

	public static void main(String[] args) {
		// Spider.create(new
		// GuangdongProcessor()).addUrl(GuangdongProcessor.url)
		// .run();

		// Spider.create(new ZhuHaiProcessor()).addUrl(ZhuHaiProcessor.url)
		// .thread(5)
		// .run();
		//

//		Spider.create(new ZhongshanProcessor()).addUrl(ZhongshanProcessor.url)
//		.thread(5).run();

		
//		Spider.create(new GuanghzouPublicResourceProcessor()).addUrl(GuanghzouPublicResourceProcessor.test)
//		.thread(5).run();
		
		Spider.create(new FoShangProcessor()).addUrl(FoShangProcessor.url)
		.thread(5).run();
		
		// System.setProperty("javax.net.ssl.trustStore",
		// "/System/Library/Frameworks/JavaVM.framework/Versions/Current/Commands/java/jre/lib/security/");
		//
		// Spider.create(new ShenzhenProcessor()).addUrl(ShenzhenProcessor.url)
		// .run();
	}

}
