package com.seeks.support.downloadws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DownLoadWenShu {

	static Logger logger=LoggerFactory.getLogger(DownLoadWenShu.class);
	
	private static RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(1500000)
            .setConnectTimeout(1500000)
            .setConnectionRequestTimeout(1500000)
            .build();
	private final static String DOWN_LOAD_PATH="/SEEKS/count_bak/";
	public static void download(String startDate,String id) throws Exception{
		String httpUrl="http://wenshu.court.gov.cn/Download/FileDownload.aspx";
		Map<String, String> maps=new HashMap<String, String>();
		maps.put("action", "2");
		maps.put("userName", "chongqing");
		maps.put("pwd", "chongqingcpws");

		Map<String, String> headers=new HashMap<String, String>();
		headers.put("Accept", "*/*");
		headers.put("Accept-Encoding", "gzip, deflate");
		headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7,ja;q=0.6");
		headers.put("Connection", "keep-alive");
		//headers.put("Content-Length", "45");
		//headers.put("Cookie", "DownLoadUserName=UserName=chongqing;");
		headers.put("DNT", "1");
		headers.put("Host", "wenshu.court.gov.cn");
		headers.put("Origin", "http://wenshu.court.gov.cn");
		headers.put("Referer", "http://wenshu.court.gov.cn/Download/DownLoadLogin.aspx");
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
		headers.put("X-Requested-With", "XMLHttpRequest");


		String result=sendHttpPost(httpUrl,headers, maps);
		if(result.equals("登录成功")){
			httpUrl="http://wenshu.court.gov.cn/Download/FileDownload.aspx?action=1";
			maps.clear();
			maps.put("id", id);
			maps.put("dates", startDate);
			downloadFile(maps, httpUrl, startDate);
		}else{
			logger.info(result);
		}
	}
	
	/**
	 * 发送 post请求
	 * @param httpUrl 地址
	 * @param maps 参数
	 * @throws UnknownHostException 
	 */
	public static String sendHttpPost(String httpUrl, Map<String, String> headers,Map<String, String> params) throws UnknownHostException {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost  
		for (String key : headers.keySet()) {
			httpPost.addHeader(key,headers.get(key));
		}
		// 创建参数队列  
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, params.get(key)));
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}
	
	private static String sendHttpPost(HttpPost httpPost){
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpPost.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.info(e.getMessage());
				//e.printStackTrace();
			}
		}
		return responseContent;
	}
	
	public static boolean downloadFile(Map<String,String> params,String url,String startDate){
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
	        HttpPost httpPost = new HttpPost(url);  
			httpPost.setConfig(requestConfig);
	        httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
	        httpPost.setHeader("Accept-Encoding", "gzip, deflate");
	        httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
	        httpPost.setHeader("Cache-Control", "max-age=0");
	        httpPost.setHeader("Connection", "keep-alive");
	        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
	        httpPost.setHeader("Host", "wenshu.court.gov.cn");
	        httpPost.setHeader("Origin", "http://wenshu.court.gov.cn");
	        httpPost.setHeader("Referer", "http://wenshu.court.gov.cn/Download/FileDownload.aspx");
	        httpPost.setHeader("Upgrade-Insecure-Requests", "1");
	        //DownLoadUserName=UserName=chongqing&Password=chongqingcpws
	        httpPost.setHeader("Cookie", "DownLoadUserName=UserName=chongqing&Password=chongqingcpws");
		    if (params != null && params.size() > 0) {  
	            List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
	            Set<String> keySet = params.keySet();  
	            for (String key : keySet) {  
	                nvps.add(new BasicNameValuePair(key, params.get(key)));  
	            }  
	            httpPost.setEntity(new UrlEncodedFormEntity(nvps));  
		    }  

	        HttpResponse response = httpClient.execute(httpPost);  
	        System.out.println(response.getStatusLine()); 

	        HttpEntity entity = response.getEntity();
	        //System.out.println(EntityUtils.toString(entity));
	        if (entity == null) {
	            return false;
	        }
	        logger.info(entity.getContentLength()/(1024.0*1024.0)+"---------------文书文件长度为-----"+startDate);
	        logger.info("【开始下载文件】:"+startDate);
	        InputStream is = entity.getContent();  
	        File file = new File( DOWN_LOAD_PATH+startDate+".zip");  
	        FileOutputStream output = new FileOutputStream(file);  
	        byte[] b = new byte[1024];
	        int j = 0;
	        while ((j = is.read(b)) != -1) {
	          output.write(b, 0, j);
	        }	        
	        output.flush();	        
	        is.close();  
	        output.close();
	        logger.info("【文件下载结束】:"+startDate);			
		}
		catch (Exception e) {
			e.getMessage();
			logger.info(startDate+"【下载文书文件出错，出错原因:】:"+e.getMessage());
			return false;
		}

		return true;
	}
}
