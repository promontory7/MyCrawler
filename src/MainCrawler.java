import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import model.Project;
import pageprocessor.GuangdongProcessor;
import pageprocessor.Lixinji;
import pageprocessor.NBANewsProcessor;
import pageprocessor.ShanTouProcessor;
import pageprocessor.ZhongshanProcessor;
import pageprocessor.ZhuHaiProcessor;
import pageprocessoruncomplate.FoShangProcessor;
import pageprocessoruncomplate.GuangDongTransportationProcessor;
import pageprocessoruncomplate.GuanghzouPublicResourceProcessor;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;

public class MainCrawler {

	public static void main(String[] args) {
		// Spider.create(new
		// GuangdongProcessor()).addUrl(GuangdongProcessor.url).thread(5).run();

		// Spider.create(new ZhuHaiProcessor()).addUrl(ZhuHaiProcessor.url)
		// .thread(5)
		// .run();

		Spider.create(new ShanTouProcessor()).addUrl(ShanTouProcessor.url).thread(5).run();
//		Spider.create(new NBANewsProcessor()).addUrl(NBANewsProcessor.url).thread(5).run();
//		Spider.create(new Lixinji()).addUrl(Lixinji.url).addPipeline(new JsonFilePipeline("/Users/hehe")).thread(5).run();

		// Spider.create(new
		// ZhongshanProcessor()).addUrl(ZhongshanProcessor.url)
		// .thread(5).run();

		// Spider.create(new
		// GuanghzouPublicResourceProcessor()).addUrl(GuanghzouPublicResourceProcessor.test)
		// .thread(5).run();

		// Spider.create(new FoShangProcessor()).addUrl(FoShangProcessor.url)
		// .thread(5).run();
		//
		// System.setProperty("javax.net.ssl.trustStore",
		// "/System/Library/Frameworks/JavaVM.framework/Versions/Current/Commands/java/jre/lib/security/");
		//
		// Spider.create(new ShenzhenProcessor()).addUrl(ShenzhenProcessor.url)
		// .run();

		// SessionFactory sf = new
		// Configuration().configure().buildSessionFactory();
		// Session s = null;
		// Transaction t = null;
		//
		// try {
		// // 准备数据
		// Project um = new Project();
		// um.setProjectName("测试");
		// um.setProjectNo("15641641641");
		// s = sf.openSession();
		// t = s.beginTransaction();
		// s.save(um);
		// t.commit();
		// } catch (Exception err) {
		// t.rollback();
		// err.printStackTrace();
		// } finally {
		// s.close();
		// }

	}

}
