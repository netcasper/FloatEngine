package com.example.wangwilliam.floatengine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangwilliam on 2/28/16.
 */
public class HttpPicProvider extends AsyncTask<String, android.os.Process, List<Bitmap>> {
    private Button btnTrigger;
    private ChartAdapter adapter = null;
    private ListView listView = null;
    private List<ChartListFragment> fragmentList = null;

    public HttpPicProvider(Button btn, List<ChartListFragment> fragmentList) {
        btnTrigger = btn;
        this.fragmentList = fragmentList;
    }

    public HttpPicProvider(ChartAdapter adapter, ListView listView) {
        this.adapter = adapter;
        this.listView = listView;
    }

    @Override
    protected List<Bitmap> doInBackground(String... params) {
        List<Bitmap> bitmaps = new ArrayList<Bitmap>();
        String[] urls = params[0].split(" ");
        for (int i=0; i<urls.length; i++) {
            URL url = null;
            HttpURLConnection conn = null;
            InputStream is = null;
            try {
                url = new URL(urls[i]);
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(1000);
                conn.setRequestMethod("GET");
                conn.connect();
                is = conn.getInputStream();
                bitmaps.add(BitmapFactory.decodeStream(is));
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) conn.disconnect();
            }
        }

        return bitmaps;
    }

    @Override
    protected void onPostExecute(List<Bitmap> bitmaps) {
        btnTrigger.setEnabled(true);
        if (fragmentList == null || bitmaps == null) return;

        for(int i=0; i<fragmentList.size(); i++) {
            List<ChartItem> chartList = new ArrayList<ChartItem>();
            for(int j=0; j<bitmaps.size()/fragmentList.size(); j++) {
                ChartItem chartItem = new ChartItem();
                chartItem.bitmap = bitmaps.get(i*bitmaps.size()/fragmentList.size() + j);
                chartList.add(chartItem);
            }
            fragmentList.get(i).setChartList(chartList);
            fragmentList.get(i).Refresh();
        }
    }
}
