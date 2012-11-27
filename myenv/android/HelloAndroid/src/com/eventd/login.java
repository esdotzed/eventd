package com.eventd;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.eventd.EventdAndroid.AnotherButtonListener;
import com.eventd.EventdAndroid.MyButtonListener;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class login extends Activity{
	private Button login = null;
	private TextView userTextView = null;
	private TextView psswdTextView = null;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.login);
		 userTextView = (TextView)findViewById(R.id.username);
		 psswdTextView = (TextView)findViewById(R.id.Password_Field);
		 
		 login = (Button)findViewById(R.id.login);
		 
		 login.setOnClickListener(new MyButtonListener());  
	
	}

	 class MyButtonListener implements OnClickListener{
	        
	    	public void onClick (View v) {

	    	Intent intent = new Intent();	    	
	        try {
	            HttpClient client = new DefaultHttpClient();  
	            client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "MobileEventd");
	            String postURL = "http://hidden-brushlands-4742.herokuapp.com/login/";
	            HttpPost post = new HttpPost(postURL);
	            List<NameValuePair> params = new ArrayList<NameValuePair>();
	            params.add(new BasicNameValuePair("username", userTextView.getText().toString()));
	            params.add(new BasicNameValuePair("password", psswdTextView.getText().toString()));
	            Log.i("test",params.toString());
	            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
	            post.setEntity(ent);
	            HttpResponse responsePOST = client.execute(post);  
	            HttpEntity resEntity = responsePOST.getEntity();  
	            if (resEntity != null) {    
	                Log.i("RESPONSE",EntityUtils.toString(resEntity));
	            }
	            else
	            	Log.i("RESPONSE","Failed!");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    	//intent.setClass(login.this, EventdAndroid.class);

	    	//login.this.startActivity(intent);

	    	}
	
     }
     
     
}	 
	 