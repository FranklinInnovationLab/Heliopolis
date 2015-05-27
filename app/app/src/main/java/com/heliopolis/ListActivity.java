package com.heliopolis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListActivity extends ActionBarActivity {
    private CallbackManager callbackManager;

    BusinessAdapter dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //facebook stuff
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile", "user_friends", "user_birthday"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        System.out.println("123");
                        System.out.println(loginResult.getAccessToken());
                        System.out.println(loginResult.getRecentlyDeniedPermissions());
                        System.out.println(loginResult.getRecentlyGrantedPermissions());

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        Log.v("LoginActivity", response.toString());
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, name, email, gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        setContentView(R.layout.activity_list);
        displayListView();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayListView() {

        //Array list of countries
        ArrayList<Business> businessList = new ArrayList<>();
        Business b = new Business();
        b.businessName = "Shit";
        b.checkedIn = true;
        businessList.add(b);

        b = new Business();
        b.businessName = "Shit";
        b.checkedIn = true;

        ListView listView = (ListView) findViewById(R.id.listView);
        businessList.add(b);
        dataAdapter = new BusinessAdapter(this, R.layout.business_layout, businessList);
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: ",
                        Toast.LENGTH_LONG).show();
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
                        CheckBox cb = (CheckBox) v;
                        Business business = (Business) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + business.businessName +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        business.checkedIn = cb.isChecked();
                    }
                });
            } else {
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
