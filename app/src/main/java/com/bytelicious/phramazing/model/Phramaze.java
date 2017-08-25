package com.bytelicious.phramazing.model;

import org.parceler.Parcel;

import java.util.Date;
import java.util.UUID;

import io.realm.PhramazeRealmProxy;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

/**
 * @author ylyubenov
 */
@Parcel(implementations = { PhramazeRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { Phramaze.class })
public class Phramaze extends RealmObject {

    private String phrase;
    private Date creationDate;
    private String author;
    @LinkingObjects("phramazes")
    private final RealmResults<User> users = null;

    @PrimaryKey
    private String id;

    public Phramaze() {
        id = UUID.randomUUID().toString();
        creationDate = new Date();
    }

    public String getPhrase() {
        return phrase;
    }
    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public String getId() {
        return id;
    }
    public RealmResults<User> getUsers() {
        return users;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
}
