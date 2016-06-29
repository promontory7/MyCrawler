package pageprocessoruncomplate;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import model.Project;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import utils.HibernateUtil;
import utils.MyUtils;

public class JieyangProcessor implements PageProcessor {

	public static final String URL_LIST = "http://www\\.jysggzy\\.com/web/newsListAction\\!oneNewsList\\.shtml\\?theID\\=76\\&pager\\.offset\\=\\d+";
	public static final String URL_DETAILS = "http://www\\.jysggzy\\.com/web/webContentsAction\\!annShow\\.shtml\\?theID\\=\\d+\\&catalogPid\\=7";

	public static String url = "http://www.jysggzy.com/web/newsListAction!oneNewsList.shtml?theID=76&pager.offset=0";
	public static String test = "http://www.jysggzy.com/web/webContentsAction!annShow.shtml?theID=405&catalogPid=7";
	private Site site = Site.me().setRetryTimes(3).setTimeOut(20000).setSleepTime(300);
	private static boolean isFirst = true;

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub

		if (isFirst) {

			ArrayList<String> urls = new ArrayList<String>();
			// 150
			for (int i = 15; i < 80; i += 15) {
				urls.add("http://www.jysggzy.com/web/newsListAction!oneNewsList.shtml?theID=76&pager.offset=" + i);
			}
			System.out.println("添加所有列表链接      " + urls.size());
			page.addTargetRequests(urls);
			isFirst = false;
		}
		if (page.getUrl().regex(URL_LIST).match()) {
			List<String> urls = page.getHtml().xpath("//div[@class=\"box\"]").links().regex(URL_DETAILS).all();
			System.out.println("从列表页获取的详情数目" + urls.size());
			if (urls != null && urls.size() > 0) {
				for (int i = 0; i < urls.size(); i++) {
					MyUtils.addRequestToPage(page, urls.get(i));
				}
			}

		}
		if (page.getUrl().regex(URL_DETAILS).match()) {
			Document doc = Jsoup.parse(page.getHtml().toString());
			Element div = doc.getElementsByAttributeValue("class", "article").get(0);

			String projectName = div.getElementsByAttributeValue("class", "title").text();
			String projectPublicStart = div.getElementsByAttributeValue("class", "data").text().replace("发布时间：", "").replace("采购方式：公开", "");

			StringBuffer projectArticle = new StringBuffer();
			for (Element pElement : div.getElementsByAttributeValueEnding("class", "detail").get(0).children()) {
				projectArticle.append(pElement.text()).append("\n");
			}
			
			Project project = new Project();
			project.setProjectName(projectName);
			project.setPublicStart(projectPublicStart);
			project.setUrl(page.getUrl().toString().trim());
			project.setState(0);
			project.setWebsiteType("揭阳市");
			project.setTime(MyUtils.getcurentTime());
			project.setArticle(projectArticle.toString());
			project.setRawHtml(div.toString());
			System.out.println(project);

			HibernateUtil.save2Hibernate(project);
		}
	}

}
