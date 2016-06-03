package pageprocessoruncomplate;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.metamodel.relational.Size;
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

public class ShaoguangProcessor implements PageProcessor {

	public static final String URL_LIST = "http://www\\.sgjyzx\\.com/businessAnnounceAction\\!frontBusinessAnnounceListChildren\\.do\\?businessAnnounce\\.announcetype\\=12\\&page\\=\\d+";
	public static final String URL_DETAILS = "http://www\\.sgjyzx\\.com/businessAnnounceAction\\!frontToBusinessAnnounceForm\\.do\\?businessAnnounce\\.id=*";

	// 市值
	public static String url = "http://www.sgjyzx.com/businessAnnounceAction!frontBusinessAnnounceListChildren.do?businessAnnounce.announcetype=12&page=1";

	public static String test = "http://www.sgjyzx.com/businessAnnounceAction!frontToBusinessAnnounceForm.do?businessAnnounce.id=ee374ed39aea4aa7a17cdfc9eba2585f";
	public static String detailStart = "http://www.sgjyzx.com/businessAnnounceAction!frontToBusinessAnnounceForm.do?businessAnnounce.id=";

	private Site site = Site.me().setRetryTimes(3).setSleepTime(300);
	private static boolean isFirst = true;

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		if (isFirst) {
			System.out.println("添加所有列表链接");
			ArrayList<String> urls = new ArrayList<String>();
			for (int i = 2; i < 10; i++) {
				urls.add(
						"http://www.sgjyzx.com/businessAnnounceAction!frontBusinessAnnounceListChildren.do?businessAnnounce.announcetype=12&page="
								+ i);
			}
			System.out.println("url的总数是：" + urls.size());
			page.addTargetRequests(urls);
			isFirst = false;
		}
		Document doc = Jsoup.parse(page.getHtml().toString());

		if (page.getUrl().regex(URL_LIST).match()) {
			Elements trs = doc.getElementsByAttributeValue("class", "listPanel").select("tbody").select("tr");
			for (Element tr : trs) {
				Elements tds = tr.select("td");
				if (tds.size() == 3) {
					String id = tds.get(1).select("a").toString().substring(11, 43);
					CacheHashMap.cache.put(detailStart + id, tds.get(1).text() + "###" + tds.get(2).text());
					page.addTargetRequest(detailStart + id);
					System.out.println(CacheHashMap.cache.get(detailStart + id));
				}
			}
		}
		if (page.getUrl().regex(URL_DETAILS).match()) {
			Elements elements = doc.getElementsByAttributeValue("class", "xx-text");

			Project project = new Project();

			StringBuffer projectArticle = new StringBuffer();
			String projectName = null;
			String projectPublicStart = null;


			if (elements.get(0).select("div").size() > 1) {
				for (Element element : elements.get(0).children()) {
					if (element.nodeName().equals("div")) {
						for (Element p : element.children()) {
							if (p.nodeName().equals("div")) {
								for (Element d : p.children()) {
									if (d.nodeName().equals("table")) {

										Elements trs = d.select("tbody").select("tr");
										for (Element tr : trs) {
											for (Element td : tr.select("td")) {
												for (Element last : td.children()) {
													projectArticle.append(last.text()).append("\n");
												}
											}
										}
									} else {
										projectArticle.append(d.text()).append("\n");
									}
								}
							} else if (p.nodeName().equals("table")) {

								Elements trs = p.select("tbody").select("tr");
								for (Element tr : trs) {
									for (Element td : tr.select("td")) {
										for (Element last : td.children()) {
											projectArticle.append(last.text()).append("\n");
										}
									}
								}
							} else {
								projectArticle.append(p.text()).append("\n");
							}
						}
					} else {
						projectArticle.append(element.text()).append("\n");
					}
				}
			} else if (elements.get(0).select("table").size() > 0) {
				for (Element element : elements.get(0).children()) {
					if (element.nodeName().equals("table")) {
						Elements trs = element.select("tbody").select("tr");
						for (Element tr : trs) {
							for (Element td : tr.select("td")) {
								for (Element last : td.children()) {
									projectArticle.append(last.text()).append("\n");
								}
							}
						}
					} else {
						projectArticle.append(element.text()).append("\n");
					}
				}
			} else {
				for (Element element : elements.get(0).children()) {
					if (element.children().size() > 0) {
						for (Element child : element.children()) {
							projectArticle.append(child.text()).append("\n");
						}
					} else {
						projectArticle.append(element.text()).append("\n");
					}
				}
			}

			// MyUtils.getLineText(elements.get(0).children(), projectArticle);

			String value = CacheHashMap.cache.get(page.getUrl().toString());
			projectName = value.split("###")[0];
			projectPublicStart = value.split("###")[1];

			project.setTime(MyUtils.getcurentTime());
			project.setWebsiteType("shaoguang");
			project.setState(0);
			project.setUrl(page.getUrl().toString());
			project.setProjectName(projectName);
			project.setPublicStart(projectPublicStart);
			project.setArticle(projectArticle.toString());
			System.out.println(project.toString());

			HibernateUtil.save2Hibernate(project);
		}

	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

}
