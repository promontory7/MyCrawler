package pageprocessor;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.sql.Select;
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
 * 珠海市公共资源交易中心
 * 
 * @author hehe
 *
 */
public class ZhuHaiProcessor implements PageProcessor {

	public static final String URL_LIST = "http://ggzy\\.zhuhai\\.gov\\.cn//zbgg/index_?\\d*\\.htm";
	public static final String URL_DETAILS = "http://ggzy\\.zhuhai\\.gov\\.cn//zbgg/\\d+.htm";

	public static String url = "http://ggzy.zhuhai.gov.cn//zbgg/index.htm";
	public static String test = "http://ggzy.zhuhai.gov.cn//zbgg/66081.htm";
	private Site site = Site.me().setRetryTimes(3).setTimeOut(20000).setSleepTime(10);

	private static boolean isFirst = true;

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub

		if (isFirst) {
			System.out.println("添加所有列表链接");
			ArrayList<String> urls = new ArrayList<String>();
			// 800
			for (int i = 2; i < 100; i++) {
				urls.add("http://ggzy.zhuhai.gov.cn//zbgg/index_" + i + ".htm");
			}
			page.addTargetRequests(urls);
			isFirst = false;
		}

		if (page.getUrl().regex(URL_LIST).match()) {
			System.out.println("开始处理");

			List<String> urls = page.getHtml().xpath("//ul[@class=\"news\"]").links().regex(URL_DETAILS).all();
			System.out.println("从列表页获取" + urls.size());
			if (urls != null && urls.size() > 0) {
				page.addTargetRequests(urls);
			}

		} else {
			Document doc = Jsoup.parse(page.getHtml().toString());

			Elements div = doc.getElementsByAttributeValue("class", "m_r m_r_g");
			Elements trs = doc.getElementById("borderTB").select("tbody").select("tr");

			Project project = new Project();

			String projectName = null;
			String projectNo = null;
			String projectAddress = null;
			String projectOwner = null;
			String projectOwnerName = null;
			String projectOwnerPhone = null;
			String projectType = null;
			String projectWay = null;
			String projectIsAcceptUnion = null;
			String projectQualification_requir = null;
			String projectBond = null;
			String projectPublicEnd = null;
			String projectAgency = null;
			String projectAgencyName = null;
			String projectAgecyPhone = null;
			String projectPrice = null;
			String rawhtml = div.toString();
			String projectPublicStart = doc.getElementsByAttributeValue("class", "jigou").select("h5").text()
					.replace("发布时间：", "");

			boolean isSecondNameAndPhone = false;
			for (Element tr : trs) {
				Elements ths = tr.select("th");
				Elements tds = tr.select("td");

				for (int i = 0; i < ths.size(); i++) {

					switch (ths.get(i).text().trim()) {
					case "项目编号":
						projectNo = tds.get(i).text();
						break;
					case "项目名称":
						projectName = tds.get(i).text();
						break;
					case "项目地址":
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
					case "招标类别":
						projectType = tds.get(i).text();
						break;
					case "招标方式":
						projectWay = tds.get(i).text();
						break;
					case "接受联合体投标":
						projectIsAcceptUnion = tds.get(i).text();
						break;
					case "投标资格":
						projectQualification_requir = tds.get(i).text();
						break;
					case "投标保证金数额":
						projectBond = tds.get(i).text();
						break;
					case "投标确认截止时间":
						projectPublicEnd = tds.get(i).text().replace("前", "").trim();
						break;
					case "招标人/招标代理":
						projectAgency = tds.get(i).text();
						isSecondNameAndPhone = true;
						break;
					case "预算价/招标控制价":
						projectPrice = tds.get(i).text();
						break;
					default:
						break;
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
			project.setIsAcceptUnion(projectIsAcceptUnion);
			project.setArticle(projectQualification_requir);
			project.setBond(projectBond);
			project.setRawHtml(rawhtml);
			project.setUrl(page.getUrl().toString());
			project.setWebsiteType("海珠市");
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
