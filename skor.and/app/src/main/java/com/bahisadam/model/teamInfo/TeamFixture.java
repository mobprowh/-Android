package com.bahisadam.model.teamInfo;


import android.os.Parcel;
import android.os.Parcelable;

public class TeamFixture implements Parcelable{

    private String mHomeTeamName;
    private String mHomeTeamImageUrl;
    private String mAwayTeamName;
    private String mAwayTeamImageUrl;
    private String mDate;
    private String mLeagueName;
    private String mLeagueId;
    private String mMatchId;

    public TeamFixture() {
    }

    public String getMatchId() {
        return mMatchId;
    }

    public void setMatchId(String matchId) {
        mMatchId = matchId;
    }

    protected TeamFixture(Parcel in) {
        mHomeTeamName = in.readString();
        mHomeTeamImageUrl = in.readString();
        mAwayTeamName = in.readString();
        mAwayTeamImageUrl = in.readString();
        mDate = in.readString();
        mLeagueName = in.readString();
        mLeagueId = in.readString();
        mMatchId = in.readString();
    }

    public static final Creator<TeamFixture> CREATOR = new Creator<TeamFixture>() {
        @Override
        public TeamFixture createFromParcel(Parcel in) {
            return new TeamFixture(in);
        }

        @Override
        public TeamFixture[] newArray(int size) {
            return new TeamFixture[size];
        }
    };

    public String getHomeTeamName() {
        return mHomeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        mHomeTeamName = homeTeamName;
    }

    public String getHomeTeamImageUrl() {
        return mHomeTeamImageUrl;
    }

    public void setHomeTeamImageUrl(String homeTeamImageUrl) {
        mHomeTeamImageUrl = homeTeamImageUrl;
    }

    public String getAwayTeamName() {
        return mAwayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        mAwayTeamName = awayTeamName;
    }

    public String getAwayTeamImageUrl() {
        return mAwayTeamImageUrl;
    }

    public void setAwayTeamImageUrl(String awayTeamImageUrl) {
        mAwayTeamImageUrl = awayTeamImageUrl;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getLeagueName() {
        return mLeagueName;
    }

    public void setLeagueName(String leagueName) {
        mLeagueName = leagueName;
    }

    public String getLeagueId() {
        return mLeagueId;
    }

    public void setLeagueId(String leagueId) {
        mLeagueId = leagueId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mHomeTeamName);
        parcel.writeString(mHomeTeamImageUrl);
        parcel.writeString(mAwayTeamName);
        parcel.writeString(mAwayTeamImageUrl);
        parcel.writeString(mDate);
        parcel.writeString(mLeagueName);
        parcel.writeString(mLeagueId);
        parcel.writeString(mMatchId);
    }
}
