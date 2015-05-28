package com.heliopolis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class ListActivity extends ActionBarActivity {
    private LoginManager loginManager;
    BusinessAdapter dataAdapter = null;

    public void enableStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, MyService.class));
        loginManager = LoginManager.getInstance();
        setContentView(R.layout.activity_list);
        displayListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            loginManager.logOut();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayListView() {
        //queries server to get businesses and subscription
        enableStrictMode();
        ArrayList<Business> businessList = new ArrayList<>();
        HttpClient client = new DefaultHttpClient();
        String device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        HttpGet request = new HttpGet("http://5eef8957.ngrok.io/businesses?device_id=" + device_id);
        HttpResponse response;
        try {
            response = client.execute(request);
            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                stringBuilder.append(line);
            }
            line = stringBuilder.toString();
            JSONArray jsonArr = new JSONArray(line);

            for (int i = 0 ; i < jsonArr.length(); i++) {
                JSONObject obj = jsonArr.getJSONObject(i);
                String name = (String)obj.get("name");
                int id = (int)obj.get("id");
                boolean subscribed = (boolean)obj.get("subscribed");
                Business b = new Business(id, name, subscribed);
                businessList.add(b);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        dataAdapter = new BusinessAdapter(this, R.layout.business_layout, businessList);
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });

    }

    private class BusinessAdapter extends ArrayAdapter<Business> {

        private ArrayList<Business> businessList;

        public BusinessAdapter(Context context,
                               int textViewResourceId,
                               ArrayList<Business> businessList) {
            super(context, textViewResourceId, businessList);
            this.businessList = new ArrayList<>();
            this.businessList.addAll(businessList);
        }

        private class ViewHolder {
            TextView name;
            CheckBox checkedIn;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View businessView, ViewGroup parent) {

            ViewHolder holder = null;

            if (businessView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                businessView = vi.inflate(R.layout.business_layout, null);

                holder = new ViewHolder();
                holder.name = (TextView) businessView.findViewById(R.id.name);
                holder.checkedIn = (CheckBox) businessView.findViewById(R.id.checkBox);
                businessView.setTag(holder);

                holder.checkedIn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    final CheckBox cb = (CheckBox) v;
                    final Business business = (Business) cb.getTag();
                    business.checkedIn = cb.isChecked();
                    final String business_id = Integer.toString(business.business_id);
                    final String device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    new Thread(new Runnable() {
                        public void run() {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost("http://5eef8957.ngrok.io/subscriptions/update");
                        ArrayList<NameValuePair> postParameters;
                        postParameters = new ArrayList<NameValuePair>();
                        postParameters.add(new BasicNameValuePair("business_id", business_id));
                        postParameters.add(new BasicNameValuePair("device_id", device_id));
                        try{
                            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
                        }
                        catch (Exception e) {
                        }
                        try {
                            HttpResponse response = httpClient.execute(httpPost);
                            HttpEntity respEntity = response.getEntity();
                            if (respEntity != null) {
                            }
                        }
                        catch (Exception e) {
                        }
                        }
                    }).start();
                    }
                });
            }
            else {
                holder = (ViewHolder) businessView.getTag();
            }
            Business business = businessList.get(position);
            holder.name.setText(business.businessName);
            holder.checkedIn.setChecked(business.checkedIn);
            holder.checkedIn.setTag(business);
            return businessView;
        }

    }
}
