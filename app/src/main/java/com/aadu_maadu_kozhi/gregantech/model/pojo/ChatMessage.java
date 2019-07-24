package com.aadu_maadu_kozhi.gregantech.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonIgnore;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.List;

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class ChatMessage implements Parcelable {

    private String chat_id;
    private List<String> chat_users;
    private String senderId;
    private String senderName;
    private String message;
    private boolean read;
    @JsonIgnore
    @ServerTimestamp
    private Timestamp chat_message_date_and_time = null;


    protected ChatMessage(Parcel in) {
        chat_id = in.readString();
        if (in.readByte() == 0x01) {
            chat_users = new ArrayList<String>();
            in.readList(chat_users, String.class.getClassLoader());
        } else {
            chat_users = null;
        }
        senderId = in.readString();
        senderName = in.readString();
        message = in.readString();
        read = in.readByte() != 0x00;
        chat_message_date_and_time = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chat_id);
        if (chat_users == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(chat_users);
        }
        dest.writeString(senderId);
        dest.writeString(senderName);
        dest.writeString(message);
        dest.writeByte((byte) (read ? 0x01 : 0x00));
        dest.writeValue(chat_message_date_and_time);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ChatMessage> CREATOR = new Parcelable.Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };
    public ChatMessage() {
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public List<String> getChat_users() {
        return chat_users;
    }

    public void setChat_users(List<String> chat_users) {
        this.chat_users = chat_users;
    }

    public Timestamp getChat_message_date_and_time() {
        return chat_message_date_and_time;
    }

    public void setChat_message_date_and_time(Timestamp chat_message_date_and_time) {
        this.chat_message_date_and_time = chat_message_date_and_time;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}