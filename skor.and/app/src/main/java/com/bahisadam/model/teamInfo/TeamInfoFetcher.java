package com.bahisadam.model.teamInfo;



import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.text.SimpleDateFormat;


public class TeamInfoFetcher {

    private Uri mUri;
    private Uri mUriMatches;
    private Uri mUriFixtures;
    private static final String TAG = "TeamInfoFetcher";
    public static final String TEAM_INFO_PLAYERS = "team_info_players";
    public static final String TEAM_INFO_MAIN = "team_info_main";
    public static final String TEAM_INFO_STATS = "team_info_stats";
    public static final String TEAM_INFO_MATCHES = "team_info_matches";
    public static final String TEAM_INFO_FIXTURES = "team_info_fixtures";
    private String mTeamId;

    public TeamInfoFetcher(String id) {
        mTeamId = id;
        mUri = Uri.parse("http://www.bahisadam.com/api/team/detail/"+id);
        mUriMatches = Uri.parse("http://www.bahisadam.com/api/match/getteammatches/"+id);
        mUriFixtures = Uri.parse("http://www.bahisadam.com/api/match/getnextmatches/"+id);

    }

    private byte[] getUrlBytes(String urlSpec)throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK) throw new IOException(connection.getResponseMessage()+": with" + urlSpec);
            int bytesRead;
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer))>0){
                out.write(buffer,0,bytesRead);
            }
            out.close();
            return  out.toByteArray();
        }
        finally {
            connection.disconnect();
        }

    }

    private String getUrlString(String urlSpec) throws IOException{
        return new String(getUrlBytes(urlSpec));
    }

    public Bundle getTeamInfo(){
        String url = mUri.toString();
        String url1 = mUriMatches.toString();
        String url2 = mUriFixtures.toString();
        return downloadItems(url, url1, url2);
    }

    private Bundle downloadItems(String url, String urlMatches, String urlFixtures) {
        Bundle teamInfo = new Bundle();

        try {
            String jsonString = getUrlString(url);
            String jsonStringMatches = getUrlString(urlMatches);
            String jsonStringFixtures = getUrlString(urlFixtures);
            JSONObject jsonBody = new JSONObject(jsonString);
            JSONObject jsonBodyMatches = new JSONObject(jsonStringMatches);
            JSONObject jsonBodyFixtures = new JSONObject(jsonStringFixtures);
            Team team = parseTeamInfo(jsonBody);
            ArrayList<TeamPlayerInfo> players = parsePlayerInfo(jsonBody);
            ArrayList<TeamPlayerStats> teamPlayerStats = parsePlayerByLeagueInfo(jsonBody);
            ArrayList<TeamMatch> matches = parseMatchesInfo(jsonBodyMatches);
            ArrayList<TeamFixture> fixtures = parseFixturesInfo(jsonBodyFixtures);
            teamInfo.putParcelableArrayList(TEAM_INFO_PLAYERS,players);
            teamInfo.putParcelable(TEAM_INFO_MAIN,team);
            teamInfo.putParcelableArrayList(TEAM_INFO_STATS, teamPlayerStats);
            teamInfo.putParcelableArrayList(TEAM_INFO_MATCHES,matches);
            teamInfo.putParcelableArrayList(TEAM_INFO_FIXTURES,fixtures);
        } catch (JSONException je){
            Log.e(TAG,"Failed to parse Json",je);
        } catch (IOException ioe){
            Log.e(TAG,"Failed to fetch items",ioe);
        }
        return teamInfo;
    }

    private ArrayList<TeamMatch> parseMatchesInfo(JSONObject jsonBody) throws JSONException{
        ArrayList<TeamMatch> matches = new ArrayList<>();
        Iterator<?> keys = jsonBody.keys();
        while(keys.hasNext()) {
            String key = (String) keys.next();
            JSONArray matchArray = jsonBody.getJSONObject(key).getJSONArray("matches");

            for (int i = 0; i < matchArray.length(); i++) {
                try {
                JSONObject jsonMatch = matchArray.getJSONObject(i);
                TeamMatch match = new TeamMatch();
                match.setMatchId(jsonMatch.getString("_id"));
                match.setHomeTeamName(jsonMatch.getJSONObject("home_team").getString("team_name"));
                match.setHomeTeamImageUrl("http://static.bahisadam.com" + jsonMatch.getJSONObject("home_team").getJSONArray("team_logos").getString(0));
                match.setHomeTeamGoals(jsonMatch.getString("home_goals"));
                match.setAwayTeamName(jsonMatch.getJSONObject("away_team").getString("team_name"));
                match.setAwayTeamImageUrl("http://static.bahisadam.com" + jsonMatch.getJSONObject("away_team").getJSONArray("team_logos").getString(0));
                match.setAwayTeamGoals(jsonMatch.getString("away_goals"));
                match.setLeagueName(jsonMatch.getJSONObject("league_id").getString("league_name"));
                match.setLeagueId(jsonMatch.getJSONObject("league_id").getString("_id"));
                String dateString = jsonMatch.getString("match_date");
                Date date = new DateTime(dateString).toDate();
                match.setDate(new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).format(date));
                matches.add(match);
            }catch (JSONException je){je.printStackTrace();}
            }
        }
        return matches;
    }

    private ArrayList<TeamFixture> parseFixturesInfo(JSONObject jsonBody) throws JSONException{
        ArrayList<TeamFixture> matches = new ArrayList<>();
        Iterator<?> keys = jsonBody.keys();
        while(keys.hasNext()) {
            String key = (String) keys.next();
            JSONArray matchArray = jsonBody.getJSONObject(key).getJSONArray("matches");
            for (int i = 0; i < matchArray.length(); i++) {
                try {
                JSONObject jsonMatch = matchArray.getJSONObject(i);
                TeamFixture match = new TeamFixture();
                match.setMatchId(jsonMatch.getString("_id"));
                match.setHomeTeamName(jsonMatch.getJSONObject("home_team").getString("team_name"));
                match.setHomeTeamImageUrl("http://static.bahisadam.com" + jsonMatch.getJSONObject("home_team").getJSONArray("team_logos").getString(0));
                match.setAwayTeamName(jsonMatch.getJSONObject("away_team").getString("team_name"));
                match.setAwayTeamImageUrl("http://static.bahisadam.com" + jsonMatch.getJSONObject("away_team").getJSONArray("team_logos").getString(0));
                match.setLeagueName(jsonMatch.getJSONObject("league_id").getString("league_name"));
                match.setLeagueId(jsonMatch.getJSONObject("league_id").getString("_id"));
                String dateString = jsonMatch.getString("match_date");
                Date date = new DateTime(dateString).toDate();
                match.setDate(new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).format(date));
                matches.add(match);
                }catch (JSONException je){je.printStackTrace();}
            }
        }
        return matches;
    }

    private ArrayList<TeamPlayerStats> parsePlayerByLeagueInfo(JSONObject jsonBody) throws JSONException{
        ArrayList<TeamPlayerStats> players= new ArrayList<>();
        JSONArray statsArray = jsonBody.getJSONArray("stats");

            for (int i = 0; i < statsArray.length(); i++) {
                JSONObject leagueStats = statsArray.getJSONObject(i);
                JSONArray playerStats = leagueStats.getJSONArray("players");
                for (int j = 0; j < playerStats.length(); j++) {
                    try {
                        TeamPlayerStats player = new TeamPlayerStats();
                        JSONObject playerStat = playerStats.getJSONObject(j);
                        player.setName(playerStat.getString("player"));
                        player.setCount(playerStat.getInt("count"));
                        player.setEvent(playerStat.getString("event_type"));
                        player.setLeagueId(playerStat.getString("league_id"));
                        player.setLeagueName(leagueStats.getJSONObject("league").getString("league_name"));
                        if(playerStat.has("player_rs_id")) player.setID(playerStat.getString("player_rs_id"));
                        players.add(player);
                    }catch (JSONException je){je.printStackTrace();}
                }
            }
        return players;
    }

    private Team parseTeamInfo(JSONObject jsonBody) throws JSONException{
        Team team = new Team();
        try {
        team.setId(mTeamId);
        team.setName(jsonBody.getJSONObject("main").getString("team_name"));
        team.setLogoPath("http://static.bahisadam.com"+jsonBody.getJSONObject("main").getJSONArray("team_logos").getString(0));
        JSONObject teamDetails = jsonBody.getJSONObject("detail");
        team.setStadiumImageUrl(teamDetails.getString("img_stadium"));
        team.setStadiumName(teamDetails.getString("stadium"));
        team.setStadiumCapacity(teamDetails.getString("seats"));
        team.setStadiumYearBuilt(teamDetails.getString("yearBuilt"));
        team.setClubOfficialName(teamDetails.getString("fullName"));
        team.setYearFounded(teamDetails.getString("yearFoundation"));
        team.setClubHead(teamDetails.getString("chairman"));
        team.setCoach(teamDetails.getString("managerNow"));
        team.setWebsite(teamDetails.getString("website"));
        team.setTwitter(teamDetails.getString("twitter"));
        }catch (JSONException je){je.printStackTrace();}
        return team;
    }

    private ArrayList<TeamPlayerInfo> parsePlayerInfo(JSONObject jsonBody) throws JSONException{
        JSONArray squadArray = jsonBody.getJSONObject("detail").getJSONArray("squad");
        ArrayList<TeamPlayerInfo> players = new ArrayList<>();

        for(int i = 0;i<squadArray.length();i++){
            JSONObject playerJSON = squadArray.getJSONObject(i);
            try {
            TeamPlayerInfo playerInfo = new TeamPlayerInfo();
            playerInfo.setName(playerJSON.getString("nick"));
            playerInfo.setSquadNumber(playerJSON.getString("squadNumber"));
            int position = playerJSON.getInt("role");
            String positionStr = "-";
            switch(position){
                case 1:
                    positionStr = "K";
                    break;
                case 2:
                    positionStr = "D";
                    break;
                case 3:
                    positionStr = "O";
                    break;
                case 4:
                    positionStr = "F";
                    break;
            }
            playerInfo.setPosition(positionStr);
            playerInfo.setGoals(playerJSON.getString("goals"));
            playerInfo.setAssists(playerJSON.getString("assists"));
            playerInfo.setCountryCode(playerJSON.getString("CountryCode"));
            playerInfo.setImageUrl(playerJSON.getString("image"));
            playerInfo.setPlayerId(playerJSON.getString("id"));
            players.add(playerInfo);
            }catch (JSONException je){je.printStackTrace();}
        }
        return players;
    }

}
