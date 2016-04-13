package pageprocessor;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.Project;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import utils.SessionFactoryUtil;

public class ShanTouProcessor implements PageProcessor {
	public static final String URL_LIST = "http://www\\.stjs\\.org\\.cn/zbtb/zhaobiao_gonggao\\.asp*";
	public static final String URL_DETAILS = "http://www\\.stjs\\.org\\.cn/zbtb/info_xinxi\\.asp\\?id=\\d+";

	public static String url = "http://www.stjs.org.cn/zbtb/zhaobiao_gonggao.asp?page=1";
	public static String test = "http://www.stjs.org.cn/zbtb/info_xinxi.asp?id=1882";
	private Site site = Site.me().setRetryTimes(3).setSleepTime(10);
	private static boolean isFirst = true;

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub

		if (isFirst) {
			System.out.println("添加所有列表链接");
			ArrayList<String> urls = new ArrayList<String>();
			for (int i = 2; i < 20; i++) {
				urls.add("http://www.stjs.org.cn/zbtb/zhaobiao_gonggao.asp?page=" + i);
			}
			page.addTargetRequests(urls);
			isFirst = false;
		}

		if (page.getUrl().regex(URL_LIST).match()) {
			List<String> urls = page.getHtml().xpath("//td[@class=\"newsline7\"]").links().regex(URL_DETAILS).all();
			System.out.println("从列表页获取的详情数目" + urls.size());
			if (urls != null && urls.size() > 0) {
				page.addTargetRequests(urls);
			}

		} else {
			Document doc = Jsoup.parse(page.getHtml().toString());

			Elements divElement = doc.body().select("tbody");
			Project project = new Project();

			for (Element tbody : divElement) {
				int num = tbody.select("tr").size();
				if (num >= 1) {
					if (tbody.select("tr").get(0).select("td").get(0).text().equals("公告编号")) {
						Elements trr = tbody.select("tr");

						for (Element tr : trr) {
							int m = tr.select("td").size();
							for (int j = 0; j < m; j = j + 2) {
								System.out.println(
										tr.select("td").get(j).text() + " : " + tr.select("td").get(j + 1).text());

								if (tr.select("td").get(j).text().equals("工程编号")) {
									project.setProjectNo(tr.select("td").get(j + 1).text());
								}
								if (tr.select("td").get(j).text().equals("工程名称")) {
									project.setProjectName(tr.select("td").get(j + 1).text());
								}
								if (tr.select("td").get(j).text().equals("招标人")) {
									project.setOwners(tr.select("td").get(j + 1).text());
								}
								if (tr.select("td").get(j).text().equals("招标代理机构")) {
									project.setAgency(tr.select("td").get(j + 1).text());
								}
								if (tr.select("td").get(j).text().equals("联系人")) {
									project.setOwnerpeopleName(tr.select("td").get(j + 1).text());
								}
								if (tr.select("td").get(j).text().equals("联系电话")) {
									project.setOwnerpeoplePhone(tr.select("td").get(j + 1).text());
								}
								if (tr.select("td").get(j).text().equals("工程地点")) {
									project.setProjectAddress(tr.select("td").get(j + 1).text());
								}
								if (tr.select("td").get(j).text().equals("招标内容")) {
									project.setArticle(tr.select("td").get(j + 1).text());
								}
								if (tr.select("td").get(j).text().equals("报名时间")) {
									String[] strings = tr.select("td").get(j + 1).text().split("～");
									if (strings.length == 2) {
										project.setPublicStart(strings[0]);
										project.setPublicEnd(strings[2]);
									}
								}

							}

							SessionFactory sf = SessionFactoryUtil.getInstance();
							Session s = null;
							Transaction t = null;

							try {
								s = sf.openSession();
								t = s.beginTransaction();
								s.save(project);
								t.commit();
							} catch (Exception err) {
								t.rollback();
								err.printStackTrace();
							} finally {
								s.close();
							}
						}
					}

					// System.out.println(tbody.select("tr").get(0).select("td").get(1).text());
				}

			}
		}
	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

}
