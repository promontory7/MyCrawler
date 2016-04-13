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

/**
 * 广东省招标投标监管网
 * 
 * @author hehe
 *
 */
public class GuangdongProcessor implements PageProcessor {

	public static final String URL_LIST = "http://www\\.gdzbtb\\.gov\\.cn/zhaobiao12/index_?\\d*\\.htm";
	public static final String URL_DETAILS = "http://www\\.gdzbtb\\.gov\\.cn/zhaobiao12/\\d+/t\\d+_\\d+.htm";

	public static String url = "http://www.gdzbtb.gov.cn/zhaobiao12/index.htm";
	public static String test = "http://www.gdzbtb.gov.cn/zhaobiao12/201603/t20160325_351106.htm";

	private Site site = Site.me().setRetryTimes(3).setSleepTime(10);

	private static boolean isFirst = true;

	@Override
	public void process(Page page) { 
	
//		System.out.print(page.getHtml().toString());
		
		if(isFirst){
			System.out.println("添加所有列表链接");
			ArrayList<String> urls =new ArrayList<String>();
			for(int i=1;i<3;i++){
				urls.add("http://www.gdzbtb.gov.cn/zhaobiao12/index_"+i+".htm");
			}
			page.addTargetRequests(urls);
			isFirst=false;
		}
		
		if (page.getUrl().regex(URL_LIST).match()) {
			System.out.println("开始处理");

			List<String> urls = page.getHtml().xpath("//ul[@class=\"position2\"]").links().regex(URL_DETAILS).all();
			System.out.println(urls.size());
			if(urls!=null&&urls.size()>0){
				page.addTargetRequests(urls);
			}
	
			
		}else 
		{

			Project project =new Project();
			Document doc = Jsoup.parse(page.getHtml().toString());
//			System.out.println(doc.body());
			
			Elements td = doc.getElementsByAttributeValue("class", "cn03");
			Elements tr =doc.getElementsByAttributeValue("class","cn20_1");
			
			for(int i=0;i<tr.size();i++){
				System.out.println(tr.get(i).text()+"   :"+td.get(i).text());
				if (tr.get(i).text().equals("项目名称")) {
					project.setProjectName(td.get(i).text());
				}
				if (tr.get(i).text().equals("招标人")) {
					project.setOwners(td.get(i).text());
				}
				if (tr.get(i).text().equals("项目所在地区")) {
					project.setProjectAddress(td.get(i).text());
				}
				
				if (tr.get(i).text().equals("招标类型")) {
					project.setProjectType(td.get(i).text());
				}
				if (tr.get(i).text().equals("发布日期")) {
					project.setPublicStart(td.get(i).text());
				}
				if (tr.get(i).text().equals("批复文号")) {
					project.setProjectNo(td.get(i).text());
				}
				if (tr.get(i).text().equals("相关附件")) {
					project.setAttach(td.get(i).select("a").attr("href"));
				}
				if (tr.get(i).text().equals("公告内容")) {
					
					Elements d =td.get(i).select("div").select("p");
					StringBuffer stringBuffer =new StringBuffer();
					for(Element e:d){
						stringBuffer.append(e.text()).append("\n");
					}
					project.setArticle(stringBuffer.toString());
				}
				
			}
			
			SessionFactory sf =SessionFactoryUtil.getInstance();
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

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

}
