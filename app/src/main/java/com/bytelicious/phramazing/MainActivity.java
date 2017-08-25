package com.bytelicious.phramazing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.Toast;

import com.bytelicious.phramazing.app.PhramazingActivity;
import com.bytelicious.phramazing.home.HomeFragment;
import com.bytelicious.phramazing.model.User;
import com.bytelicious.phramazing.trending.TrendingFragment;

public class MainActivity extends PhramazingActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {


    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance())
                        .commit();
                setTitle(realmInstance.where(User.class).findFirst().getUsername());
                return true;
            case R.id.navigation_trending:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, TrendingFragment.newInstance())
                        .commit();
                setTitle(R.string.title_trending);
                return true;
            case R.id.navigation_notifications:
                Toast.makeText(this, "Under construction", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

}