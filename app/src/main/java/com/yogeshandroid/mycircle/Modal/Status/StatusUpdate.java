package com.yogeshandroid.mycircle.Modal.Status;

import android.net.Uri;

public class StatusUpdate {
    private Uri uri;
    private String name;
    private String uId;

    public StatusUpdate(Uri uri, String name, String uId) {
        this.uri = uri;
        this.name = name;
        this.uId = uId;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
