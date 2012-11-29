package com.eventd;

import java.io.StringReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class Profile extends Activity {
	private TextView name = null;
	private TextView ID  = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		name = (TextView)findViewById(R.id.item_name);
		ID = (TextView)findViewById(R.id.item_description);
		Client client = new Client();
		client.client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "MobileEventd");
		try {  
            String getURL = "http://hidden-brushlands-4742.herokuapp.com";
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
                	
                	if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("user"))
                	{
                		while (eventType != XmlPullParser.END_TAG || !xpp.getName().equals("user"))
                		{
                			if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("id"))
                			{
                				eventType = xpp.next();
                				if (eventType == XmlPullParser.TEXT)
                					ID.setText(xpp.getText());
                				//eventType = xpp.next();
                				//eventType = xpp.next();
                			}
                		
                			if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("username"))
                			{
                				eventType = xpp.next();
                				if (eventType == XmlPullParser.TEXT)
                					name.setText(xpp.getText());
                			}
                			eventType = xpp.next();
                		}
                	}
                    eventType = xpp.next();
                }
            }
		}catch (Exception e) {
        	e.printStackTrace();
        }    
	}


}
