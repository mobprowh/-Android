package com.bahisadam.model.teamInfo;


import android.os.Parcel;
import android.os.Parcelable;

public class TeamPlayerInfo implements Parcelable{

    private String mSquadNumber;
    private String mName;
    private String mPosition;
    private String mGoals;
    private String mAssists;
    private String mCountryCode;
    private String mImageUrl;
    private String mPlayerId;

    public String getPlayerId() {
        return mPlayerId;
    }

    public void setPlayerId(String playerId) {
        mPlayerId = playerId;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public TeamPlayerInfo() {
    }

    protected TeamPlayerInfo(Parcel in) {
        mSquadNumber = in.readString();
        mName = in.readString();
        mPosition = in.readString();
        mGoals = in.readString();
        mAssists = in.readString();
        mCountryCode = in.readString();
        mImageUrl = in.readString();
        mPlayerId = in.readString();
    }

    public static final Creator<TeamPlayerInfo> CREATOR = new Creator<TeamPlayerInfo>() {
        @Override
        public TeamPlayerInfo createFromParcel(Parcel in) {
            return new TeamPlayerInfo(in);
        }

        @Override
        public TeamPlayerInfo[] newArray(int size) {
            return new TeamPlayerInfo[size];
        }
    };

    public String getSquadNumber() {
        return mSquadNumber;
    }

    public void setSquadNumber(String squadNumber) {
        if(squadNumber == null) mSquadNumber = "-";
        else mSquadNumber = squadNumber;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPosition() {
        return mPosition;
    }

    public void setPosition(String position) {
        mPosition = position;
    }

    public String getGoals() {
        return mGoals;
    }

    public void setGoals(String goals) {
        mGoals = goals;
    }

    public String getAssists() {
        return mAssists;
    }

    public void setAssists(String assists) {
        mAssists = assists;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mSquadNumber);
        parcel.writeString(mName);
        parcel.writeString(mPosition);
        parcel.writeString(mGoals);
        parcel.writeString(mAssists);
        parcel.writeString(mCountryCode);
        parcel.writeString(mImageUrl);
        parcel.writeString(mPlayerId);
    }
}
