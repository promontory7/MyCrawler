package pageprocessor;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 广东省招标投标监管网
 * @author hehe
 *
 */
public class GuangdongProcessor implements PageProcessor{
	
	public static final String URL_LIST = "http://www\\.gdzbtb\\.gov\\.cn/zhaobiao12/index_?\\d*\\.htm";
	public static final String URL_DETAILS = "http://www\\.gdzbtb\\.gov\\.cn/zhaobiao12/\\d+/t\\d+_\\d+.htm";
	
	public static String url="http://www.gdzbtb.gov.cn/zhaobiao12/index.htm";
	public static String test="http://www.gdzbtb.gov.cn/zhaobiao12/201603/t20160325_351106.htm";
	
	private Site site = Site.me().setRetryTimes(3).setSleepTime(10);
	
	private static boolean isFirst=true;

	@Override
	public void process(Page page) {
	
//		System.out.print(page.getHtml().toString());
		
		if(isFirst){
			System.out.println("添加所有列表链接");
			ArrayList<String> urls =new ArrayList<String>();
			for(int i=1;i<100;i++){
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
			Document doc = Jsoup.parse(page.getHtml().toString());
//			System.out.println(doc.body());
			
			Elements td = doc.getElementsByAttributeValue("class", "cn03");
			
			System.out.println("项目名称  ："+td.get(0).text());
			System.out.println("批复文号  ："+td.get(1).text());
			System.out.println("批复单位  ："+td.get(2).text());

			System.out.println("招标单位  ："+td.get(3).text());
			System.out.println("项目所在地 ："+td.get(4).text());

			System.out.println("资金来源  ："+td.get(5).text());
			System.out.println("招标类型  ："+td.get(6).text());
			System.out.println("公告内容  ："+td.get(7).text());
			System.out.println("发布人    ："+td.get(8).text());
			System.out.println("发布时间  ："+td.get(9).text());

			
			}
//			Element aString =doc.getElementById("article");
//			System.out.println(aString.toString());
//			
//		        Elements trs = aString.select("tr");
//		        for(int i = 2;i<trs.size();i++){.
//		            Elements tds = trs.get(i).select("td");
//		            for(int j = 0;j<tds.size();j++){
//		                String text = tds.get(j).text();
//		                System.out.println(" -----------"+text);
//		            }
//		        }
		}
		
	
		
	

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

}
