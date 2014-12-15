package com.android.dutluk;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;

import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;



public class TimelineActivity extends Activity {

	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_INFO = "info";

	ActionBar actionBar;
	MenuInflater inflater;
	Menu m;

	ListView lv;
	ArrayList<HashMap<String, String>> alist = new ArrayList<HashMap<String, String>>();

	ProgressDialog dialog;
	Boolean isInput = true;
	LinearLayout ll;
	String searchedTerm;
	String type = "";
	String type_send = "";
	ArrayList<Integer> storyIDs = new ArrayList<Integer>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);

		Intent comingIntent = getIntent();
		type = comingIntent.getStringExtra("type");

		if(type.equals("myStories")){
			type_send = ""+0;
		}
		if(type.equals("placesStories")){
			type_send = comingIntent.getStringExtra("placeID");
		}
		if(type.equals("subsStories")){
			type_send = ""+1;
		}


		lv = (ListView) findViewById(R.id.listView1);


		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(16, 188, 201)));
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		new MyTask().execute();

	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private void readData() {


		TimelineAdapter adapter = new TimelineAdapter(this, alist);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				try {
					if(type_send.equals("0") || type_send.equals("1"))
						startShowStoryActivity(""+ storyIDs.get(arg2));
					else 
						navigateToTimelineActivityForPlaces(""+storyIDs.get(arg2));
				} catch (Exception e) {
					// TODO: handle exception
					String data = e.getMessage();
				}

			}
		});
		lv.setLongClickable(true);
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				return isInput;

			}
		});

	}
	private class MyTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			readData();
		}

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient httpclient = new DefaultHttpClient();
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("email", Utility.userName));
			pairs.add(new BasicNameValuePair("type", type_send));
			String url = Utility.SERVER_NAME + "GetStory?" + URLEncodedUtils.format(pairs, "utf-8");
			Log.e("url",url);
			HttpGet httpget = new HttpGet(url);
			//Log.w("submit", "aa" + url);
			try {
				storyIDs.clear();
				HttpResponse getResponse = httpclient.execute(httpget);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						getResponse.getEntity().getContent()));
				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}

				Log.e("submit", result.toString());
				JSONArray jsonarray = new JSONArray(result.toString());


				for(int i=0; i<jsonarray.length(); i++){
					JSONObject obj = jsonarray.getJSONObject(i);
					String content = obj.getString("content");
					String name = "";
					if(content.length()<50)
						name = content;
					else 
						name = content.substring(0,50)+ "...";
					String info = obj.getString("mail");
					int id = obj.getInt("storyId");
				
					HashMap<String, String> map = new HashMap<String, String>();
					storyIDs.add(id);
					map.put(KEY_ID,""+ id);
					map.put(KEY_TITLE, name);
					map.put(KEY_INFO, info);
					alist.add(map);
				}   



			} catch (ClientProtocolException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			} catch (JSONException e) {

				e.printStackTrace();
			}
			return null;

		}

	}
	public void  startShowStoryActivity(String story_id) {

		Intent showStoryIntent = new Intent(getApplicationContext(),ShowStoryActivity.class);
		Bundle b = new Bundle();
		b.putString("story_id",story_id);
		showStoryIntent.putExtras(b);
		// Clears History of Activity
		showStoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(showStoryIntent);
	}
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		final android.widget.SearchView searchView = (android.widget.SearchView) menu
				.findItem(R.id.search).getActionView();
		searchView.setSubmitButtonEnabled(true);
		if(type_send.equals("0") || type_send.equals("1") )
			searchView.setQueryHint("Search Story");
		else 
			searchView.setQueryHint("Search Place");	
		searchView
		.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {

				AsyncHttpClient client = new AsyncHttpClient();
				alist.clear();
				if(type_send.equals("0") || type_send.equals("1") ){
					
					client.post(Utility.SERVER_NAME + "Search?func=story&term=" + query, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							storyIDs.clear();
							try {
								JSONArray jsonarray = new JSONArray(response);


								for(int i=0; i<jsonarray.length(); i++){
									JSONObject obj = jsonarray.getJSONObject(i);

									String content = obj.getString("Name");
									String name = "";
									if(content.length()<50)
										name = content;
									else 
										name = content.substring(0,50)+ "...";
									String info = obj.getString("mail");
									int id = obj.getInt("storyId");
									HashMap<String, String> map = new HashMap<String, String>();
									storyIDs.add(id);
									map.put(KEY_ID, "" + id);
									map.put(KEY_TITLE, name);
									map.put(KEY_INFO, info);
									alist.add(map);
								}   
								readData();

							} catch (JSONException e) {

								e.printStackTrace();
							}
						}
					});
				}
				else {
					
					client.post(Utility.SERVER_NAME + "Search?func=place&term=" + query, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							try {
								storyIDs.clear();
								JSONArray jsonarray = new JSONArray(response);
								Log.e("response",response);

								for(int i=0; i<jsonarray.length(); i++){
									JSONObject obj = jsonarray.getJSONObject(i);

									String content = obj.getString("Name");
									String name = "";
									if(content.length()<50)
										name = content;
									else 
										name = content.substring(0,50)+ "...";
									String lat = obj.getString("Latitude");
									String longi = obj.getString("Longtitude");
									int id = obj.getInt("PlaceID");
									HashMap<String, String> map = new HashMap<String, String>();
									storyIDs.add(id);
									map.put(KEY_ID, "" + id);
									map.put(KEY_TITLE, name);
									map.put(KEY_INFO, "Lat: " + lat + "  Long: " + longi);
									alist.add(map);
								}   
								readData();

							} catch (JSONException e) {

								e.printStackTrace();
							}
						}
					});
				}
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

				return false;
			}
		});
		MenuItem menuItem = menu.findItem(R.id.search);
		m = menu;
		menuItem.setOnActionExpandListener(new OnActionExpandListener() {

			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {

				m.findItem(R.id.addStory);
				m.findItem(R.id.addStory).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				m.findItem(R.id.map);
				m.findItem(R.id.map).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				m.findItem(R.id.ownProfile);
				m.findItem(R.id.ownProfile).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {


				m.findItem(R.id.addStory);
				m.findItem(R.id.addStory).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				m.findItem(R.id.map);
				m.findItem(R.id.map).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				m.findItem(R.id.ownProfile);
				m.findItem(R.id.ownProfile).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

				return true;
			}
		});
		return super.onCreateOptionsMenu(menu);

	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.search:			
			break;
		case R.id.addStory:
			navigateToAddStoryActivity();
			break;
		case R.id.map:
			navigateToHomeMapActivity();
			break;
		case R.id.ownProfile:
			navigateToProfileActivity();
			break;
		case R.id.advancedSearch:
			navigateToAdvancedSearchActivity();
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	public void navigateToSearchActivity() {
		Intent searchIntent = new Intent(getApplicationContext(),AdvancedSearchActivity.class);
		searchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(searchIntent);

	}
	public void navigateToAddStoryActivity() {
		Intent addStoryIntent = new Intent(getApplicationContext(),AddStoryActivity.class);
		addStoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(addStoryIntent);

	}
	public void navigateToHomeMapActivity() {
		Intent homeMapIntent = new Intent(getApplicationContext(),HomeMapActivity.class);
		homeMapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(homeMapIntent);

	}
	public void navigateToProfileActivity() {
		Intent profileIntent = new Intent(getApplicationContext(),ProfileActivity.class);
		profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(profileIntent);

	}
	public void navigateToAdvancedSearchActivity() {
		Intent searchIntent = new Intent(getApplicationContext(),AdvancedSearchActivity.class);
		searchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(searchIntent);
		
	}
	public void navigateToTimelineActivityForPlaces(String placeID){
		Intent timelineIntent = new Intent(getApplicationContext(),TimelineActivity.class);
		Bundle b = new Bundle();
		b.putString("type","placesStories");
		b.putString("placeID",placeID);
		timelineIntent.putExtras(b);
		timelineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(timelineIntent);
	}


}
