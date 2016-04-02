package pageprocessor;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 中山市公共资源交易中心
 * 
 * @author hehe
 *
 */
public class ZhongshanProcessor implements PageProcessor {

	public static final String URL_LIST = "http://www\\.zsjyzx\\.gov\\.cn/zsweb/newweb/showList/000000000002/000000000201\\?pageNO=\\d+";
	public static final String URL_DETAILS_RAW = "http://www\\.zsjyzx\\.gov\\.cn:80/zsweb/newweb/detial/\\d+";
	public static final String URL_DETAILS = "http://www\\.zsjyzx\\.gov\\.cn/zsweb/newweb/detial/\\d+";

	public static final String URL_TABLE = "http://www\\.zsjyzx\\.gov\\.cn/zs/user/showWebZBGG\\?pjtfsn=*";

	public static String url = "http://www.zsjyzx.gov.cn/zsweb/newweb/showList/000000000002/000000000201?pageNO=1";
	public static String test = "http://www.zsjyzx.gov.cn/zsweb/newweb/detial/000000013924";
	public static String table = "http://www.zsjyzx.gov.cn/zs/user/showWebZBGG?pjtfsn=3627";

	private Site site = Site.me().setRetryTimes(3).setSleepTime(3000);

	private static boolean isFirst = true;

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		Document doc = Jsoup.parse(page.getHtml().toString());

		if (isFirst) {
			System.out.println("添加所有列表链接");
			ArrayList<String> urls = new ArrayList<String>();
			for (int i = 1; i < 5; i++) {
				urls.add("http://www.zsjyzx.gov.cn/zsweb/newweb/showList/000000000002/000000000201?pageNO=" + i);
			}
			page.addTargetRequests(urls);
			isFirst = false;
		}
		if (page.getUrl().regex(URL_LIST).match()) {
			List<String> urls = page.getHtml().xpath("//td[@class=\"toptd_bai\"]").links().regex(URL_DETAILS_RAW).all();
			System.out.println("从列表页获取的详情数目" + urls.size());
			for (String url : urls) {
				System.out.println(url.replace(":80", ""));
				page.addTargetRequest(url.replace(":80", ""));
			}
		}

		if (page.getUrl().regex(URL_DETAILS).match()) {

			// Elements divElement=doc.select("projectForm");
			// System.out.println(doc.body().select("document").text());

			// System.out.println(page.getHtml().xpath("//div[@id='context_div']").links().all().size());
			// http://www.zsjyzx.gov.cn/zs/user/showWebZBGG?pjtfsn=3446

			doc.getElementById("context_div").select("iframe").text();
			String tagreturl = doc.getElementById("context_div").select("iframe").attr("src").toString()
					.replace(":80/zsweb/..", "");
			System.out.println("添加详情信息表格"+tagreturl);
			// page.addTargetRequest();
			page.addTargetRequest(tagreturl);

		}
		if (page.getUrl().regex(URL_TABLE).match()) {

			Elements trnum = doc.body().select("tbody").select("tr");
			System.out.println("我要开始解析数据了："+trnum.size());

			for (Element tr : trnum) {
				int num = tr.select("th").size();
				if (num > 0) {
					for (int i = 0; i < num; i++) {
						if (tr.select("th").size() == tr.select("td").size()) {
							page.putField(tr.select("th").get(i).text(), tr.select("td").get(i).text());
							System.out.println(tr.select("th").get(i).text() + " : " + tr.select("td").get(i).text());
						} else if ((tr.select("th") == null) && tr.select("td").size() != 0) {
							System.out.println(tr.select("td").get(i).text());
							System.out.println("怎么可以没有??");
						} else if (tr.select("th").size() - 1 == tr.select("td").size()) {
							if (i == num - 1) {
								continue;
							}
							System.out
									.println(tr.select("th").get(i + 1).text() + " : " + tr.select("td").get(i).text());
						} else if ((tr.select("th").size() == 1) && (tr.select("td").size() == 4)) {
							System.out.println(tr.select("th").get(0).text() + " : " + tr.select("td").get(0).text()
									+ " , " + tr.select("td").get(1).text() + " , " + tr.select("td").get(2).text()
									+ " , " + tr.select("td").get(3).text());

						}
					}
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
