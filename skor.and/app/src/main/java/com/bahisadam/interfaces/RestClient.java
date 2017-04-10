package com.bahisadam.interfaces;


import com.bahisadam.model.LiveResponse;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.model.StandingsRequest;
import com.bahisadam.model.TournamentListRequest;
import com.bahisadam.model.UpdateForecastBody;
import com.bahisadam.model.requests.AddRemoveFavoriteRequest;
import com.bahisadam.model.requests.BaseAuthRequest;
import com.bahisadam.model.requests.DeviceLoginRequest;
import com.bahisadam.model.requests.RegisterRequest;
import com.bahisadam.model.requests.UpdatePredictionRequest;
import com.bahisadam.model.requests.UserLoginRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestClient {
    @GET("./")
    Call<MatchPOJO> matchRequest();
    @GET("match/detail/{matchId}")
    Call<MatchPOJO.MatchDetailed> matchDetailRequest(@Path("matchId") String matchId);
    @GET("standings/{leagueId}")
    Call<MatchPOJO.StandingsRequest> standingsRequest(@Path("leagueId") Integer leagueId);
    @GET("league/standings/{leagueId}")
    Call<StandingsRequest> standings(@Path("leagueId") Integer leagueId);
    @GET("standings/homeaway/{leagueId}")
    Call<MatchPOJO.HomeAwayRequest> homeAwayRequest(@Path("leagueId") Integer leagueId);
    @GET("forecast/match-forecast/list/{matchId}")
    Call<List<MatchPOJO.Comment>> commentsRequest(@Path("matchId") String matchId);
    @GET("forecast/like/{matchId}")
    Call<MatchPOJO.LikeUpdate> likeUpdateRequest(@Path("matchId") String matchId);
    @POST("match/updateforecast")
    Call<MatchPOJO.ForecastUpdate> updateForecastRequest( @Body UpdateForecastBody body);
    @GET("stats/byLeague/{leagueId}")
    Call<MatchPOJO.LeagueStats> statsByLeague(@Path("leagueId") Integer leagueId);
    @GET("fixture/{leagueId}/{round}")
    Call<MatchPOJO.Fixture> fixture(@Path("leagueId") Integer leagueId,@Path("round") Integer roound);
    @GET("fixture/{leagueId}")
    Call<MatchPOJO.Fixture> fixture(@Path("leagueId") Integer leagueId);
    @GET("live-scores")
    Call<LiveResponse> liveMatches();
    @GET("league/tournament-list")
    Call<TournamentListRequest> tournamentList();
    @POST("auth/login")
    Call<BaseAuthRequest.Response> loginRequest( @Body UserLoginRequest body);
    @POST("auth/device-login")
    Call<BaseAuthRequest.Response> loginRequest(@Body DeviceLoginRequest body);
    @POST("auth/register")
    Call<BaseAuthRequest.Response> registerRequest(@Body RegisterRequest body);
    @POST("forecast/update-myforecast")
    Call<UpdatePredictionRequest.Response> updatePrediction(@Body UpdatePredictionRequest body);
    @POST("user/favorites/add")
    Call<AddRemoveFavoriteRequest.Response> addFavorite(@Body AddRemoveFavoriteRequest body);
    @POST("user/favorites/remove")
    Call<AddRemoveFavoriteRequest.Response> removeFavorite(@Body AddRemoveFavoriteRequest body);
}

