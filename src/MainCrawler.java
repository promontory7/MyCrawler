
import pageprocessoruncomplate.DongguanProcessor;
import pageprocessoruncomplate.FoShangProcessor;
import pageprocessoruncomplate.GuangDongTransportationProcessor;
import pageprocessoruncomplate.GuangdongProcessor;
import pageprocessoruncomplate.GuanghzouPublicResourceProcessor;
import pageprocessoruncomplate.HeyuanProcessor;
import pageprocessoruncomplate.Huizhouprocessor;
import pageprocessoruncomplate.MaomingProcessor;
import pageprocessoruncomplate.MeizhouProcessor;
import pageprocessoruncomplate.QingyuanProcessor;
import pageprocessoruncomplate.ShaoguangProcessor;
import pageprocessoruncomplate.YangjiangProcessor;
import pageprocessoruncomplate.ZhaoqingPrecessor;
import pageprocessoruncomplate.jiangmenProcessor;
import us.codecraft.webmagic.Spider;

public class MainCrawler {

	public static void main(String[] args) {

//		Spider.create(new GuangdongProcessor()).addUrl(GuangdongProcessor.url).thread(5).run();
//
//		Spider.create(new GuangDongTransportationProcessor()).addUrl(GuangDongTransportationProcessor.url).thread(5)
//				.run();
//
//		Spider.create(new Huizhouprocessor()).addUrl(Huizhouprocessor.url).thread(5).run();
//
//		Spider.create(new YangjiangProcessor()).addUrl(YangjiangProcessor.url).thread(5).run();
//
//		Spider.create(new MaomingProcessor()).addUrl(MaomingProcessor.url).thread(5).run();
//
//		Spider.create(new ZhaoqingPrecessor()).addUrl(MaomingProcessor.url).thread(5).run();
//
//		Spider.create(new QingyuanProcessor()).addUrl(QingyuanProcessor.url).thread(5).run();
//
//		Spider.create(new jiangmenProcessor()).addUrl(jiangmenProcessor.url).thread(5).run();
//
//		Spider.create(new GuanghzouPublicResourceProcessor()).addUrl(GuanghzouPublicResourceProcessor.url).thread(5)
//				.run();

//		Spider.create(new MeizhouProcessor()).addUrl(MeizhouProcessor.url).thread(5).run();

//		Spider.create(new ShaoguangProcessor()).addUrl(ShaoguangProcessor.url).thread(5).run();
//
//		Spider.create(new HeyuanProcessor()).addUrl(HeyuanProcessor.url).thread(5).run();
//
//		Spider.create(new FoShangProcessor()).addUrl(FoShangProcessor.url).thread(5).run();

		
		Spider.create(new DongguanProcessor()).addUrl(DongguanProcessor.url).thread(5).run();
	}

}
