package com.testcore.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.testcore.R;
import com.testcore.view.BetterSpinner;
import com.testcore.view.MaterialBetterSpinner;


public class BetterSpinnerActivity extends ActionBarActivity {


    BetterSpinner spinner1;
    MaterialBetterSpinner spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betterspinner);
        spinner1 = (BetterSpinner)findViewById(R.id.spinner1);
        spinner2 = (MaterialBetterSpinner)findViewById(R.id.spinner2);

        String[] list = getResources().getStringArray(R.array.month);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, list);

        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_visit_repo) {
//            Uri uri = Uri.parse("https://github.com/Lesilva/BetterSpinner");
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
//
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
