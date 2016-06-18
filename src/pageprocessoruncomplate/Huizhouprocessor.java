package pageprocessoruncomplate;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.Project;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import utils.CacheHashMap;
import utils.HibernateUtil;
import utils.MyUtils;

public class Huizhouprocessor implements PageProcessor {

	public static final String URL_LIST = "http://zyjy\\.huizhou\\.gov\\.cn/pages/cms/hzggzyjyzx/html/artList*";
	public static final String URL_DETAILS = "http://zyjy\\.huizhou\\.gov\\.cn/pages/cms/hzggzyjyzx/html/zbgg*";

	public static String url = "http://zyjy.huizhou.gov.cn/pages/cms/hzggzyjyzx/html/artList.html?sn=hzggzyjyzx&cataId=49b0a00aec704a028cf4ad8678aceb7e&pageNo=1";

	public static String test = "http://zyjy.huizhou.gov.cn/pages/cms/hzggzyjyzx/html/zbgg_xq/ec4fe0184fa54eb99f83b1b544b40bb1.html?cataId=471c7e155621488e801de49132dc25da";

	private Site site = Site.me().setRetryTimes(3).setTimeOut(20000).setSleepTime(300);
	private static boolean isFirst = true;

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		Document doc = Jsoup.parse(page.getHtml().getFirstSourceText());

		if (isFirst) {
			System.out.println("添加所有列表链接");
			ArrayList<String> urls = new ArrayList<String>();
			urls.add(
					"http://zyjy.huizhou.gov.cn/pages/cms/hzggzyjyzx/html/artList.html?sn=hzggzyjyzx&cataId=471c7e155621488e801de49132dc25da&pageNo=2");
			// 30
			for (int i = 2; i < 30; i++) {
				urls.add(
						"http://zyjy.huizhou.gov.cn/pages/cms/hzggzyjyzx/html/artList.html?sn=hzggzyjyzx&cataId=49b0a00aec704a028cf4ad8678aceb7e&pageNo="
								+ i);
				urls.add(
						"http://zyjy.huizhou.gov.cn/pages/cms/hzggzyjyzx/html/artList.html?sn=hzggzyjyzx&cataId=471c7e155621488e801de49132dc25da&pageNo="
								+ i);
			}
			page.addTargetRequests(urls);
			isFirst = false;
		}

		if (page.getUrl().regex(URL_LIST).match()) {
			System.out.println("获取列表数据");

			Elements uls = doc.getElementById("div_list").getElementsByAttributeValue("class", "ul_art_row");
			for (Element ul : uls) {
				String url = ul.getElementsByAttributeValue("class", "li_art_title").get(0).select("a").attr("href").trim();
				String title = ul.getElementsByAttributeValue("class", "li_art_title").get(0).text();
				String data = ul.getElementsByAttributeValue("class", "li_art_date").get(0).text();
				CacheHashMap.cache.put(url, title + "###" + data);
				System.out.println(url + "   " + CacheHashMap.cache.get(url));
				page.addTargetRequest(url);
			}
		}

		if (page.getUrl().regex(URL_DETAILS).match()) {
			Project project = new Project();
			StringBuffer project_article = new StringBuffer();
			System.out.println("======="+page.getUrl()+"===========");
			String[] value = CacheHashMap.cache.get(page.getUrl().toString().trim()).split("###");
			if (value != null && value.length > 1) {
				project.setProjectName(value[0]);
				project.setPublicStart(value[1]);
			}

			Element divzoom = doc.getElementById("divZoom");
			for (Element p : divzoom.children()) {
				project_article.append(p.text()).append("\n");
			}
			
			String  rawhtml =doc.getElementById("div_view").toString();
			project.setUrl(page.getUrl().toString().trim());
			project.setState(0);
			project.setWebsiteType("惠州市");
			project.setTime(MyUtils.getcurentTime());
			project.setRawHtml(rawhtml);

			project.setArticle(project_article.toString());
			System.out.println(project);

			HibernateUtil.save2Hibernate(project);

		}

	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}
}
