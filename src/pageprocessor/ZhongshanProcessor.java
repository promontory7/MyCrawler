package pageprocessor;

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
import utils.HibernateUtil;
import utils.MyUtils;

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

	public static final String URL_TABLE = "http://www\\.zsjyzx\\.gov\\.cn/zs/user/showWebZBGG\\?pjtfsn\\=\\d+";
	public static String url = "http://www.zsjyzx.gov.cn/zsweb/newweb/showList/000000000002/000000000201?pageNO=1";
	public static String test = "http://www.zsjyzx.gov.cn/zsweb/newweb/detial/000000013924";
	public static String table = "http://www.zsjyzx.gov.cn/zs/user/showWebZBGG?pjtfsn=3627";

	private Site site = Site.me().setRetryTimes(3).setTimeOut(20000).setSleepTime(300);

	private static boolean isFirst = true;

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		Document doc = Jsoup.parse(page.getHtml().toString());

		if (isFirst) {
			System.out.println("添加所有列表链接");
			ArrayList<String> urls = new ArrayList<String>();
			//80
			for (int i = 1; i < 40; i++) {
				urls.add("http://www.zsjyzx.gov.cn/zsweb/newweb/showList/000000000002/000000000201?pageNO=" + i);
			}
			page.addTargetRequests(urls);
			isFirst = false;
		}
		if (page.getUrl().regex(URL_LIST).match()) {
			List<String> urls = page.getHtml().xpath("//td[@class=\"toptd_bai\"]").links().regex(URL_DETAILS_RAW).all();
			System.out.println("从列表页获取的详情数目" + urls.size());
			page.addTargetRequests(urls);
		}

		if (page.getUrl().regex(URL_DETAILS).match() || page.getUrl().regex(URL_DETAILS_RAW).match()) {

			doc.getElementById("context_div").select("iframe").text();
			String tagreturl = doc.getElementById("context_div").select("iframe").attr("src").toString()
					.replace(":80/zsweb/..", "");
			System.out.println("添加详情信息表格" + tagreturl);
			// page.addTargetRequest();
			page.addTargetRequest(tagreturl);

		}
		if (page.getUrl().regex(URL_TABLE).match()) {

			Elements trnum = doc.body().select("tbody").select("tr");

			Project project = new Project();

			String projectName = null;

			String projectAddress = null;
			String projectOwner = null;
			String projectOwnerName = null;
			String projectOwnerPhone = null;
			String projectType = null;
			String projectWay = null;
			String projectQualification_requir = null;
			String projectPublicEnd = null;
			String projectAgency = null;
			String projectAgencyName = null;
			String projectAgecyPhone = null;
			String projectPrice = null;

			String projectPublicStart = null;

			boolean isSecondNameAndPhone = false;
			Element form = doc.getElementById("projectForm");
			String rawhtml = form.toString();
			String projectNo = doc.getElementById("projectForm").select("div").text();

			for (Element tr : trnum) {
				Elements ths = tr.select("th");
				Elements tds = tr.select("td");

				if ((ths != null && ths.size() > 0) && (tds != null && tds.size() > 0) && ths.size() == tds.size()) {
					for (int i = 0; i < ths.size(); i++) {

						switch (ths.get(i).text().trim()) {

						case "工程名称":
							projectName = tds.get(i).text();
							break;
						case "工程所在地":
							projectAddress = tds.get(i).text();
							break;
						case "建设单位":
							projectOwner = tds.get(i).text();
							break;
						case "联系人":
							if (isSecondNameAndPhone) {
								projectAgencyName = tds.get(i).text();
							} else {
								projectOwnerName = tds.get(i).text();
							}
							break;
						case "联系电话":
							if (isSecondNameAndPhone) {
								projectAgecyPhone = tds.get(i).text();
								isSecondNameAndPhone = false;
							} else {
								projectOwnerPhone = tds.get(i).text();
								isSecondNameAndPhone = true;
							}
							break;

						case "批准招标形式":
							projectWay = tds.get(i).text();
							break;

						case "投标资格":
							projectQualification_requir = tds.get(i).text();
							break;

						case "招标部分造价":
							projectPrice = tds.get(i).text();
							break;

						case "报名起止日期":

							String[] datadetails = tds.get(i).text().split("至");
							projectPublicStart = datadetails[0];
							projectPublicEnd = datadetails[1];
							break;
						case "招标代理机构名称":
							projectAgency = tds.get(i).text();
							break;

						default:
							break;
						}
					}
				} else if ((ths == null || ths.size() == 0) && tds != null && tds.size() > 1) {
					projectQualification_requir = tds.get(0).text();
				} else if (ths.size() - 1 == tds.size()) {
					String first = ths.get(0).text();
					if (!first.equals("资金来源构成")) {
						projectType = first;
					}
				}
			}
			project.setProjectName(projectName);
			project.setProjectNo(projectNo);
			project.setPublicStart(projectPublicStart);
			project.setPublicEnd(projectPublicEnd);
			project.setProjectType(projectType);
			project.setProjectAddress(projectAddress);
			project.setOwners(projectOwner);
			project.setOwnerpeopleName(projectOwnerName);
			project.setOwnerpeoplePhone(projectOwnerPhone);
			project.setAgency(projectAgency);
			project.setAgecyPhone(projectAgecyPhone);
			project.setAgencyName(projectAgencyName);
			project.setTenDerWay(projectWay);
			project.setProjectPrice(projectPrice);
			project.setArticle(projectQualification_requir);
			project.setRawHtml(rawhtml);
			project.setUrl(page.getUrl().toString());
			project.setWebsiteType("中山市");
			project.setTime(MyUtils.getcurentTime());
			project.setState(0);

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
