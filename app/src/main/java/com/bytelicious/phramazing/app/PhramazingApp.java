package com.bytelicious.phramazing.app;

import android.app.Application;

import com.bytelicious.phramazing.model.Phramaze;
import com.bytelicious.phramazing.model.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

/**
 * @author ylyubenov
 */

public class PhramazingApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
//                .modules(new CustomRealmModule())
                .build();

        Realm.setDefaultConfiguration(config);

        addMockData();
    }

    private void addMockData() {
        try (Realm realmInstance = Realm.getDefaultInstance()) {
            if (realmInstance.where(User.class).findFirst() == null) {
                User user = new User();
                String me = "Dhaka";
                user.setUsername(me);
                RealmList<Phramaze> phramazes = new RealmList<>();
                Phramaze phramaze = new Phramaze();
                phramaze.setAuthor(me);
                phramaze.setPhrase("Stuff happens because things occur");
                phramazes.add(phramaze);
                phramaze = new Phramaze();
                phramaze.setAuthor(me);
                phramaze.setPhrase("I knew there was something wrong with some things, but...");
                phramazes.add(phramaze);
                user.setPhramazes(phramazes);
                realmInstance.executeTransaction(realm -> realm.insertOrUpdate(user));
            }
        }
    }

}