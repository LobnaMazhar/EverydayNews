package com.example.lobna.everydayNews.Menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.lobna.everydayNews.Activity.SettingsActivity;
import com.example.lobna.everydayNews.R;

/**
 * Created by Lobna on 26-Jan-17.
 */

public class Menu extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);

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
            startActivity(new Intent(Menu.this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
