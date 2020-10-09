package com.logicalclocks.actions;

import io.hops.cli.action.JobRemoveAction;
import io.hops.cli.config.HopsworksAPIConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * read job status which can be accessed via getter method
 */
public class JobStatus extends JobAction {
    Logger logger = Logger.getLogger(JobStatus.class.getName());
    protected  String[] jobStatusArr=new String[2];

    private int executionId;
    //private HopsworksAPIConfig hopsworksAPIConfig;
    //private String jobName;
    public String[] getJobStatusArr() {
        return jobStatusArr;
    }

    private void setJobStatusArr(String[] arr) {
        this.jobStatusArr = arr;
    }


    public JobStatus(HopsworksAPIConfig hopsworksAPIConfig, String jobName) {
        super(hopsworksAPIConfig, jobName);
    }

    @Override
    public int execute() throws Exception {

        String[] arr = getLatestExecutionStatus();
        if (arr!=null){
            setJobStatusArr(arr);
            logger.log(Level.INFO," --- Job Name :"+jobName+" | State :"+arr[0]);
            logger.log(Level.INFO," --- Job Name :"+jobName+" | Final Status :"+arr[1]);
            return 0;
        }return 1;

    }

}
