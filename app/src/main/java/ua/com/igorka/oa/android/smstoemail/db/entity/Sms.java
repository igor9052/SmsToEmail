package ua.com.igorka.oa.android.smstoemail.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Igor Kuzmenko on 06.04.2015.
 */
public final class Sms implements Parcelable {

    private int id;
    private String originatingAddress;
    private String displayOriginatingAddress;
    private String displayMessageBody;
    private String timestamp;

    public Sms(int id, String originatingAddress, String displayOriginatingAddress, String displayMessageBody, String timestamp) {
        this.id = id;
        this.originatingAddress = originatingAddress;
        this.displayOriginatingAddress = displayOriginatingAddress;
        this.displayMessageBody = displayMessageBody;
        this.timestamp = timestamp;
    }

    public Sms() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginatingAddress() {
        return originatingAddress;
    }

    public void setOriginatingAddress(String originatingAddress) {
        this.originatingAddress = originatingAddress;
    }

    public String getDisplayOriginatingAddress() {
        return displayOriginatingAddress;
    }

    public void setDisplayOriginatingAddress(String displayOriginatingAddress) {
        this.displayOriginatingAddress = displayOriginatingAddress;
    }

    public String getDisplayMessageBody() {
        return displayMessageBody;
    }

    public void setDisplayMessageBody(String displayMessageBody) {
        this.displayMessageBody = displayMessageBody;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.originatingAddress);
        dest.writeString(this.displayOriginatingAddress);
        dest.writeString(this.displayMessageBody);
        dest.writeString(this.timestamp);
    }

    @Override
    public String toString() {
        return "Sms{" +
                "id=" + id +
                ", originatingAddress='" + originatingAddress + '\'' +
                ", displayOriginatingAddress='" + displayOriginatingAddress + '\'' +
                ", displayMessageBody='" + displayMessageBody + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    private Sms(Parcel in) {
        this.id = in.readInt();
        this.originatingAddress = in.readString();
        this.displayOriginatingAddress = in.readString();
        this.displayMessageBody = in.readString();
        this.timestamp = in.readString();
    }

    public static final Parcelable.Creator<Sms> CREATOR = new Parcelable.Creator<Sms>() {
        public Sms createFromParcel(Parcel source) {
            return new Sms(source);
        }

        public Sms[] newArray(int size) {
            return new Sms[size];
        }
    };
}
