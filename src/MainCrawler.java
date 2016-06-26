
import pageprocessor.ShanTouProcessor;
import pageprocessor.ZhongshanProcessor;
import pageprocessor.ZhuHaiProcessor;
import pageprocessoruncomplate.ChaozhouProcessor;
import pageprocessoruncomplate.DongguanProcessor;
import pageprocessoruncomplate.FoShangProcessor;
import pageprocessoruncomplate.GuangDongTransportationProcessor;
import pageprocessoruncomplate.GuangdongProcessor;
import pageprocessoruncomplate.GuanghzouPublicResourceProcessor;
import pageprocessoruncomplate.HeyuanProcessor;
import pageprocessoruncomplate.Huizhouprocessor;
import pageprocessoruncomplate.JieyangProcessor;
import pageprocessoruncomplate.MaomingProcessor;
import pageprocessoruncomplate.MeizhouProcessor;
import pageprocessoruncomplate.QingyuanProcessor;
import pageprocessoruncomplate.ShaoguangProcessor;
import pageprocessoruncomplate.ShundeProcessor;
import pageprocessoruncomplate.YangjiangProcessor;
import pageprocessoruncomplate.ZhaoqingPrecessor;
import pageprocessoruncomplate.jiangmenProcessor;
import us.codecraft.webmagic.Spider;

public class MainCrawler {

	public static void main(String[] args) {
		
		
//		Spider.create(new ZhongshanProcessor()).addUrl(ZhongshanProcessor.url).thread(5).run();
//
//		Spider.create(new ZhuHaiProcessor()).addUrl(ZhuHaiProcessor.url).thread(5).run();
//		
//		Spider.create(new ShanTouProcessor()).addUrl(ShanTouProcessor.url).thread(5).run();
//
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
//
//		Spider.create(new MeizhouProcessor()).addUrl(MeizhouProcessor.url).thread(5).run();
//
//		Spider.create(new ShaoguangProcessor()).addUrl(ShaoguangProcessor.url).thread(5).run();
//
//		Spider.create(new HeyuanProcessor()).addUrl(HeyuanProcessor.url).thread(5).run();
//
//		Spider.create(new FoShangProcessor()).addUrl(FoShangProcessor.url).thread(5).run();
//
//		Spider.create(new DongguanProcessor()).addUrl(DongguanProcessor.url).thread(5).run();
//
//		Spider.create(new ChaozhouProcessor()).addUrl(ChaozhouProcessor.url).thread(5).run();
//
//		
//		Spider.create(new JieyangProcessor()).addUrl(JieyangProcessor.url).thread(5).run();
//
		
		Spider.create(new ShundeProcessor()).addUrl(ShundeProcessor.url).thread(5).run();

	}

}
