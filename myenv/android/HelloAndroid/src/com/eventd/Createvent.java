package com.eventd;



import com.eventd.login.MyButtonListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.createvent);		 
		 Intent intent = getIntent();
		 create = (Button)findViewById(R.id.button2);		 
		 create.setOnClickListener(new MyButtonListener());
		 category = (Spinner)findViewById(R.id.spinner1);
		 category.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	
	 class MyButtonListener implements OnClickListener{
	        
	    	public void onClick (View v) {
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
		  }
		  public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		  }
	 };
	 

  
}