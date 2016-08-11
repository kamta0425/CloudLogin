package com.example.kamta.cloudlogin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends Activity implements View.OnClickListener{

	EditText username,password;
	Button bLogin;
	TextView register;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		username=(EditText) findViewById(R.id.etUsername);
		password=(EditText) findViewById(R.id.etPassword);
		bLogin=(Button) findViewById(R.id.bLogin);
		register=(TextView) findViewById(R.id.tvRegister);
		bLogin.setOnClickListener(this);
		register.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){

			case R.id.bLogin:
				String username1=username.getText().toString();
				String password1=password.getText().toString();

				if(username1.isEmpty() || password1.isEmpty() || password1.equals("")|| username1.equals("")){
					Toast.makeText(Login.this, "Field Cannot be Empty", Toast.LENGTH_SHORT).show();
				}else if(isNetworkAvailable()){
					makeLoginRequest(username1,password1);
				}else{
					Toast.makeText(Login.this, "Network Not Available", Toast.LENGTH_SHORT).show();
				}
				break;

			case R.id.tvRegister:
				username.setText("");
				password.setText("");
				startActivity(new Intent(Login.this,Register.class));
				break;
		}
	}

	private void showErrorMessage() {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		//builder.setTitle("");
		builder.setMessage("Incorrect User Detail");
		builder.setPositiveButton("Ok", null);
		builder.show();
	}

	private void makeLoginRequest(final String user,final String pass)  {
		final ProgressDialog dialog = ProgressDialog.show(Login.this, "", "Processing...", true);
		RequestQueue mRequestQueue;
		Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
		Network network = new BasicNetwork(new HurlStack());
		mRequestQueue = new RequestQueue(cache, network);
		mRequestQueue.start();

		String url = "http://kps.comli.com/FetchCloud.php";

		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						dialog.dismiss(); //pDialog.hide();
						try {
							JSONObject objec =new JSONObject(response);
							Log.d("kamta",""+ objec.getString("name")+"\n"
											+ objec.getString("age")+"\n"
											+ objec.getString("email"));
							Toast.makeText(Login.this,"Success",Toast.LENGTH_LONG).show();
							storeAndMoveToMain(objec);
						} catch (JSONException e) {
							Log.d("kamta","23JSONException Respongse = "+ response+"\n Exception"+e);
							Toast.makeText(Login.this,"Wrong Username or Password",Toast.LENGTH_LONG).show();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						dialog.dismiss(); //pDialog.hide();
						showErrorMessage();
						Toast.makeText(Login.this,"Failed",Toast.LENGTH_LONG).show();
						Log.d("kamta", "String Respongse Error: " + error.getMessage());
						VolleyLog.d("kamta", "Volley String Respongse Error: " + error.getMessage());
					}
				}){
			@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("username",user);
				params.put("password",pass);
				Log.d("kamta", "Data posting... username = "+user+" \npassword = "+pass);
				return params;
			}
		};
		mRequestQueue.add(stringRequest);
		//StringRequest f= new StringRequest(Request.Method.POST,url,new Response<String>())
	}

	private void storeAndMoveToMain(JSONObject obj) {
		SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
		SharedPreferences.Editor editor =sp.edit();
		try {
			editor.putString("name",obj.getString("name"));
			editor.putString("username",obj.getString("username"));
			editor.putString("age",obj.getString("age"));
			editor.putString("email",obj.getString("email"));
			editor.putBoolean("status",true);
		} catch (JSONException e) {
			Log.d("kamta","SharedPreference JSONException435");
		}finally {
			editor.apply();
		}
		finish();
		startActivity(new Intent(Login.this,MainActivity.class));
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}



