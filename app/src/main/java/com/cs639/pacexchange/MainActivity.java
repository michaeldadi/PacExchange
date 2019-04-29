package com.cs639.pacexchange;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set home page as initial view
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new ItemFragment()).commit();
        //On event tab is switched
        onTabSwitch();
        //Open login on application launch
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
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

        if (id == R.id.action_settings)
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void onTabSwitch() {
        BottomNavigationView mOnNavigationItemSelectedListener = findViewById(R.id.navigation);
        //Change view on bottomnavbar item click
        mOnNavigationItemSelectedListener.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ItemFragment()).commit();
                    break;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new InventoryFragment()).commit();
                    break;
                case R.id.navigation_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new UserProfileFragment()).commit();
                    break;
            }
            return true;
        });
    }
}
