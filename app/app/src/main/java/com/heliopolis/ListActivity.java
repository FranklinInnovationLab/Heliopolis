package com.heliopolis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends ActionBarActivity {

    BusinessAdapter dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
