package com.scripted.roi;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.scripted.dataload.PropertyDriver;
import com.scripted.generic.FileUtils;

public class ROIAPIrequest {
	public static HttpResponse httpsResponse = null;
	private static final Logger log = Logger.getLogger(ROIAPIrequest.class);

	public static void callROIApi(String setComplexity, String totalHours) throws ClientProtocolException, IOException {
		try {
			PropertyDriver propDriver = new PropertyDriver();
			propDriver.setPropFilePath("src/main/resources/Roi/ROIGETApi.properties");
			String roiURI=PropertyDriver.readProp("roiURI");
			String projectName = PropertyDriver.readProp("project");
			String strUserName = PropertyDriver.readProp("username");
			String strDebugFlag = PropertyDriver.readProp("debug");
			String strRequestBody = "{\"Project\":\"" + projectName + "\",\"Complexity\":\"" + setComplexity
					+ "\",\"ExecutionTime\" : \"" + totalHours + "\",\"User\" : \"" + strUserName + "\",\"debug\":\""
					+ strDebugFlag + "\"} ";
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(roiURI);

			StringEntity params;
			params = new StringEntity(strRequestBody);
			request.addHeader("content-type", "application/json");
			request.setEntity(params);

			httpsResponse = httpClient.execute(request);
			String responseString = EntityUtils.toString(httpsResponse.getEntity());
			log.info("responseString");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
