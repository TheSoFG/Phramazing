package com.bytelicious.phramazing.app;

import android.support.v4.app.Fragment;

import io.realm.Realm;

/**
 * @author ylyubenov
 */

public abstract class PhramazingFragment extends Fragment {

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