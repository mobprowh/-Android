package com.bahisadam.model.teamInfo;


import android.os.Parcel;
import android.os.Parcelable;

public class Team implements Parcelable{

    private String mId;
    private String mName;
    private String mLogoPath;
    private String mStadiumImageUrl;
    private String mStadiumName;
    private String mStadiumCapacity;
    private String mStadiumYearBuilt;
    private String mClubOfficialName;
    private String mYearFounded;
    private String mClubHead;
    private String mCoach;
    private String mWebsite;
    private String mTwitter;



    public Team() {
    }

    protected Team(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mLogoPath = in.readString();
        mStadiumImageUrl = in.readString();
        mStadiumName = in.readString();
        mStadiumCapacity = in.readString();
        mStadiumYearBuilt = in.readString();
        mClubOfficialName = in.readString();
        mYearFounded = in.readString();
        mClubHead = in.readString();
        mCoach = in.readString();
        mWebsite = in.readString();
        mTwitter = in.readString();
    }

    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLogoPath() {
        return mLogoPath;
    }

    public void setLogoPath(String logoPath) {
        mLogoPath = logoPath;
    }

    public String getStadiumImageUrl() {
        return mStadiumImageUrl;
    }

    public void setStadiumImageUrl(String stadiumImageUrl) {
        mStadiumImageUrl = stadiumImageUrl;
    }

    public String getStadiumName() {
        return mStadiumName;
    }

    public void setStadiumName(String stadiumName) {
        mStadiumName = stadiumName;
    }

    public String getStadiumCapacity() {
        return mStadiumCapacity;
    }

    public void setStadiumCapacity(String stadiumCapacity) {
        mStadiumCapacity = stadiumCapacity;
    }

    public String getStadiumYearBuilt() {
        return mStadiumYearBuilt;
    }

    public void setStadiumYearBuilt(String stadiumYearBuilt) {
        mStadiumYearBuilt = stadiumYearBuilt;
    }

    public String getClubOfficialName() {
        return mClubOfficialName;
    }

    public void setClubOfficialName(String clubOfficialName) {
        mClubOfficialName = clubOfficialName;
    }

    public String getYearFounded() {
        return mYearFounded;
    }

    public void setYearFounded(String yearFounded) {
        mYearFounded = yearFounded;
    }

    public String getClubHead() {
        return mClubHead;
    }

    public void setClubHead(String clubHead) {
        mClubHead = clubHead;
    }

    public String getCoach() {
        return mCoach;
    }

    public void setCoach(String coach) {
        mCoach = coach;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        mWebsite = website;
    }

    public String getTwitter() {
        return mTwitter;
    }

    public void setTwitter(String twitter) {
        mTwitter = twitter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mName);
        parcel.writeString(mLogoPath);
        parcel.writeString(mStadiumImageUrl);
        parcel.writeString(mStadiumName);
        parcel.writeString(mStadiumCapacity);
        parcel.writeString(mStadiumYearBuilt);
        parcel.writeString(mClubOfficialName);
        parcel.writeString(mYearFounded);
        parcel.writeString(mClubHead);
        parcel.writeString(mCoach);
        parcel.writeString(mWebsite);
        parcel.writeString(mTwitter);
    }
}
