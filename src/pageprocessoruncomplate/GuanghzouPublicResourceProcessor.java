package pageprocessoruncomplate;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
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

/**
 * 广州公共资源交易网
 * 
 * @author hehe
 *
 */
public class GuanghzouPublicResourceProcessor implements PageProcessor {

	public static final String URL_LIST = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\w*";
	// 房建市政
	public static final String URL_LIST1 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=503&pchannelid=466&curgclb=01\\,02&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=1&pcurIndex=1&cIndex=1&page=\\d+";
	// 交通
	public static final String URL_LIST3 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=510&channelids=15&pchannelid=467&curgclb=03&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=1&pcurIndex=2&page=\\d+";
	// 电力
	public static final String URL_LIST2 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=515&channelids=15&pchannelid=468&curgclb=05&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=1&pcurIndex=3&page=\\d+";
	// 铁路
	public static final String URL_LIST4 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=520&channelids=15&pchannelid=469&curgclb=06&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=2&pcurIndex=4&page=\\d+";
	// 水利
	public static final String URL_LIST5 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=525&channelids=15&pchannelid=470&curgclb=04&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=1&pcurIndex=5&page=\\d+";
	// 民航
	public static final String URL_LIST6 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=539&channelids=15&pchannelid=471&curgclb=07&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=1&pcurIndex=6&page=\\d+";
	// 园林
	public static final String URL_LIST7 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=543&channelids=15&pchannelid=472&curgclb=08&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=1&pcurIndex=7&page=\\d+";
	// 小额
	public static final String URL_LIST8 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=530&channelids=15&pchannelid=473&curgclb=&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=2&pcurIndex=8&page=\\d+";
	// 其它
	public static final String URL_LIST9 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=535&channelids=15&pchannelid=474&curgclb=13&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=1&pcurIndex=9&page=\\d+";

	public static final String URL_DETAILS = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout3/index\\.jsp\\?siteId=\\d+&channelId=\\d+8&infoId=\\d+";

	public static String url = "http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=503&pchannelid=466&curgclb=01,02&curxmlb=01,02,03,04,05&curIndex=1&pcurIndex=1&cIndex=1&page=1";
	public static String test = "http://www.gzggzy.cn/cms/wz/view/index/layout3/index.jsp?siteId=1&channelId=628&infoId=458535";

	private Site site = Site.me().setRetryTimes(3).setTimeOut(20000).setSleepTime(300);
	private static boolean isFirst = true;

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub

		if (isFirst) {
			System.out.println("添加所有列表链接");
			ArrayList<String> urls = new ArrayList<String>();
			// 房建市政
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=503&pchannelid=466&curgclb=01,02&curxmlb=01,02,03,04,05&curIndex=1&pcurIndex=1&cIndex=1&page=0");
			// 交通
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=510&channelids=15&pchannelid=467&curgclb=03&curxmlb=01,02,03,04,05&curIndex=1&pcurIndex=2&page=0");
			// 电力
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=515&channelids=15&pchannelid=468&curgclb=05&curxmlb=01,02,03,04,05&curIndex=1&pcurIndex=3&page=0");
			// 铁路
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=520&channelids=15&pchannelid=469&curgclb=06&curxmlb=01,02,03,04,05&curIndex=2&pcurIndex=4&page=0");
			// 水利
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=525&channelids=15&pchannelid=470&curgclb=04&curxmlb=01,02,03,04,05&curIndex=1&pcurIndex=5&page=0");
			// 民航
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=539&channelids=15&pchannelid=471&curgclb=07&curxmlb=01,02,03,04,05&curIndex=1&pcurIndex=6&page=0");
			// 园林
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=543&channelids=15&pchannelid=472&curgclb=08&curxmlb=01,02,03,04,05&curIndex=1&pcurIndex=7&page=0");
			// 小额
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=530&channelids=15&pchannelid=473&curgclb=&curxmlb=01,02,03,04,05&curIndex=2&pcurIndex=8&page=0");
			// 其它
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=535&channelids=15&pchannelid=474&curgclb=13&curxmlb=01,02,03,04,05&curIndex=1&pcurIndex=9&page=0");

			System.out.println("全部种类数量" + urls.size());
			page.addTargetRequests(urls);
			isFirst = false;
		}
		Document doc = Jsoup.parse(page.getHtml().toString());

		if (page.getUrl().regex(URL_LIST).match()) {
			
			String projectType=null;
			if (page.getUrl().regex(URL_LIST1).match()) {
				projectType="房建市政";
			}else if (page.getUrl().regex(URL_LIST2).match()) {
				projectType="电力";
			}else if (page.getUrl().regex(URL_LIST3).match()) {
				projectType="交通";
			}else if (page.getUrl().regex(URL_LIST4).match()) {
				projectType="铁路";
			}else if (page.getUrl().regex(URL_LIST5).match()) {
				projectType="水利";
			}else if (page.getUrl().regex(URL_LIST6).match()) {
				projectType="民航";
			}else if (page.getUrl().regex(URL_LIST7).match()) {
				projectType="园林";
			}else if (page.getUrl().regex(URL_LIST8).match()) {
				projectType="小额";
			}else  {
				projectType="其他";
			}
			
			System.out.println("获取列表数据");

			Elements trElements = doc.getElementsByAttributeValue("class", "wsbs-table").select("tbody").select("tr");
			System.out.println("trElements"+trElements.text().toString());
			for (Element tr : trElements) {
				Elements td = tr.select("td");
				if (td.size()==3) {
					CacheHashMap.cache.put(td.get(1).select("a").attr("href"), td.get(1).text() + "###" + td.get(2).text()+ "###" + projectType);
				}
				
			}
			System.out.println("CacheHashMap.cache.size()" + CacheHashMap.cache.size());

			List<String> urls = page.getHtml().xpath("//td[@class=\"text_left\"]").links().regex(URL_DETAILS).all();
			System.out.println("这个页面数据数量" + urls.size());

			if (urls != null && urls.size() > 0) {
				page.addTargetRequests(urls);

			}
		}

		if (page.getUrl().regex(URL_DETAILS).match()) {
			Project project = new Project();

			String projectType=null;
			String projectName = null;
			String projectPublicStart = null;
			StringBuffer article = new StringBuffer();
			StringBuffer attach = new StringBuffer();
			String rawhtml=null;

			
			String value = CacheHashMap.cache.get(page.getUrl().toString());
			projectName = value.split("###")[0];
			projectPublicStart = value.split("###")[1];
			projectType=value.split("###")[2];

			Elements elements = doc.getElementsByAttributeValue("class", "Section1").select("p");
			
			for (int i = 0; i < elements.size(); i++) {
				article.append(elements.get(i).text()).append("\n");
			}

			Elements attachElements = doc.getElementsByAttributeValue("class", "xx-main").select("font");
			for (int i = 1; i < attachElements.size(); i++) {
				attach.append(attachElements.get(i).text()).append(" ### ")
						.append(attachElements.get(i).select("a").attr("href")).append("\n");
			}

			rawhtml=doc.getElementsByAttributeValue("class", "Section1").toString();
			
			project.setTime(MyUtils.getcurentTime());
			project.setWebsiteType("广州市");
			project.setState(0);
			project.setUrl(page.getUrl().toString());
			project.setProjectName(projectName);
			project.setProjectType(projectType);
			project.setPublicStart(projectPublicStart);
			project.setArticle(article.toString());
			project.setAttach(attach.toString());
			project.setRawHtml(rawhtml);
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
