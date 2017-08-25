package com.bytelicious.phramazing.app;

import android.support.v7.app.AppCompatActivity;

import io.realm.Realm;

/**
 * @author ylyubenov
 */

public abstract class PhramazingActivity extends AppCompatActivity {

    protected Realm realmInstance;

    @Override
    public void onStart() {
        super.onStart();
        realmInstance = Realm.getDefaultInstance();
    }

    @Override
    public void onStop() {
        super.onStop();
        realmInstance.close();
    }

}