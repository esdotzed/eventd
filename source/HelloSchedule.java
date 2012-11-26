package com.eventd;








import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HelloSchedule extends Activity {
    /** Called when the activity is first created. */
    private TextView myTextView = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);
        
        Intent intent = getIntent();
		//��Intent���и���keyȡ��value
		String value = intent.getStringExtra("testIntent");
		//���ݿؼ���ID�õ���Ӧ�Ŀؼ�����
	//	myTextView = (TextView)findViewById(R.id.myTextView);
		//Ϊ�ؼ�����Textֵ
	//	myTextView.setText(value);
    }
    
    public void onHomeClick(View v) {
    	startActivity(new Intent(this, EventdAndroid.class));
    }
    
    public void onScheduleClick(View v) {
        // Launch overall conference schedule
        startActivity(new Intent(this, HelloSchedule.class));
    }
    
    public void onMapClick(View v) {
    	startActivity(new Intent(this, com.eventd.augment.activity.Demo.class));
    }
    
    public void onStarredClick(View v) {
    	startActivity(new Intent(this, com.eventd.feedback.sendMySQL.class));
    }
    
    public void onSessionsClick(View v) {
    	Toast.makeText(this, "To be implemented :-)", Toast.LENGTH_LONG).show();
    }
    
    public void onVendorsClick(View v) {
    	Toast.makeText(this, "Time to get some coffee :-)", Toast.LENGTH_LONG).show();
    }
    
    public void onNotesClick(View v) {
    	Toast.makeText(this, "To be implemented :-)", Toast.LENGTH_LONG).show();
    }
    
    public void onSearchClick(View v) {
    	Toast.makeText(this, "Search to be implemented :-)", Toast.LENGTH_LONG).show();
    }
    
    public void onRefreshClick(View v) {
    	Toast.makeText(this, "Refresh to be implemented :-)", Toast.LENGTH_LONG).show();
    }
    
    
    
}