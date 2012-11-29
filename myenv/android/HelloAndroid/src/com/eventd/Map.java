package com.eventd;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.eventd.SearchMap.searchListener;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
 
public class Map extends MapActivity
{    
	private TextView where = null;
	private TextView what = null;
	private String lng = null;
	private String lat = null;
	private Button search = null;
	private List<String> eventlat = new ArrayList<String>();
	private List<String> eventlng = new ArrayList<String>();
	private List<String> eventid = new ArrayList<String>();
	private String[] eventsid = null;
	private String[] eventslat = null;
	private String[] eventslng = null;
    /** Called when the activity is first created. */

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.android_map);
		lng = "-76.484159";
		lat = "42.4449355";
		where  = (TextView)findViewById(R.id.search_where_field);
		what = (TextView)findViewById (R.id.search_what_field);		 
	    search = (Button)findViewById(R.id.search);
	    search.setOnClickListener(new searchListener());

    }
 

    protected boolean isRouteDisplayed() {
        return false;
    }
    
    
    
    
	class searchListener implements OnClickListener{
        
    	public void onClick (View v) {
    		try {  
    			eventlng = new ArrayList<String>();
    			eventlat = new ArrayList<String>();
    			eventid = new ArrayList<String>();
    	        MapView mapView = (MapView) findViewById(R.id.mapView);
    	        mapView.setBuiltInZoomControls(true);
    	        List<Overlay> mapOverlays = mapView.getOverlays();
    	        Drawable drawable = Map.this.getResources().getDrawable(R.drawable.home_btn_map_default);
 			
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
                    	//Log.i("cookies","11");
                    	
                    	if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("event"))
                    	{
                    		//Log.i("cookies", "22");
                    		while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("event")))
                    		{
                    			//Log.i("cookies","33");
                    			if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("id"))
                    			{
                    				eventType = xpp.next();
                    				if (eventType == XmlPullParser.TEXT)
                    					eventid.add(xpp.getText());
                    					//Log.i("cookies", "44");
                    			}
                    		
                    			if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("lat"))
                    			{
                    				eventType = xpp.next();
                    				if (eventType == XmlPullParser.TEXT)
                    					eventlat.add(xpp.getText());
                    			}
                    			if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("lng"))
                    			{
                    				eventType = xpp.next();
                    				if (eventType == XmlPullParser.TEXT)
                    					eventlng.add(xpp.getText());
                    			}
                    			eventType = xpp.next();
                    		}
                    	}
                        eventType = xpp.next();
                    }
                    eventsid = eventid.toArray(new String[eventid.size()]);
                    eventslat = eventlat.toArray(new String[eventlat.size()]);
                    eventslng = eventlng.toArray(new String[eventlng.size()]);
                    Log.i("cookies", eventsid[0]);
                    
                    for (int i = 0; i <= eventid.size()-1; i++)
                    {
                    	HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, Map.this);
                    	Log.i("cookies",eventslat[i]);
                    	Log.i("cookies",eventslng[i]);
                    	GeoPoint point = new GeoPoint((int)(Double.parseDouble(eventslat[i])*1000000),(int)(Double.parseDouble(eventslng[i])*1000000));
                    	OverlayItem overlayitem = new OverlayItem(point, eventsid[i], "x");
                    	itemizedoverlay.addOverlay(overlayitem);
                    	mapOverlays.add(itemizedoverlay);
                    }
  
                }
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 

    	}
	}
    
	public class HelloItemizedOverlay extends ItemizedOverlay {
		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		private Context mContext;
		
		public HelloItemizedOverlay(Drawable defaultMarker) {
			  super(boundCenterBottom(defaultMarker));	  
			}
		public void addOverlay(OverlayItem overlay) {
		    mOverlays.add(overlay);
		    populate();
		}
		
		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return mOverlays.get(i);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return mOverlays.size();
		}
		
		public HelloItemizedOverlay(Drawable defaultMarker, Context context) {
			  super(boundCenterBottom(defaultMarker));
			  mContext = context;
		}
		protected boolean onTap(int index) {
			  OverlayItem item = mOverlays.get(index);
			  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			  //dialog.setTitle(item.getTitle());
			  //dialog.setMessage(item.getSnippet());
			  //dialog.show();
	          Intent intent=new Intent(Map.this, EventDetail.class);
	          Bundle bundle = new Bundle();
	          bundle.putString( "ID",item.getTitle());        
	          intent.putExtras(bundle);   
	          startActivity(intent); 
			  
			  
			  return true;
		}

	}

    
}