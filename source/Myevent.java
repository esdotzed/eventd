package com.eventd;



import com.eventd.feedback.sendMySQL;
import com.eventd.login.MyButtonListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Myevent extends Activity{

	
//	private Button create = null;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.myevent);
		 
		 Intent intent = getIntent();

//		 create = (Button)findViewById(R.id.button2);
		 
//		 create.setOnClickListener(new MyButtonListener());  
		 
	}

/*	
	 class MyButtonListener implements OnClickListener{
	        
	    	public void onClick (View v) {

	    	Intent intent = new Intent();

	    	

	    	intent.setClass(Createvent.this, EventdAndroid.class);

	    	Createvent.this.startActivity(intent);

	    	}
	
  }
 */ 
}