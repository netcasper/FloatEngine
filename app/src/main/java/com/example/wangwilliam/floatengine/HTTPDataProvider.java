package com.example.wangwilliam.floatengine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

/**
 * Created by wangwilliam on 2/21/16.
 */
public class HTTPDataProvider {
    private StringBuilder sb = new StringBuilder();
    private boolean isRunning = true;
    private EngineSettings engineSettings = null;

    public HTTPDataProvider(EngineSettings engineSettings) {
        this.engineSettings = engineSettings;
    }

    public void SetEngineSettings(EngineSettings engineSettings) {
        this.engineSettings = engineSettings;
    }

    public boolean connect(String connectionString){
        return true;
    }

    public void disconnect(){
    }

    public void doWork() {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try{
                    while (isRunning){

                        URL url = new URL(buildURL(engineSettings.floatList));
                        conn = (HttpURLConnection)url.openConnection();
                        conn.setConnectTimeout(3000);
                        conn.setRequestMethod("GET");
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));

                        sb.delete(0, sb.length());
                        String realLine = null;
                        while ((realLine = br.readLine()) != null) {
                            String[] strList = realLine.split(",");
                            if (engineSettings.simpleMode == false) {
                                double d = Double.parseDouble(strList[1]);
                                if (d > 1000) {
                                    java.text.DecimalFormat df = new java.text.DecimalFormat("#"); // 保留小数点后2位，其他不变
                                    sb.append(df.format(d).toString());
                                } else {
                                    sb.append(strList[1]);
                                }
                                sb.append(engineSettings.priceSeparator);
                            }
                            sb.append(strList[3]);
                            sb.append(engineSettings.itemSeparator);
                        }
                        sb.delete(sb.length()-1, sb.length());
                        is.close();
                        br.close();
                        conn.disconnect();
                        Thread.sleep(engineSettings.interval);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (RuntimeException e){
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    if (conn != null) conn.disconnect();
                }
            }
        });
        thread.start();
    }

    public String getDataFromWeb(String codesWithPrefix){
        return sb.toString().trim();
    }

    public String parseDataInfo(String strInput){
        String result = "";
        if (strInput.isEmpty()) {
            return result;
        }

        return strInput;
    }

    public String getRatioListString(){
        String result = "Error";
        String webOutput = getDataFromWeb(buildURL(engineSettings.floatList));
        if (webOutput.isEmpty()){
            return result;
        }

        String finalString = parseDataInfo(webOutput);
        if (finalString.isEmpty()){
            return result;
        }
        return finalString;
    }

    public void shutdown(){
        isRunning = false;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String buildURL(String list){
        StringBuilder sb = new StringBuilder();
        String[] codes = list.split(" ");
        sb.append(EngineConstants.sinaURL);
        for (int i=0; i<codes.length; i++) {
            if (codes[i].length() != 6) continue;
            if (codes[i].equals(EngineConstants.szzs)
                    || codes[i].charAt(0) == '5'
                    || codes[i].charAt(0) == '6'){
                sb.append(EngineConstants.sinaPrefixShortSH);
            } else {
                sb.append(EngineConstants.sinaPrefixShortSZ);
            }
            sb.append(codes[i]);
            sb.append(',');
        }
        return sb.toString().trim();
    }

}
