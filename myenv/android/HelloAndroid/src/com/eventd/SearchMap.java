package com.eventd;





import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class SearchMap extends Activity{
	private TextView where = null;
	private TextView what = null;
	private String lng = null;
	private String lat = null;
	private Button search = null;
	private List<String> eventname = new ArrayList<String>();
	private List<String> eventid = new ArrayList<String>();
	private String[] eventsid = null;

	

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.map);
		 lng = "-76.484159";
		 lat = "42.4449355";
		 where  = (TextView)findViewById(R.id.search_where_field);
		 what = (TextView)findViewById (R.id.search_what_field);		 
	     search = (Button)findViewById(R.id.search);
	     search.setOnClickListener(new searchListener());
	     
	     
	    
	    } 
	     
	class searchListener implements OnClickListener{
        
    	public void onClick (View v) {
    		try {  
    			eventname = new ArrayList<String>();
    			eventid = new ArrayList<String>();
    			ListView listView = (ListView) findViewById(R.id.listView);
    			Client client = new Client();
    	    	client.client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "MobileEventd");
                String getURL = "http://hidden-brushlands-4742.herokuapp.com/event/search_xml/?what="+what.getText().toString()+"&where="+where.getText().toString()+"&lng="+lng+"&lat="+lat;
                HttpGet get = new HttpGet(getURL);
                HttpResponse responseGet = client.client.execute(get);  
                HttpEntity resEntityGet = responseGet.getEntity();  
                if (resEntityGet != null) {  
                	//do something with the response
                    //Log.i("GET RESPONSE",EntityUtils.toString(resEntityGet));


                    
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
                    		while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("event")))
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
                    String [] events = eventname.toArray(new String[eventname.size()]);
                    //for (String s:events)
                    //	Log.i("cookies", s);
                    eventsid = eventid.toArray(new String[eventid.size()]);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchMap.this,
                    		  android.R.layout.simple_list_item_1, android.R.id.text1, events);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(mMessageClickedHandler);             
                            
                }
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 

    	}
	}
	private OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
	    public void onItemClick(AdapterView parent, View v, int position, long id) {
	        // Do something in response to the click
            Intent intent=new Intent(SearchMap.this, EventDetail.class);
            Bundle bundle = new Bundle();
            bundle.putString( "ID",eventsid[position]);        
            intent.putExtras(bundle);   
            startActivity(intent); 
	    }
	};
	
	
}