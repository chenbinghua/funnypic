package com.hua.funnypic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainActivity extends Activity {

	private ProgressDialog pd; 	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        pd = new ProgressDialog(this);
        pd.setMessage("正在下载...");
    }
    
    public void click(View view){
    	new PicUrlTask().execute();
    }
    
    class PicUrlTask extends AsyncTask<Void, Void, List<String>>{
    	

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.show();
		}

		@Override
		protected void onPostExecute(List<String> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(pd.isShowing()){
				pd.dismiss();
			}
		}

		@Override
		protected List<String> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			List<String> list = new ArrayList<String>();
			
			try {
				URL url = new URL("http://www.neihanshequ.com/pic/");
				HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
				InputStream is = conn.getInputStream();
				
				StringBuffer sb = new StringBuffer();
				String line = "";
				BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
				while((line = br.readLine()) != null){
					sb.append(line);
				}
				br.close();
				String html = sb.toString();
				Document doc = Jsoup.parse(html);
				Element article = doc.getElementById("article");
				Elements imgs = article.getElementsByTag("img");
				boolean flag = false;
				for(Element img:imgs){
					if(flag){
						String picUrl = img.attr("data-original");
						list.add(picUrl);
					}
					flag = !flag;
				}
				//打印测试
				for (String str : list) {
					Log.i("Main", str);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
    	
    }

}
