package co.mobilemakers.contactstudio2;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

/**
 * Contact data model
 *
 * Created by ariel.cattaneo on 05/02/2015.
 */
public class ContactModel implements Parcelable {

    public final static String ID = "_id";
    public final static String FIRST_NAME = "firstName";
    public final static String LAST_NAME = "lastName";
    public final static String NICKNAME = "nickname";
    public final static String PICTURE = "picture";

    @DatabaseField(generatedId = true, columnName = ID) private int id;
    @DatabaseField(columnName = FIRST_NAME) protected String mFirstName;
    @DatabaseField(columnName = LAST_NAME) protected String mLastName;
    @DatabaseField(columnName = NICKNAME) protected String mNickname;
    @DatabaseField(columnName = PICTURE) protected String mPhotoURL;

    public static final Creator<ContactModel> CREATOR = new Creator<ContactModel>() {
        @Override
        public ContactModel createFromParcel(Parcel source) {
            return new ContactModel(source);
        }

        @Override
        public ContactModel[] newArray(int size) {
            return new ContactModel[size];
        }
    };

    ContactModel() {

    }

    ContactModel(Parcel source) {
        mFirstName = source.readString();
        mLastName = source.readString();
        mNickname = source.readString();
        mPhotoURL = source.readString();
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public String getName() {
        return mFirstName + " " + mLastName;
    }

    public String getNickname() {
        return mNickname;
    }

    public void setNickname(String nickname) {
        this.mNickname = nickname;
    }

    public String getPhotoURL() {
        return mPhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.mPhotoURL = photoURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mNickname);
        dest.writeString(mPhotoURL);
    }
}
