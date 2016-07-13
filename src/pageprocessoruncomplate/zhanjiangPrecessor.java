package pageprocessoruncomplate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class zhanjiangPrecessor implements PageProcessor{
	
		public static String url = "http://zb.zjcic.net/Default.aspx?tabid=95";

		public static String test = "http://zb.zjcic.net/Default.aspx?tabid=61&ArticleID=3435";

		private Site site = Site.me().setRetryTimes(3).setTimeOut(20000).setSleepTime(100);

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	@Override
	public void process(Page page) {
		Document doc = Jsoup.parse(page.getHtml().toString());

		// TODO Auto-generated method stub
		if (page.getUrl().toString().trim().equals(url)) {
			Element div =doc.getElementById("dnn_ctr513_ArticleList_PanelA");
			page.addTargetRequest(new Request(){});
			
		}else {
			
		}
	}

}
