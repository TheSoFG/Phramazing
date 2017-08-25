package com.bytelicious.phramazing.model;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * @author ylyubenov
 */
@RealmClass
public class User implements RealmModel {

    @PrimaryKey
    private String id;
    private String username;
    private RealmList<Phramaze> phramazes;
    private Phramaze phramaze;

    public User() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public RealmList<Phramaze> getPhramazes() {
        return phramazes;
    }
    public void setPhramazes(RealmList<Phramaze> phramazes) {
        this.phramazes = phramazes;
    }
}
