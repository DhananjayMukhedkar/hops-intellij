package com.logicalclocks.cli.action;

import com.logicalclocks.cli.config.HopsworksAPIConfig;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;

import static org.apache.http.HttpHeaders.USER_AGENT;

public class JobCreateAction extends HopsworksAction {
  
  private static final Logger logger = LoggerFactory.getLogger(JobStartAction.class);
  
  private JsonObject payload;
  
  public JobCreateAction(HopsworksAPIConfig hopsworksAPIConfig, JsonObject payload) {
    super(hopsworksAPIConfig);
    this.payload = payload;
  }
  
  @Override
  public int execute() throws Exception {
    HttpClient getClient = getClient();
    String uri = hopsworksAPIConfig.getProjectUrl() + "/jobs/spark";
    HttpPost request = new HttpPost(uri);
    request.addHeader("User-Agent", USER_AGENT);
    request.addHeader("Authorization", "ApiKey " + hopsworksAPIConfig.getApiKey());

    StringEntity entity = new StringEntity(payload.toString());
    request.setEntity(entity);
    request.setHeader("Accept", "application/json");
    request.setHeader("Content-type", "application/json");
    
    HttpResponse response = getClient.execute(request);
    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    StringBuilder result = new StringBuilder();
    String line = "";
    while ((line = rd.readLine()) != null) {
      result.append(line);
    }
    JsonObject body = Json.createReader(new StringReader(result.toString())).readObject();
    logger.info("create job: " + body.toString());
    return response.getStatusLine().getStatusCode();
  }
}
