package pageprocessoruncomplate;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.Project;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import utils.HibernateUtil;
import utils.MyUtils;

public class ChaozhouProcessor implements PageProcessor {

	public static final String URL_DETAILS = "http://www\\.czggzy\\.com/t\\.j\\.jsp\\?id=\\d+\\&fs\\=GG";

	public static String url = "http://www.czggzy.com/l.j.jsp?j=GG";
	public static String test = "http://www.czggzy.com/t.j.jsp?id=555&fs=GG";
	private Site site = Site.me().setRetryTimes(3).setTimeOut(20000).setSleepTime(300);

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		if (page.getUrl().toString().trim().equals(url)) {
			List<String> urls = page.getHtml().xpath("//tr[@class=\"trstyle\"]").links().regex(URL_DETAILS).all();
			System.out.println("从列表页获取的详情数目" + urls.size());
			if (urls != null && urls.size() > 0) {
				for (int i = 0; i < urls.size(); i++) {
					MyUtils.addRequestToPage(page, urls.get(i));
				}
			}
		}
		if (page.getUrl().regex(URL_DETAILS).match()) {
			Document doc = Jsoup.parse(page.getHtml().toString());
			Element div = doc.getElementById("text_content");

			String projectName = doc.getElementsByAttributeValue("class", "STYLE1").get(0).text();
			String projectPublicStart = doc.getElementsByAttributeValue("class", "info").text().substring(5, 15);

			StringBuffer project_article = new StringBuffer();
			Elements divs = div.select("div");
			for (Element td : divs) {
				project_article.append(td.text()).append("\n");
			}

			Project project = new Project();
			project.setProjectName(projectName);
			project.setPublicStart(projectPublicStart);
			project.setUrl(page.getUrl().toString().trim());
			project.setState(0);
			project.setWebsiteType("潮州市");
			project.setTime(MyUtils.getcurentTime());
			project.setArticle(project_article.toString());
			project.setRawHtml(div.toString());
			System.out.println(project);

			HibernateUtil.save2Hibernate(project);
		}

	}

}
