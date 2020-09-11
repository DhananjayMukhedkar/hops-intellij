package com.logicalclocks.cli.action;

import com.logicalclocks.cli.config.HopsworksAPIConfig;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import static org.apache.http.HttpHeaders.USER_AGENT;

public class JobStartAction extends HopsworksAction {
  private static final Logger logger = LoggerFactory.getLogger(JobStartAction.class);
  
  private int jobId;

  public JobStartAction(HopsworksAPIConfig hopsworksAPIConfig, int jobId) {
    super(hopsworksAPIConfig);
    this.jobId = jobId;
  }

  
  @Override
  public int execute() throws IOException {
    HttpClient getClient = getClient();
    String uri = hopsworksAPIConfig.getProjectUrl() + "/jobs/" + jobId + "/executions";
    HttpPost request = new HttpPost(uri);
    request.addHeader("User-Agent", USER_AGENT);
    request.addHeader("Authorization", "ApiKey " + hopsworksAPIConfig.getApiKey());
    HttpResponse response = getClient.execute(request);    // , localContext
    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    StringBuilder result = new StringBuilder();
    String line = "";
    while ((line = rd.readLine()) != null) {
      result.append(line);
    }
    JsonReader jsonReader = Json.createReader(new StringReader(result.toString()));
    JsonObject body = jsonReader.readObject();
    jsonReader.close();
    System.out.println("started job: " + jobId);
    return response.getStatusLine().getStatusCode();
  }
}
