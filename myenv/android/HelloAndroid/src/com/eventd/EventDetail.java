package com.eventd;




import java.io.StringReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.eventd.login.MyButtonListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EventDetail extends Activity{
	private String eventid = null;
	private String eventtitle = null;
	private String description = null;
	private String location = null;
	private String owner = null;
	private String stime = null;
	private String etime = null;
	private TextView eventtitleT = null;
	private TextView descriptionT = null;
	private TextView locationT = null;
	private TextView ownerT = null;
	private TextView stimeT = null;
	private TextView etimeT = null;
	private Button go = null;
	private Button notgo = null;
	private Button maybego = null;

	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
    	setContentView(R.layout.eventdetail);
	    Bundle extras = getIntent().getExtras();
	    eventid = extras.getString("ID");
	    Log.i("eventids", eventid);
	 	eventtitleT = (TextView)findViewById(R.id.item_name);
		descriptionT = (TextView)findViewById(R.id.item_description);
		locationT = (TextView)findViewById(R.id.item_location);
		ownerT = (TextView)findViewById(R.id.item_visible);
		stimeT = (TextView)findViewById(R.id.item_from);
		etimeT = (TextView)findViewById(R.id.item_to);
	    go = (Button)findViewById(R.id.go);
	    notgo = (Button)findViewById(R.id.notgo);
	    maybego = (Button)findViewById(R.id.maybego);
	    go.setOnClickListener(new golistener());
	    notgo.setOnClickListener(new notgolistener());
	    maybego.setOnClickListener(new maybegolistener());
	     
	    //DefaultHttpClient client = new DefaultHttpClient();
	    Client client = new Client();
		//client = login.client;
	    client.client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "MobileEventd");
	    try {  
	        String getURL = "http://hidden-brushlands-4742.herokuapp.com/event/"+eventid;
	        HttpGet get = new HttpGet(getURL);
	        HttpResponse responseGet = client.client.execute(get);  
	        HttpEntity resEntityGet = responseGet.getEntity();  
	        if (resEntityGet != null) {  
	            //do something with the response
	         	String XML = EntityUtils.toString(resEntityGet);
	            Log.i("GET RESPONSE",XML);
	            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            XmlPullParser xpp = factory.newPullParser();
	             
	            xpp.setInput( new StringReader (XML) );
	            int eventType = xpp.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	            	if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("title"))
         			{
         				eventType = xpp.next();
         				if (eventType == XmlPullParser.TEXT)
         					eventtitle=xpp.getText();
         			}
	            	if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("desc"))
         			{
         				eventType = xpp.next();
         				if (eventType == XmlPullParser.TEXT)
         					description=xpp.getText();
         			}
	            	if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("txt"))
         			{
         			 	eventType = xpp.next();
         				if (eventType == XmlPullParser.TEXT)
         					location=xpp.getText();
         			}
	            	if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("owner"))
         			{
         			 	eventType = xpp.next();
         				if (eventType == XmlPullParser.TEXT)
         					owner=xpp.getText();
         			}
	            	if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("stime"))
         			{
         			 	eventType = xpp.next();
         				if (eventType == XmlPullParser.TEXT)
         					stime=xpp.getText();
         			}
	            	if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("etime"))
         			{
         			 	eventType = xpp.next();
         				if (eventType == XmlPullParser.TEXT)
         					etime=xpp.getText();
         			}
	            	eventType = xpp.next();
	            }
	            eventtitleT.setText(eventtitle);
	            descriptionT.setText(description);
	            locationT.setText(location);
	            ownerT.setText(owner);
	            stimeT.setText(stime);
	            etimeT.setText(etime);
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    } 	     
	     
	}
	
 	class golistener implements OnClickListener{
	    
    	public void onClick (View v) {
    	    Client client = new Client();
    	    client.client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "MobileEventd");
    	    try{
    	    	String getURL = "http://hidden-brushlands-4742.herokuapp.com/event/attend/"+eventid+"/Y";
    	    	HttpGet get = new HttpGet(getURL);
    	    	HttpResponse responseGet = client.client.execute(get);
    	    }catch (Exception e) {
            	e.printStackTrace();
            }     
    	}
    	
    }
 	class notgolistener implements OnClickListener{
	    
    	public void onClick (View v) {
    	    Client client = new Client();
    	    client.client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "MobileEventd");
    	    try{
                String getURL = "http://hidden-brushlands-4742.herokuapp.com/event/attend/"+eventid+"/N";
                HttpGet get = new HttpGet(getURL);
                HttpResponse responseGet = client.client.execute(get);
        	}catch (Exception e) {
              	e.printStackTrace();
            } 

    	}
 	}
 	class maybegolistener implements OnClickListener{
	    
    	public void onClick (View v) {
    	    Client client = new Client();
    	    client.client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "MobileEventd");
    	    try{
                String getURL = "http://hidden-brushlands-4742.herokuapp.com/event/attend/"+eventid+"/M";
                HttpGet get = new HttpGet(getURL);
                HttpResponse responseGet = client.client.execute(get);
        	}catch (Exception e) {
        		e.printStackTrace();
            }
    	}
	
 	}

		 
}


