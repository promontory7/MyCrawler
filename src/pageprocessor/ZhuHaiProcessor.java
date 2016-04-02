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
 * 珠海市公共资源交易中心
 * @author hehe
 *
 */
public class ZhuHaiProcessor implements PageProcessor{
	
	public static final String URL_LIST = "http://ggzy\\.zhuhai\\.gov\\.cn//zbgg/index_?\\d*\\.htm";
	public static final String URL_DETAILS = "http://ggzy\\.zhuhai\\.gov\\.cn//zbgg/\\d+.htm";
	
	
	public static String url="http://ggzy.zhuhai.gov.cn//zbgg/index.htm";
	public static String test="http://ggzy.zhuhai.gov.cn//zbgg/66081.htm";
	private Site site = Site.me().setRetryTimes(3).setSleepTime(10);

	private static boolean isFirst=true;
	
	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		
		if(isFirst){
			System.out.println("添加所有列表链接");
			ArrayList<String> urls =new ArrayList<String>();
			for(int i=2;i<100;i++){
				urls.add("http://ggzy.zhuhai.gov.cn//zbgg/index_"+i+".htm");
			}
			page.addTargetRequests(urls);
			isFirst=false;
		}
		
		if (page.getUrl().regex(URL_LIST).match()) {
			System.out.println("开始处理");

			List<String> urls = page.getHtml().xpath("//ul[@class=\"news\"]").links().regex(URL_DETAILS).all();
			System.out.println("从列表页获取"+urls.size()); 
			if(urls!=null&&urls.size()>0){
				page.addTargetRequests(urls);
			}
	
			
		}else{
			Document doc = Jsoup.parse(page.getHtml().toString());
			Elements divElement=doc.getElementById("borderTB").select("tbody").select("tr");
			
			for(Element tr:divElement){
				int num=tr.select("th").size();
				if(num>0){
					for(int i=0;i<num;i++){
						page.putField(tr.select("th").get(i).text(), tr.select("td").get(i).text());
						System.out.println(tr.select("th").get(i).text()+" : "+tr.select("td").get(i).text());
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
