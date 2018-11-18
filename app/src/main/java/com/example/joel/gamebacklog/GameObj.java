package com.example.joel.gamebacklog;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

@Entity(tableName = "gameobj")
public class GameObj implements Serializable {

    @PrimaryKey (autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String mTitle;
    @ColumnInfo(name = "device")
    private String mDevice;
    @ColumnInfo(name = "notes")
    private String mNotes;
    @ColumnInfo(name = "status")
    private String mStatus;

    public GameObj(String title, String device, String notes, String status) {
        this.mTitle = title;
        this.mDevice = device;
        this.mNotes = notes;
        this.mStatus = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDevice() {
        return mDevice;
    }

    public String getNotes() {
        return mNotes;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setDevice(String device) {
        this.mDevice = device;
    }

    public void setNotes(String notes) {
        this.mNotes = notes;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }
}
