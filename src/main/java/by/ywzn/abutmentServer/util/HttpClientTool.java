package by.ywzn.abutmentServer.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpClientTool {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientTool.class);

	/**
	 * post 请求
	 * 
	 * @param requestUrl
	 *            请求url
	 * @param strQueryJson
	 *            传递的参数
	 * @return 接口返回的数据
	 */
	public static String post(String requestUrl, String strQueryJson) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String resp = "";
		try {
			// 创建HttpPost.
			HttpPost httppost = new HttpPost(requestUrl);
			httppost.setHeader("Accept-Encoding", "gzip, deflate");
			httppost.setHeader("Accept-Language", "zh-CN");
			httppost.setHeader("Accept", "application/json, application/xml, text/html, text/*, image/*, */*");
			httppost.setHeader("Content-Type", "application/json; charset=UTF-8");// 发送的格式，内容

			HttpEntity sendEntity;
			sendEntity = new StringEntity(strQueryJson, "UTF-8");
			httppost.setEntity(sendEntity);
			logger.info("");
			logger.info("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				logger.info("--------------------------------------");
				// 打印响应状态
				logger.info("status:" + response.getStatusLine());
				if (entity != null) {
					// 打印响应内容长度
					logger.info("Response content length: " + entity.getContentLength());
					// 打印响应内容
					resp = EntityUtils.toString(entity);
					logger.info("Response content: " + resp);
				}
				logger.info("------------------------------------");
				logger.info("");
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			JSONObject json = new JSONObject();
			json.put("msg", "ClientProtocolException");
			resp = json.toJSONString();
			logger.warn("" + e.getMessage(), e);
		} catch (ParseException e) {
			JSONObject json = new JSONObject();
			json.put("msg", "ParseException");
			resp = json.toJSONString();
			logger.warn("" + e.getMessage(), e);
		} catch (IOException e) {
			JSONObject json = new JSONObject();
			json.put("msg", "ParseException");
			resp = json.toJSONString();
			logger.warn("" + e.getMessage(), e);
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				logger.warn("" + e.getMessage(), e);
			}
		}
		return resp;
	}

	/**
	 * get 请求
	 * 
	 * @param requestUrl
	 *            请求url
	 * @param strQueryJson
	 *            传递的参数
	 * @return 接口返回的数据
	 */
	public static String get(String requestUrl, String strQueryJson) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String resp = "";
		try {
			// 创建httpget.
			HttpGet httpget = new HttpGet(requestUrl);
			StringEntity strEntity = new StringEntity(strQueryJson, ContentType.APPLICATION_JSON);
			String str = java.net.URLEncoder.encode(EntityUtils.toString(strEntity), "UTF-8");
			httpget.setURI(new URI(httpget.getURI().toString() + "?" + str.toString()));
			logger.info("executing request " + httpget.getURI());
			// 执行get请求.
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				logger.info("--------------------------------------");
				// 打印响应状态
				logger.info("status:" + response.getStatusLine());
				if (entity != null) {
					// 打印响应内容长度
					logger.info("Response content length: " + entity.getContentLength());
					// 打印响应内容
					resp = EntityUtils.toString(entity);
					logger.info("Response content: " + resp);
				}
				logger.info("------------------------------------");
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			JSONObject json = new JSONObject();
			json.put("msg", "ClientProtocolException");
			resp = json.toJSONString();
			logger.warn("" + e.getMessage(), e);
		} catch (ParseException e) {
			JSONObject json = new JSONObject();
			json.put("msg", "ParseException");
			resp = json.toJSONString();
			logger.warn("" + e.getMessage(), e);
		} catch (IOException e) {
			JSONObject json = new JSONObject();
			json.put("msg", "ParseException");
			resp = json.toJSONString();
			logger.warn("" + e.getMessage(), e);
		} catch (URISyntaxException e) {
			JSONObject json = new JSONObject();
			json.put("msg", "URISyntaxException");
			resp = json.toJSONString();
			logger.warn("" + e.getMessage(), e);
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				logger.warn("" + e.getMessage(), e);
			}
		}
		return resp;
	}

	/**
	 * put 请求
	 * 
	 * @param requestUrl
	 *            请求url
	 * @param strQueryJson
	 *            传递的参数
	 * @return 接口返回的数据
	 */
	public static String put(String requestUrl, String strQueryJson) {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		String resp = "";
		try {
			// 创建HttpPut.
			HttpPut httpput = new HttpPut(requestUrl);
			httpput.setHeader("Accept-Encoding", "gzip, deflate");
			httpput.setHeader("Accept-Language", "zh-CN");
			httpput.setHeader("Accept", "application/json, application/xml, text/html, text/*, image/*, */*");
			HttpEntity sendEntity;
			sendEntity = new StringEntity(strQueryJson, "UTF-8");
			httpput.setEntity(sendEntity);

			CloseableHttpResponse response = httpclient.execute(httpput);
			try {
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				logger.info("--------------------------------------");
				// 打印响应状态
				logger.info("status:" + response.getStatusLine());
				if (entity != null) {
					// 打印响应内容长度
					logger.info("Response content length: " + entity.getContentLength());
					// 打印响应内容
					resp = EntityUtils.toString(entity);
					logger.info("Response content: " + resp);
				}
				logger.info("------------------------------------");
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			JSONObject json = new JSONObject();
			json.put("msg", "ClientProtocolException");
			resp = json.toJSONString();
			logger.warn("" + e.getMessage(), e);
		} catch (ParseException e) {
			JSONObject json = new JSONObject();
			json.put("msg", "ParseException");
			resp = json.toJSONString();
			logger.warn("" + e.getMessage(), e);
		} catch (IOException e) {
			JSONObject json = new JSONObject();
			json.put("msg", "ParseException");
			resp = json.toJSONString();
			logger.warn("" + e.getMessage(), e);
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				logger.warn("" + e.getMessage(), e);
			}
		}
		return resp;
	}
}
