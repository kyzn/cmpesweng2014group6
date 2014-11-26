package com.android.dutluk;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class ProfileActivity extends Activity {

	ProgressDialog prgDialog;
	TextView errorMsg;
	
	EditText nameET;
	EditText birthDateET;
	EditText genderET;
	TextView mailTV;
	EditText phoneET;
	TextView xpTV;
	TextView levelTV;
	EditText bioET;

	String mail = "";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        
        errorMsg = (TextView)findViewById(R.id.errorProfile);
     	// Instantiate Progress Dialog object
 		prgDialog = new ProgressDialog(this);
 		// Set Progress Dialog Text
	    prgDialog.setMessage("Please wait...");
	    // Set Cancelable as False
	    prgDialog.setCancelable(false);
         
     	nameET = (EditText)findViewById(R.id.nameProfile);
     	birthDateET = (EditText)findViewById(R.id.birthDateProfile);
     	genderET =  (EditText)findViewById(R.id.genderProfile);
     	mailTV =  (TextView)findViewById(R.id.emailProfile);
     	phoneET = (EditText)findViewById(R.id.phoneProfile);
     	xpTV = (TextView)findViewById(R.id.xpProfile);
     	levelTV = (TextView)findViewById(R.id.levelProfile);
     	bioET = (EditText)findViewById(R.id.biographyProfile);
     	
 
        
        Intent loginIntent = getIntent();
        mail = loginIntent.getStringExtra("mail");
        
     	RequestParams params = new RequestParams();
		params.put("mail", mail);
		invokeWSforGET(params);

    }

	public void invokeWSforGET(RequestParams params){
		// Show Progress Dialog 
		prgDialog.show();
		// Make RESTful webservice call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.43.17:9999/useraccount/profile/getProfile",params ,new AsyncHttpResponseHandler() {
        	// When the response returned by REST has Http response code '200'
             @Override
             public void onSuccess(String response) {
            	// Hide Progress Dialog
            	 prgDialog.hide();
                 try {
                	 	 // JSON Object
                         JSONObject obj = new JSONObject(response);
                         // When the JSON response has status boolean value assigned with true
                         if(obj.getBoolean("status")){
                        	 // Set Default Values for Edit View controls
                        	 setDefaultValues(obj);
                        	 // Display successfully registered message using Toast
                        	 Toast.makeText(getApplicationContext(), "You can update your profile!", Toast.LENGTH_LONG).show();
                         } 
                         // Else display error message
                         else{
                        	 errorMsg.setText(obj.getString("error_msg"));
                        	 Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                         }
                 } catch (JSONException e) {
                     // TODO Auto-generated catch block
                     Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                     e.printStackTrace();
                     
                 }
             }
             // When the response returned by REST has Http response code other than '200'
             @Override
             public void onFailure(int statusCode, Throwable error,
                 String content) {
                 // Hide Progress Dialog
                 prgDialog.hide();
                 // When Http response code is '404'
                 if(statusCode == 404){
                     Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                 } 
                 // When Http response code is '500'
                 else if(statusCode == 500){
                     Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                 } 
                 // When Http response code other than 404, 500
                 else{
                     Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                 }
             }
         });
	}
	/**
	 * Set default values for Edit View controls
	 * @throws JSONException 
	 */
	public void setDefaultValues(JSONObject obj) throws JSONException{
		
		nameET.setText(obj.getString("name"));
		birthDateET.setText(obj.getString("birthDate"));
		genderET.setText(obj.getString("gender"));
		mailTV.setText(obj.getString("mail"));
		phoneET.setText(obj.getString("phone"));
		xpTV.setText(obj.getString("experiencePoint"));
		levelTV.setText(obj.getString("level"));
		bioET.setText(obj.getString("bio"));

	}
	

	public void saveUser(View view){

		String name = nameET.getText().toString();
		String birthDate = birthDateET.getText().toString();
		String gender = genderET.getText().toString();
		String phone = phoneET.getText().toString();
		String bio = bioET.getText().toString();

		RequestParams params = new RequestParams();
		params.put("name", name);
		params.put("birthDate", birthDate);
		params.put("gender", gender);
		params.put("phone", phone);
		params.put("bio", bio);				
		invokeWSforSAVE(params);

	}    

	public void invokeWSforSAVE(RequestParams params){
		// Show Progress Dialog 
		prgDialog.show();
		// Make RESTful web service call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.43.17:9999/useraccount/profile/updateProfile",params ,new AsyncHttpResponseHandler() {
        	// When the response returned by REST has Http response code '200'
             @Override
             public void onSuccess(String response) {
            	// Hide Progress Dialog
            	 prgDialog.hide();
                 try {
                	 	 // JSON Object
                         JSONObject obj = new JSONObject(response);
                         // When the JSON response has status boolean value assigned with true
                         if(obj.getBoolean("status")){
                        	 // Set Default Values for Edit View controls
                        	 //setDefaultValues(obj);
                        	 // Display successfully registered message using Toast
                        	 Toast.makeText(getApplicationContext(), "You are successfully updated your profile!", Toast.LENGTH_LONG).show();
                         } 
                         // Else display error message
                         else{
                        	 errorMsg.setText(obj.getString("error_msg"));
                        	 Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                         }
                 } catch (JSONException e) {
                     // TODO Auto-generated catch block
                     Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                     e.printStackTrace();
                     
                 }
             }
             // When the response returned by REST has HTTP response code other than '200'
             @Override
             public void onFailure(int statusCode, Throwable error,
                 String content) {
                 // Hide Progress Dialog
                 prgDialog.hide();
                 // When HTTP response code is '404'
                 if(statusCode == 404){
                     Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                 } 
                 // When HTTP response code is '500'
                 else if(statusCode == 500){
                     Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                 } 
                 // When HTTP response code other than 404, 500
                 else{
                     Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                 }
             }
         });
	}

}
