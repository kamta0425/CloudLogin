package com.example.kamta.cloudlogin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView name,username,age,email;
	public static final String DEFAULT="null";
	SharedPreferences sp=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_main);
	
		 name=(TextView)findViewById(R.id.tvName);
		 username=(TextView)findViewById(R.id.tvUsername);
		 age=(TextView)findViewById(R.id.tvAge);
		 email=(TextView)findViewById(R.id.tvEmail);
		 sp = getSharedPreferences("login",MODE_PRIVATE);

	}
	
	@Override
	protected void onStart() {
		super.onStart();
		boolean status = sp.getBoolean("status",false);
		if(status){
			displayUserDetail();
		}else{
			finish();
			startActivity(new Intent(MainActivity.this,Login.class));
		}
	}
	
	private void displayUserDetail() {
		name.setText(sp.getString("name",DEFAULT));
		username.setText(sp.getString("username",DEFAULT));
		email.setText(sp.getString("email",DEFAULT));
		age.setText(sp.getString("age",DEFAULT));
	}

	public void ActionMain(View view){
		SharedPreferences.Editor editor =sp.edit();
		editor.putBoolean("status",false);
		editor.apply();
		startActivity(new Intent(MainActivity.this,Login.class));
		finish();
	}
}
