package com.aadu_maadu_kozhi.gregantech.model.pojo;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class Like {
    private String id;
    private String liked_user_id;
    private String post_id;
    private String post_user_id;
    private boolean isSeen;
    @ServerTimestamp
    private Timestamp post_liked_date_and_time = null;

    public String getPost_user_id() {
        return post_user_id;
    }

    public void setPost_user_id(String post_user_id) {
        this.post_user_id = post_user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof Like) {
            Like ptr = (Like) v;
            retVal = ptr.liked_user_id.equalsIgnoreCase(this.liked_user_id);
        }

        return retVal;
    }

    public String getLiked_user_id() {
        return liked_user_id;
    }

    public void setLiked_user_id(String liked_user_id) {
        this.liked_user_id = liked_user_id;
    }

    public Timestamp getPost_liked_date_and_time() {
        return post_liked_date_and_time;
    }

    public void setPost_liked_date_and_time(Timestamp post_liked_date_and_time) {
        this.post_liked_date_and_time = post_liked_date_and_time;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
