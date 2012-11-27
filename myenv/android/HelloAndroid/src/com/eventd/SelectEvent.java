package com.eventd;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SelectEvent extends Activity {
	private String cookieString = null;
	private HttpContext localContext = new BasicHttpContext();
	private Cookie c = null;
	private List<String> eventname = new ArrayList<String>();
	private List<String> eventid = new ArrayList<String>();
	private String[] eventsid = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectevent);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cookieString = extras.getString("cookieString");
            Log.i("cookies", cookieString);
        }
        else
        {
        	Intent intent = new Intent();
        	intent.setClass(SelectEvent.this, login.class);
        	SelectEvent.this.startActivity(intent);
        }
        ListView listView = (ListView) findViewById(R.id.eventlist);
      
        //Http client and parse the cookie.
        DefaultHttpClient client = new DefaultHttpClient();
        client = login.client;
        client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "MobileEventd");
        /*String[] keyValueSets = cookieString.split(";");
        for(String cookie : keyValueSets)
        {
            String[] keyValue = cookie.split("=");
            String key = keyValue[0];
            String value = "";
            if(keyValue.length>1) value = keyValue[1];
            Log.i("key", key);
            Log.i("value", value);
            //BasicClientCookie c = new BasicClientCookie(key, value);
            c = new BasicClientCookie(key, value);
            //c.setDomain("http://hidden-brushlands-4742.herokuapp.com");
            client.getCookieStore().addCookie(c);
            localContext.setAttribute(ClientContext.COOKIE_STORE, client.getCookieStore());
        }*/
        
        try {  
            String getURL = "http://hidden-brushlands-4742.herokuapp.com";
            HttpGet get = new HttpGet(getURL);
            HttpResponse responseGet = client.execute(get);  
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
                	
                	if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("event"))
                	{
                		while (eventType != XmlPullParser.END_TAG || !xpp.getName().equals("event"))
                		{
                			if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("id"))
                			{
                				eventType = xpp.next();
                				if (eventType == XmlPullParser.TEXT)
                					eventid.add(xpp.getText());
                				//eventType = xpp.next();
                				//eventType = xpp.next();
                			}
                		
                			if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("title"))
                			{
                				eventType = xpp.next();
                				if (eventType == XmlPullParser.TEXT)
                					eventname.add(xpp.getText());
                			}
                			eventType = xpp.next();
                		}
                	}
                    eventType = xpp.next();
                }
                for (String s:eventname)
                {
                	Log.i("eventlist", s);
                }
                for (String s:eventid)
                {
                	Log.i("eventidlist", s);
                }
                String [] events = eventname.toArray(new String[eventname.size()]);
                eventsid = eventid.toArray(new String[eventid.size()]);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, events);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(mMessageClickedHandler);                 
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }     
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selectevent, menu);
		return true;
	}
	private OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
	    public void onItemClick(AdapterView parent, View v, int position, long id) {
	        // Do something in response to the click
            Intent intent=new Intent(SelectEvent.this, Myevent.class);
            Bundle bundle = new Bundle();
            bundle.putString( "ID",eventsid[position]);        
            intent.putExtras(bundle);   
            startActivity(intent); 
	    }
	};
}

