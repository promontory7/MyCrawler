package pageprocessor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import model.Project;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import utils.HibernateUtil;
import utils.MyUtils;

public class ShenzhenProcessor implements PageProcessor {

	public static String url = "https://www.szjsjy.com.cn:8001/jyw/queryGongGaoList.do?rows=200&page=1";
	public static final String URL_DETAILS1 = "https://www\\.szjsjy\\.com\\.cn\\:8001/jyw/showGongGao\\.do\\?ggGuid\\=*";
	public static final String URL_DETAILS2 = "https://www\\.szjsjy\\.com\\.cn\\:8001/jyw/jyw/oldData\\_View*";

	private Site site = Site.me().setRetryTimes(3).setTimeOut(20000).setSleepTime(10);

	@Override
	public void process(Page page) {
		// TODO Auto-generated method st

		if (page.getUrl().toString().trim().equals(url)) {
			String rawDatas = page.getJson().toString().replace("var gongGaoList=", "");
			rawDatas = rawDatas.substring(0, rawDatas.length() - 1);
			JSONObject data = JSONObject.parseObject(rawDatas);
			JSONArray datas = JSONArray.parseArray(data.getString("rows"));
			System.out.println(datas.size());

			for (int i = 0; i < datas.size(); i++) {
				String url = (datas.getJSONObject(i).getString("detailUrl")).trim()
						.replace("/jyw/zbGongGao_View.do?ggguid", "/showGongGao.do?ggGuid");
				System.out.println(url);
				MyUtils.addRequestToPage(page, url);
			}
		}
		if (page.getUrl().regex(URL_DETAILS1).match()) {
			String rawDatas = page.getJson().toString();
			JSONObject data = JSONObject.parseObject(rawDatas);

			Document doc = Jsoup.parse(data.getString("html"));
			Project project = new Project();
			Element div_zbgk = doc.getElementById("zbgk");
			Element div_zbdl = doc.getElementById("zbdl");
			Element div_zbxx = doc.getElementById("bdxx");

			Elements tables = doc.select("table");
			System.out.println("-----------------------" + tables.size());
			for (Element table : tables) {
				for (Element tr : table.select("tr")) {
					Elements tds = tr.select("td");
					if (tds.size() > 0) {
						boolean isOwner = true;
						for (int i = 0; i < tds.size(); i++) {

							switch (tds.get(i).text().trim()) {
							case "招标项目名称：":
								project.setProjectName(tds.get(i + 1).text());
								break;
							case "招标项目编号：":
								project.setProjectNo(tds.get(i + 1).text());
								break;
							case "工程类型：":
								project.setProjectType(tds.get(i + 1).text());
								break;
							case "招标方式：":
								project.setTenDerWay(tds.get(i + 1).text());
								break;
							case "公告发布开始时间：":
								project.setPublicStart(tds.get(i + 1).text());
								break;
							case "公告发布截止时间：":
								project.setPublicEnd(tds.get(i + 1).text());
								break;
							case "建设单位：":
								project.setOwners(tds.get(i + 1).text());
								isOwner = true;
								break;
							case "招标代理机构：":
								project.setAgency(tds.get(i + 1).text());
								isOwner = false;
								break;
							case "经办人：":
								if (isOwner) {
									project.setOwnerpeopleName(tds.get(i + 1).text());
								} else {
									project.setAgencyName(tds.get(i + 1).text());
								}
								break;
							case "办公电话：":
								if (isOwner) {
									project.setOwnerpeoplePhone(tds.get(i + 1).text());
								} else {
									project.setAgecyPhone(tds.get(i + 1).text());
								}
								break;
							case "是否接受联合体投标：":
								project.setIsAcceptUnion(tds.get(i + 1).text());
								break;
							case "投标人应具备资质条件：":
								project.setQualification_requir(tds.get(i + 1).text());
								break;
							case "项目负责人资格：":
								project.setManager_require(tds.get(i + 1).text());
								break;
							case "工程地址：":
								project.setProjectAddress(tds.get(i + 1).text());
								break;
							case "本次发包监理费：":
								project.setProjectPrice(tds.get(i + 1).text());
								break;
							case "本次发包工程估价：":
								project.setProjectPrice(tds.get(i + 1).text());
								break;
							case "其他投标条件：":
								project.setArticle(tds.get(i + 1).text());
								break;

							default:
								break;
							}
						}
					}
				}
			}
			project.setRawHtml(doc.toString());
			project.setUrl(page.getUrl().toString());
			project.setWebsiteType("深圳市");
			project.setTime(MyUtils.getcurentTime());
			project.setState(1);

			System.out.println(project.toString());
			HibernateUtil.save2Hibernate(project);

		}
		if (page.getUrl().regex(URL_DETAILS2).match()) {

		}

	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

}
