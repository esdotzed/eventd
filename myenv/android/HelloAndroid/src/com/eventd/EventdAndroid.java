package com.eventd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class EventdAndroid extends Activity {
    /** Called when the activity is first created. */
    
	private Button button1 = null;
	private Button home_btn_notes=null;
	private Button home_btn_schedule=null;
	private Button home_btn_sessions=null;
	private Button home_btn_vendors=null;
	private Button monocole = null;
	private String cookieString = null;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        button1 = (Button)findViewById(R.id.button1);
        home_btn_notes = (Button)findViewById(R.id.home_btn_notes);
        home_btn_schedule= (Button)findViewById(R.id.home_btn_schedule);
        home_btn_vendors= (Button)findViewById(R.id.home_btn_vendors);
        home_btn_sessions= (Button)findViewById(R.id.home_btn_sessions);
        monocole = (Button)findViewById(R.id.home_btn_map);
        
        button1.setOnClickListener(new MyButtonListener());
        home_btn_notes.setOnClickListener(new AnotherButtonListener());
        home_btn_schedule.setOnClickListener(new AnotherButtonListener2());
        home_btn_vendors.setOnClickListener(new AnotherButtonListener3());
        home_btn_sessions.setOnClickListener(new AnotherButtonListener4());
        monocole.setOnClickListener(new monocoleListener());
       
        
    }
    
    /** Handle "schedule" action. */
    
    class monocoleListener implements OnClickListener{
        
    	public void onClick (View v) {

    	Intent intent = new Intent();


    	intent.setClass(EventdAndroid.this, Augmentation.class);

    	EventdAndroid.this.startActivity(intent);

    	}
    }   

    

    public void onMapClick(View v) {
   	//startActivity(new Intent(this, com.eventd.augment.activity.Demo.class));
   }
    
    public void onStarredClick(View v) {
    	//startActivity(new Intent(this, com.eventd.feedback.sendMySQL.class));
    }
    

    
    public void onSearchClick(View v) {
    	Toast.makeText(this, "Search to be implemented :-)", Toast.LENGTH_LONG).show();
    }
    
    public void onRefreshClick(View v) {
    	Toast.makeText(this, "Refresh to be implemented :-)", Toast.LENGTH_LONG).show();
    }
  
    class MyButtonListener implements OnClickListener{
        
    	public void onClick (View v) {

    	Intent intent = new Intent();


    	intent.setClass(EventdAndroid.this, login.class);

    	EventdAndroid.this.startActivity(intent);

    	}
    }  	    
    	class AnotherButtonListener implements OnClickListener{
    	    
    	public void onClick (View v) {
    	Intent intent1 = new Intent();

    	intent1.setClass(EventdAndroid.this, Createvent.class);

    	EventdAndroid.this.startActivity(intent1);


    	    }

    	    }

     	class AnotherButtonListener2 implements OnClickListener{
    	    
        	public void onClick (View v) {
        	Intent intent1 = new Intent();

        	intent1.setClass(EventdAndroid.this, SearchMap.class);

        	EventdAndroid.this.startActivity(intent1);


        	    }

        	    }
        	    
     	
        class AnotherButtonListener3 implements OnClickListener{
    	    
        	public void onClick (View v) {
        	
            Intent intent=new Intent(EventdAndroid.this,SelectEvent.class);
            //Bundle bundle = new Bundle();   
            //bundle.putString( "cookieString",cookieString);        
            //intent.putExtras(bundle);   
            startActivity(intent); 


        	    }

        	    }
        class AnotherButtonListener4 implements OnClickListener{
    	    
        	public void onClick (View v) {
        	Intent intent1 = new Intent();

        	intent1.setClass(EventdAndroid.this, Profile.class);

        	EventdAndroid.this.startActivity(intent1);


        	    }

        	    }
    
}



