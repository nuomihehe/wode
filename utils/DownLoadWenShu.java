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

	
	public static boolean downloadFile(Map<String,String> params,String url,String startDate){
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
	        	HttpPost httpPost = new HttpPost(url);  
			httpPost.setConfig(requestConfig);
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
