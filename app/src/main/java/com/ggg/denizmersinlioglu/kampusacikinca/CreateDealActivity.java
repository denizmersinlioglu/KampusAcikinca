package com.ggg.denizmersinlioglu.kampusacikinca;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class CreateDealActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deal);
        setupActionBar();
        setTitle("Create a deal");
    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.Create_new_deal:
                Intent createDeal_Intent = new Intent(this,CreateDealActivity.class);
                startActivity(createDeal_Intent);
                return true;
            case R.id.action_Settings:
                Intent settings_Intent = new Intent(this,SettingsActivity.class);
                startActivity(settings_Intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
