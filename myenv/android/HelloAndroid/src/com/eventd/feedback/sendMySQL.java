package com.eventd.feedback;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eventd.EventdAndroid;
import com.eventd.R;

public class sendMySQL extends Activity {
	InputStream is;
	JSONObject json_data;
	TableLayout table;
	TextView columnOneText;
	TextView columnTwoText;
	String columnOneString;
	String columnTwoString;
	
	private EditText name;
	private EditText feedback;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.feedback);
	
	
	
	final Button button = (Button) findViewById(R.id.button1);
	button.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
	        // Perform action on clicks
	    	sendData();
	    	Toast.makeText(sendMySQL.this, "Sending complete! Thanks :-)", Toast.LENGTH_LONG).show();
	    	startActivity(new Intent(sendMySQL.this, com.eventd.EventdAndroid.class));
	        
	    }
	});
	
	
	
}
	
	public void sendData() {
		name = (EditText) findViewById(R.id.editText1);
		feedback = (EditText) findViewById(R.id.editText2);
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
		nameValuePairs.add(new BasicNameValuePair("name",name.getText().toString()));
	    nameValuePairs.add(new BasicNameValuePair("feedback",feedback.getText().toString()));

	    //http post
	    try{
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new      
	        HttpPost("http://www.eventd.net/sendFeedback.php");
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity entity = response.getEntity();
	        InputStream is = entity.getContent();
	        Log.i("postData", response.getStatusLine().toString());
	    }

	    catch(Exception e)
	    {
	        Log.e("log_tag", "Error in http connection "+e.toString());
	    }
	}
	
}
