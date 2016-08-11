package com.example.kamta.cloudlogin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class Register extends Activity {

	EditText etName, etUsername, etAge, etPassword, etEmail;
	String name=null,age=null,email=null,password=null,username=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		etName =(EditText) findViewById(R.id.etName);
		etUsername =(EditText) findViewById(R.id.etUsernameR);
		etAge =(EditText) findViewById(R.id.etAge);
		etPassword =(EditText) findViewById(R.id.etPasswordR);
		etEmail =(EditText) findViewById(R.id.etEmail);
	}

	public void ActionRegister(View view)  {
		name = etName.getText().toString();
		username = etUsername.getText().toString();
		age = etAge.getText().toString();
		email = etEmail.getText().toString();
		password = etPassword.getText().toString();

		if(username.isEmpty() || name.isEmpty() || password.isEmpty() || name.equals("") ||
				age.equals("")|| age.isEmpty() || email.isEmpty() ||
				password.equals("") || username.equals("")
				){
			Toast.makeText(Register.this, "Field Cannot be Empty", Toast.LENGTH_SHORT).show();
		}else if(isNetworkAvailable()){
			registerUser();
		}else{
			Toast.makeText(Register.this, "Network Not Available", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		startActivity(new Intent(this,Login.class));
		super.onBackPressed();
	}

	private void registerUser()  {
		final ProgressDialog dialog = ProgressDialog.show(Register.this, "", "Processing...", true);
		RequestQueue mRequestQueue;
		Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
		Network network = new BasicNetwork(new HurlStack());
		mRequestQueue = new RequestQueue(cache, network);
		mRequestQueue.start();

		String url = "http://kps.comli.com/RegisterCloud.php";

		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						dialog.dismiss(); //pDialog.hide();
						Toast.makeText(Register.this,"Success",Toast.LENGTH_LONG).show();
						Log.d("kamta","Got String Respongse "+ response);
						setEverythingNull();
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						dialog.dismiss();  //pDialog.hide();
						Toast.makeText(Register.this,"Failed",Toast.LENGTH_LONG).show();
						Log.d("kamta", "String Respongse Error: " + error.getMessage());
						try {
							throw (error.getCause());
						} catch (UnknownHostException e) {
							Toast.makeText(Register.this,"Server Failed UnknownHost",Toast.LENGTH_LONG).show();
						} catch (Throwable throwable) {
							//Toast.makeText(Register.this,"Error while Processing",Toast.LENGTH_LONG).show();
							Log.d("kamta", "93ErrorRespongse Register : " + error.getMessage());
						}
						VolleyLog.d("kamta", "Volley String Respongse Error: " + error.getMessage());
					}
				}){
			@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("name", name);
				params.put("username", username);
				params.put("age", age);
				params.put("password", password);
				params.put("email", email);
				return params;
			}
		};
		mRequestQueue.add(stringRequest);
	}

	private void setEverythingNull() {
		etName.setText("");
		etUsername.setText("");
		etAge.setText("");
		etEmail.setText("");
		etPassword.setText("");
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
