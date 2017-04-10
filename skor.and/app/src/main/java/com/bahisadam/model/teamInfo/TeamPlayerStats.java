package com.bahisadam.model.teamInfo;


import android.os.Parcel;
import android.os.Parcelable;

public class TeamPlayerStats implements Parcelable{

    private int mCount;
    private String mName;
    private String mEvent;
    private String mID;
    private String mLeagueId;
    private String mLeagueName;

    protected TeamPlayerStats(Parcel in) {
        mCount = in.readInt();
        mName = in.readString();
        mEvent = in.readString();
        mID = in.readString();
        mLeagueId = in.readString();
        mLeagueName = in.readString();
    }

    public TeamPlayerStats() {
    }

    public static final Creator<TeamPlayerStats> CREATOR = new Creator<TeamPlayerStats>() {
        @Override
        public TeamPlayerStats createFromParcel(Parcel in) {
            return new TeamPlayerStats(in);
        }

        @Override
        public TeamPlayerStats[] newArray(int size) {
            return new TeamPlayerStats[size];
        }
    };

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEvent() {
        return mEvent;
    }

    public void setEvent(String event) {
        mEvent = event;
    }

    public String getLeagueId() {
        return mLeagueId;
    }

    public void setLeagueId(String leagueId) {
        mLeagueId = leagueId;
    }

    public String getLeagueName() {
        return mLeagueName;
    }

    public void setLeagueName(String leagueName) {
        mLeagueName = leagueName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mCount);
        parcel.writeString(mName);
        parcel.writeString(mEvent);
        parcel.writeString(mLeagueId);
        parcel.writeString(mLeagueName);
        parcel.writeString(mID);
    }
}
