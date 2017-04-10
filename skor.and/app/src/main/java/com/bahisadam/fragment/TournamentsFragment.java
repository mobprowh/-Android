package com.bahisadam.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.bahisadam.R;
import com.bahisadam.adapter.ExpandableAdapter;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.interfaces.RestClient;
import com.bahisadam.model.TournamentListRequest;
import com.bahisadam.utility.Utilities;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class TournamentsFragment extends Fragment implements Callback<TournamentListRequest> {
    @BindView(R.id.list) ExpandableListView mListView;
    ExpandableAdapter mAdapter;
    List<TournamentListRequest.Country> mData;

    public TournamentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tournaments, container, false);
        ButterKnife.bind(this,view);

        mData = new LinkedList<TournamentListRequest.Country>();
        mAdapter = new ExpandableAdapter(mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Utilities.openLeagueDetails(getActivity(),
                        mData.get(i).getLeagues().get(i1).getId(),
                        mData.get(i).getLeagues().get(i1).getLeagueName(),
                        mData.get(i).getCountryCode());
                return false;
            }
        });
        RestClient restClient = buildRetrofit();
        Call<TournamentListRequest> call = restClient.tournamentList();
        call.enqueue(this);

        return view;
    }


    private RestClient buildRetrofit(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(TournamentListRequest.class, new JsonDeserializer<TournamentListRequest>() {
                    @Override
                    public TournamentListRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        JsonArray arr = json.getAsJsonArray();
                        TournamentListRequest request = new TournamentListRequest();
                        request.setCountries(Arrays.asList(new Gson().fromJson(arr,TournamentListRequest.Country[].class)));
                        return request;


                    }
                })
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // prepare call in Retrofit 2.0
        return  retrofit.create(RestClient.class);
    }

    @Override
    public void onResponse(Call<TournamentListRequest> call, Response<TournamentListRequest> response) {
        TournamentListRequest body  = response.body();
        if(body.getCountries() != null ){
            mData.clear();
            List<TournamentListRequest.Country> countries = body.getCountries();
            //TODO: move to comparators class, which shoul be composed from other comparators
            Collections.sort(countries, new Comparator<TournamentListRequest.Country>() {
                @Override
                public int compare(TournamentListRequest.Country country, TournamentListRequest.Country t1) {
                    if(country.getOrder() == null) return -1;
                    if(t1.getOrder() == null ) return 1;
                    return country.getOrder() - t1.getOrder();
                }
            });

            mData.addAll(countries);

            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onFailure(Call<TournamentListRequest> call, Throwable t) {
        Utilities.showSnackBar(getActivity(),mListView,getString(R.string.failed_to_connect_with_server));
        Crashlytics.logException(t);

    }

}
