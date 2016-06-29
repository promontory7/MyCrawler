package pageprocessoruncomplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import model.Project;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import utils.HibernateUtil;
import utils.MyUtils;

public class DongguanProcessor implements PageProcessor {
	public static String url = "http://www.dgzb.com.cn/ggzy/website/WebPagesManagement/findListByPage?fcInfotype=1&tenderkind=All&projecttendersite=SS&fcInfotitle=&currentPage=1";

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
			for (int i = 2; i < 50; i++) {
				String url = "http://www.dgzb.com.cn/ggzy/website/WebPagesManagement/findListByPage?fcInfotype=1&tenderkind=All&projecttendersite=SS&fcInfotitle=&currentPage="
						+ i;
				MyUtils.addRequestToPage(page, url);

			}
			isFirst = false;
		}

		JSONObject data = JSONObject.parseObject(page.getJson().toString());
		JSONArray datas = JSONArray.parseArray(data.getString("ls"));
		// System.out.println(page.getJson());

		for (int i = 0; i < datas.size(); i++) {
			JSONObject every = datas.getJSONObject(i);
			Project project = new Project();
			project.setTime(MyUtils.getcurentTime());
			project.setState(0);
			project.setWebsiteType("东莞市");
			project.setProjectNo(every.getString("fcTendersn"));
			project.setProjectName(every.getString("fcInfotitle"));
			project.setPublicStart(every.getString("fcInfostartdate"));
			project.setPublicEnd(every.getString("fcInfoenddate"));
			project.setArticle(every.getString("fcInfocontent"));
			project.setRawHtml(every.getString("fcInfocontent"));
			project.setProjectType(every.getString("businesstypecaption"));

			System.out.println(project);
			HibernateUtil.save2Hibernate(project);
		}
	}

}
