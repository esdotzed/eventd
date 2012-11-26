package com.eventd.feedback;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.eventd.R;

public class testMySQL extends Activity {
	InputStream is;
	JSONObject json_data;
	TableLayout table;
	TextView columnOneText;
	TextView columnTwoText;
	String columnOneString;
	String columnTwoString;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.layout);
	
	getData();
}
	
	public void getData() {
	 String result = "";
	 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	
	 try	{
	 HttpClient httpclient = new DefaultHttpClient();
	 HttpPost httppost = new HttpPost("http://eventd.net/getUsers.php");
	 httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	 HttpResponse response = httpclient.execute(httppost);
	 HttpEntity entity = response.getEntity();
	 is = entity.getContent();
	 }catch(Exception e){
	 Log.e("log_tag", "Fehler bei der http Verbindung "+e.toString());
	 }

	 try {
	 BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	 StringBuilder sb = new StringBuilder();
	 String line = null;
	 while ((line = reader.readLine()) != null) {
	 sb.append(line + "n");
	 } 
	 is.close();
	 result=sb.toString();
	 }catch(Exception e){
	 Log.e("log_tag", "Error converting result "+e.toString());
	 }
	
	 try {
	 JSONArray jArray = new JSONArray(result);
	 for(int i=0;i<jArray.length();i++){
	 json_data = jArray.getJSONObject(i);
	 columnOneString = json_data.get("column1").toString();
	 columnTwoString = json_data.get("column2").toString();
	 fillList();
	 } 
	 }catch(JSONException e){
	 Log.e("log_tag", "Error parsing data "+e.toString());
	 }
	}
	
	public void fillList() {
	
	 table = (TableLayout) findViewById(R.id.TableLayout01);
	 TableRow row = new TableRow(this);
	 columnOneText = new TextView(this);
	 columnTwoText = new TextView(this);
	 columnOneText.setText(columnOneString);
	 columnTwoText.setText(columnTwoString);
	 row.addView(columnOneText);
 row.addView(columnTwoText);
 
 table.addView(row,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
 Toast.makeText(this, "Sending complete!", Toast.LENGTH_LONG).show();
	}
}
