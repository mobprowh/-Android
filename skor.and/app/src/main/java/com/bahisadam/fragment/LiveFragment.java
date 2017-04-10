package com.bahisadam.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bahisadam.Listeners.RecyclerItemClickListener;
import com.bahisadam.MyApplication;
import com.bahisadam.R;
import com.bahisadam.adapter.LiveMatchAdapter;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.interfaces.RestClient;
import com.bahisadam.model.LiveResponse;
import com.bahisadam.utility.Utilities;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragment extends Fragment implements Callback<LiveResponse> {
    @BindView(R.id.rv)
    RecyclerView rv;
    LiveMatchAdapter mAdapter;
    RestClient restClient;
    private Socket mSocket;
    Gson gson;
    private boolean loaded = false;

    public LiveFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_live, container, false);
        ButterKnife.bind(this,view);

        mAdapter = new LiveMatchAdapter();
        rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mAdapter.allocateHeaders();
                if(mAdapter.isHeader(position)){
                    int section = mAdapter.getSection(position);
                    int leagueId = mAdapter.data.get(section).getId();
                    String leagueName = mAdapter.data.get(section).getLeague_name_tr();
                    String leagueIcon = mAdapter.data.get(section).getFlag();
                    Utilities.openLeagueDetails(getActivity(),leagueId,leagueName,leagueIcon);
                }

            }
        }));
        rv.setAdapter(mAdapter);
        gson = new GsonBuilder()
                .registerTypeAdapter(LiveResponse.class, new JsonDeserializer<LiveResponse>() {

                    @Override
                    public LiveResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        LiveResponse response = new LiveResponse();
                        if(loaded)
                            json=((JsonObject)json).get("scores");
                        Set<Map.Entry<String, JsonElement>> entries   =  ((JsonObject) json).entrySet();
                        Iterator<Map.Entry<String, JsonElement>> iterator = entries.iterator();
                        while(iterator.hasNext()){
                            Map.Entry<String,JsonElement> it = iterator.next();

                            response.items.add(new Gson().fromJson(it.getValue(),LiveResponse.LiveItem.class));

                        }
                        return response;
                    }
                })
                .create();

        mSocket = ((MyApplication) getActivity().getApplication()).getSocket();
        mSocket.on("livescores_updated",onLiveScoreUpdate);
        mSocket.on(Socket.EVENT_ERROR,onError);

        mSocket.connect();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // prepare call in Retrofit 2.0
        restClient =  retrofit.create(RestClient.class);
        Call<LiveResponse> call = restClient.liveMatches();
        call.enqueue(this);
        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mSocket != null){
            mSocket.disconnect();
            mSocket.off("livescores_updated");
            mSocket.off(Socket.EVENT_ERROR);
        }

    }
    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("Socket.IO Error",args[0].toString());
        }
    };

    Emitter.Listener onLiveScoreUpdate  = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<LiveResponse.LiveItem> items = gson.fromJson(args[0].toString(),LiveResponse.class).items;
                    updateScore(items);
                }
            });


        }
    };

    private void updateScore(List<LiveResponse.LiveItem> items){
        if(items!=null) {
            Collections.sort(items, new Comparator<LiveResponse.LiveItem>() {
                @Override
                public int compare(LiveResponse.LiveItem liveItem, LiveResponse.LiveItem t1) {
                    return liveItem.getOrder() - t1.getOrder();
                }
            });
            mAdapter.data.clear();
            mAdapter.data.addAll(items);
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onResponse(Call<LiveResponse> call, Response<LiveResponse> response) {
        List<LiveResponse.LiveItem> items = response.body().items;
        updateScore(items);
        loaded = true;


    }

    @Override
    public void onFailure(Call<LiveResponse> call, Throwable t) {
        int x=0;
        Crashlytics.logException(t);

    }

}
