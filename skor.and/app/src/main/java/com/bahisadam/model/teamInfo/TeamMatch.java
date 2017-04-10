package com.bahisadam.model.teamInfo;


import android.os.Parcel;
import android.os.Parcelable;

public class TeamMatch implements Parcelable{

    private String mHomeTeamName;
    private String mHomeTeamImageUrl;
    private String mHomeTeamGoals;
    private String mAwayTeamName;
    private String mAwayTeamImageUrl;
    private String mAwayTeamGoals;
    private String mDate;
    private String mLeagueName;
    private String mLeagueId;
    private String mMatchId;

    public String getMatchId() {
        return mMatchId;
    }

    public void setMatchId(String matchId) {
        mMatchId = matchId;
    }

    public TeamMatch() {
    }

    protected TeamMatch(Parcel in) {
        mHomeTeamName = in.readString();
        mHomeTeamImageUrl = in.readString();
        mHomeTeamGoals = in.readString();
        mAwayTeamName = in.readString();
        mAwayTeamImageUrl = in.readString();
        mAwayTeamGoals = in.readString();
        mDate = in.readString();
        mLeagueName = in.readString();
        mLeagueId = in.readString();
        mMatchId = in.readString();
    }

    public static final Creator<TeamMatch> CREATOR = new Creator<TeamMatch>() {
        @Override
        public TeamMatch createFromParcel(Parcel in) {
            return new TeamMatch(in);
        }

        @Override
        public TeamMatch[] newArray(int size) {
            return new TeamMatch[size];
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

    public String getHomeTeamGoals() {
        return mHomeTeamGoals;
    }

    public void setHomeTeamGoals(String homeTeamGoals) {
        mHomeTeamGoals = homeTeamGoals;
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

    public String getAwayTeamGoals() {
        return mAwayTeamGoals;
    }

    public void setAwayTeamGoals(String awayTeamGoals) {
        mAwayTeamGoals = awayTeamGoals;
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
        parcel.writeString(mHomeTeamGoals);
        parcel.writeString(mAwayTeamName);
        parcel.writeString(mAwayTeamImageUrl);
        parcel.writeString(mAwayTeamGoals);
        parcel.writeString(mDate);
        parcel.writeString(mLeagueName);
        parcel.writeString(mLeagueId);
        parcel.writeString(mMatchId);
    }
}
