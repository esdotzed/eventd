package com.eventd;



import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import com.eventd.login.MyButtonListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Createvent extends Activity{

	
	private Button create = null;
	private Spinner category = null;
	private String cat = null;
	private TextView name = null;
	private TextView description = null;
	private TextView fromtime = null;
	private TextView fromdate = null;
	private TextView totime = null;
	private TextView todate = null;
	private TextView location = null;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.createvent);		 
		 Intent intent = getIntent();
		 name = (TextView)findViewById(R.id.event_name_field);
		 description = (TextView)findViewById(R.id.event_description_field);
		 fromtime = (TextView)findViewById(R.id.from_time);
		 fromdate = (TextView)findViewById(R.id.from_date);
		 totime = (TextView)findViewById(R.id.to_time);
		 todate = (TextView)findViewById(R.id.to_date);
		 location = (TextView)findViewById(R.id.location_field);
		 create = (Button)findViewById(R.id.button2);		 
		 create.setOnClickListener(new MyButtonListener());
		 category = (Spinner)findViewById(R.id.spinner1);
		 category.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	
	 class MyButtonListener implements OnClickListener{
	        
	    	public void onClick (View v) {
	    		Client client = new Client();
	    	    
	    	    client.client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "MobileEventd");
		        try {
		            String postURL = "http://hidden-brushlands-4742.herokuapp.com/event/create/";
		            HttpPost post = new HttpPost(postURL);
		            List<NameValuePair> params = new ArrayList<NameValuePair>();
		            params.add(new BasicNameValuePair("title", name.getText().toString()));
		            params.add(new BasicNameValuePair("description", description.getText().toString()));
		            params.add(new BasicNameValuePair("start_time", fromdate.getText().toString()+" "+fromtime.getText().toString()));
		            params.add(new BasicNameValuePair("end_time", todate.getText().toString()+" "+totime.getText().toString()));	
		            params.add(new BasicNameValuePair("place_text", location.getText().toString()));
		            params.add(new BasicNameValuePair("category", cat));
		            Log.i("test",params.toString());
		            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params);
		            post.setEntity(ent);
		            HttpResponse responsePOST = client.client.execute(post);  
		            HttpEntity resEntity = responsePOST.getEntity();  
		            if (resEntity != null) {    
		                Log.i("RESPONSE",EntityUtils.toString(resEntity));
		            }
		        }catch(Exception e) {
		            e.printStackTrace();
		        }
	    		
	    		
	    		
	    		
	    		
	    		
	    		
	    		
	    		Intent intent = new Intent();    	
	    		intent.setClass(Createvent.this, EventdAndroid.class);
	    		Createvent.this.startActivity(intent);
	    	}
	
	 }
	 
	 
	 class CustomOnItemSelectedListener implements OnItemSelectedListener {
		 
		  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			//Toast.makeText(parent.getContext(), 
			//	"OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
			//	Toast.LENGTH_SHORT).show();
			  cat = parent.getItemAtPosition(pos).toString();
		  }
		  public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		  }
	 };
	 

  
}