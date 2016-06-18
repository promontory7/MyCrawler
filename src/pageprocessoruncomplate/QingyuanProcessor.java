package pageprocessoruncomplate;

import java.util.ArrayList;

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

public class QingyuanProcessor implements PageProcessor {

	public static final String URL_LIST = "http://www\\.qyggzy\\.cn/webIndex/newsLeftBoard/0102/010201\\?pageNO\\=\\d+";
	public static final String URL_DETAILS = "http://www.qyggzy.cn:80/webIndex/detailAllNews/*";
	public static final String URL_DETAILS2 = "http://www.qyggzy.cn/webIndex/detailAllNews/*";

	public static String url = "http://www.qyggzy.cn/webIndex/newsLeftBoard/0102/010201?pageNO=1";
	public static String test = "http://www.stjs.org.cn/zbtb/info_xinxi.asp?id=1882";
	private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
	private static boolean isFirst = true;

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		Document doc = Jsoup.parse(page.getHtml().getFirstSourceText());

		if (isFirst) {
			System.out.println("添加所有列表链接");
			ArrayList<String> urls = new ArrayList<String>();
			// 24
			for (int i = 2; i < 20; i++) {
				urls.add("http://www.qyggzy.cn/webIndex/newsLeftBoard/0102/010201?pageNO=" + i);
			}
			page.addTargetRequests(urls);
			System.out.println("一共有  " + urls.size() + " 个列表数据");

			isFirst = false;
		}

		if (page.getUrl().regex(URL_LIST).match()) {
			System.out.println("获取列表数据");

			Element div = doc.getElementById("context_div");
			Elements trs = div.select("table").select("tbody").select("tr");
			for (Element tr : trs) {

				if (tr.children().size() > 2) {
					Elements tds = tr.select("td");
					String url = tds.get(0).select("a").get(0).attr("href");
					String title = tds.get(0).text();
					String data = tds.get(1).text();

					CacheHashMap.cache.put(url, title + "###" + data);
					page.addTargetRequest(url);
					System.out.println(url + "  " + CacheHashMap.cache.get(url));
				}

			}

		}

		if (page.getUrl().regex(URL_DETAILS).match() || page.getUrl().regex(URL_DETAILS2).match()) {

			Project project = new Project();
			StringBuffer project_article = new StringBuffer();
			StringBuffer project_attach = new StringBuffer();

			String urldetails = CacheHashMap.cache.get(page.getUrl().toString().trim());

			System.out.println(urldetails);
			String[] value = urldetails.split("###");
			if (value != null && value.length > 1) {
				project.setProjectName(value[0].split("、")[1]);
				project.setPublicStart(value[1]);
			}

			Element div = doc.getElementById("context_div");
			String rawhtml =div.toString();
			project.setRawHtml(div.toString());
			for (Element p : div.children()) {
				if (p.nodeName().equals("table")) {
					Elements trs = p.select("tbody").select("tr");
					for (Element tr : trs) {
						if (tr.select("p").size() > 5) {
							for (Element last : tr.select("p")) {
								project_article.append(last.text()).append("\n");
							}
						} else if (tr.select("table").size() > 0) {
							for (Element tabletr : tr.select("table").select("tr")) {
								if (tabletr.select("th").size() == 4) {
									Elements ths = tabletr.select("th");
									project_article.append(ths.get(0).text() + " : ").append(ths.get(1).text())
											.append("\n");
									project_article.append(ths.get(2).text() + " : ").append(ths.get(3).text())
											.append("\n");

								} else if (tabletr.select("td").size() == 3) {
									Elements tds = tabletr.select("td");
									project_attach.append(tds.get(2).text()).append("  ###   ")
											.append(tds.get(2).select("a").attr("href")).append("\n");
								} else {
									project_article.append(tabletr.text()).append("\n");

								}

							}
						}
					}
				} else {
					project_article.append(p.text()).append("\n");
				}
			}
			project.setUrl(page.getUrl().toString().trim());
			project.setState(0);
			project.setWebsiteType("清远市");
			project.setTime(MyUtils.getcurentTime());
			project.setAttach(project_attach.toString());
			project.setArticle(project_article.toString());
			project.setRawHtml(rawhtml);
			System.out.println(project);

			HibernateUtil.save2Hibernate(project);

		}

	}

}
