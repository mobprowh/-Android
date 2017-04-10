package com.bahisadam.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bahisadam.Cache;
import com.bahisadam.Listeners.OnHorizontalAdapterItemClickListener;
import com.bahisadam.MyApplication;
import com.bahisadam.R;
import com.bahisadam.adapter.FootballAdapter;
import com.bahisadam.adapter.HorizontalAdapter;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.interfaces.RestClient;
import com.bahisadam.model.LeagueListComparator;
import com.bahisadam.model.LeagueMatchList;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.model.requests.AddRemoveFavoriteRequest;
import com.bahisadam.utility.Preferences;
import com.bahisadam.utility.Utilities;
import com.bahisadam.view.DetailPageActivity;
import com.bahisadam.view.HomeActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.*;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FootballFragment extends Fragment implements Callback<MatchPOJO>, Constant {

    //private static String TAG = FootballFragment.class.getSimpleName();
    private static WeakReference<FootballFragment> mContext;
    private RecyclerView recyclerView;
    private RecyclerView horizontal_recycler_view;
    private ProgressBar progressBar;
    //private HomePageAdapter adapter;
    private FootballAdapter adapter;
    private HorizontalAdapter horizontalAdapter;
    //private DatePickerDialog datePickerDialog;
    //private SimpleDateFormat dateFormatter;
    //private WebView mWebView;
    private String dateSelected;
    public Date currentDate;
    //private String startDate;
    //private String endDate;
    private Socket mSocket;
    private List<LeagueMatchList> list;
    private List<LeagueMatchList> favoriteList;
    private Set<String> favoriteMatches;
    boolean isFavorite;

    public static FootballFragment newInstance(Boolean fav) {

        Bundle args = new Bundle();
        args.putBoolean("fav",fav);
        FootballFragment fragment = new FootballFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public FootballFragment() {
        // Required empty public constructor
    }

    // HomeActivity Instance
    public static FootballFragment getInstance() {
        return mContext.get();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = new WeakReference<>(FootballFragment.this);
        if (getArguments() != null) {
            this.isFavorite = getArguments().getBoolean("fav");

        }

    }

    private Emitter.Listener onLiveScoreUpdate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Gson gson = new GsonBuilder().create();

            //Type type = new TypeToken<Map<String, String>>(){}.getType();
            //Map<String, String> myMap = gson.fromJson(args[0].toString(), type);
            try {
                JSONObject scores = ((JSONObject)args[0]).getJSONObject("scores");
                Type type = new TypeToken<Set<MatchPOJO.Match>>(){}.getType();
                //var adapter = mContext.recyclerView.getAdapter().get;
                Iterator<?> keys = scores.keys();
                while(keys.hasNext()){
                    String key = (String)keys.next();
                    JSONObject league = scores.getJSONObject(key);
                    Set<MatchPOJO.Match> matches = gson.fromJson(league.getJSONArray("matches").toString(), type);

                    for(LeagueMatchList l  : list) {
                        for(MatchPOJO.Match lm : l.getData()) {
                            for(MatchPOJO.Match m : matches) {
                                if(lm.getHomeTeam().getTeamNameTr().equals(m.getHomeTeam().getTeamNameTr()) && lm.getResultType().equals(PLAYING)
                                        && lm.getAwayTeam().getTeamNameTr().equals(m.getAwayTeam().getTeamNameTr())) {
                                    lm.setLiveMinute(m.getLiveMinute());
                                    lm.setResultType(m.getResultType());
                                    lm.setHomeGoals(m.getHomeGoals());
                                    lm.setAwayGoals(m.getAwayGoals());
                                    lm.setIsHalfTime(m.getIsHalfTime());
                                    break;
                                }
                            }
                        }
                    }
                }


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

            } catch (Exception e) {
                Log.e("error parsing response", e.getMessage());
            }

            /*while(scores.keys().hasNext()) {
                scores.keys().next();
            }*/


        }
    };

    @Override
    public void onPause() {
        super.onPause();
        mSocket.off("livescores_updated");
        mSocket.off(Socket.EVENT_ERROR);
        mSocket.off(Socket.EVENT_CONNECT);
        mSocket.off(Socket.EVENT_DISCONNECT);
        mSocket.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSocket.on("livescores_updated", onLiveScoreUpdate);
        mSocket.on(Socket.EVENT_ERROR,onError);
        mSocket.on(Socket.EVENT_CONNECT, this.onSocketConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, this.onSocketDisconnect);
        mSocket.connect();
    }

    public Emitter.Listener onSocketConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("socket connect", "connected");
        }
    };
    public Emitter.Listener onSocketDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("socket disconnect", "disconnected");
        }
    };

    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("Socket.IO Error",args[0].toString());
        }
    };


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_football, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        horizontal_recycler_view = (RecyclerView) rootView.findViewById(R.id.horizontal_recycler_view);


        currentDate =  Utilities.addHour(Utilities.getToday(), 3);
        Log.e("CurrentdateTime", "CurrentdateTime??" + currentDate);
        dateSelected = Utilities.toJSONDateString(currentDate);

        //init socket
        mSocket = ((MyApplication) getActivity().getApplication()).getSocket();

        setAdapterHorizontal();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        list = new ArrayList<>();
        favoriteList = new ArrayList<>();

        favoriteMatches = Preferences.getFavoriteMatches();
        if(isFavorite){
            adapter = new FootballAdapter(favoriteList, getActivity(), this);
        } else {
            adapter = new FootballAdapter(list, getActivity(), this);
        }

        adapter.setMatchClickListener(new FootballAdapter.OnMatchClickListener() {
            @Override
            public void onFavoriteClick(int section, int relativePosition,int absolutePosition) {
                MatchPOJO.Match match;
                if(isFavorite) match = favoriteList.get(section).get(relativePosition);
                else match = list.get(section).get(relativePosition);

                boolean add = !favoriteMatches.contains(match.getId());

                toggleFavoriteMatch(match, absolutePosition, add);
            }

            @Override
            public void onMatchClick(int section, int relativePosition) {
                MatchPOJO.Match match = null;
                if(isFavorite) {
                    match = favoriteList.get(section).get(relativePosition);
                } else {
                    match = list.get(section).get(relativePosition);
                }

                Bundle bundle = new Bundle();
                bundle.putString(DetailPageActivity.MATCH_ID,match.getId());
                bundle.putInt(DetailPageActivity.LEAGUE_ID,match.getLeagueId().getId());
                bundle.putString(DetailPageActivity.ARG_TEAM_HOME_NAME,match.getHomeTeam().getTeamNameTr());
                bundle.putString(DetailPageActivity.ARG_TEAM_AWAY_NAME,match.getAwayTeam().getTeamNameTr());
                bundle.putString(DetailPageActivity.ARG_REUSULT_TYPE,match.getResultType());
                Utilities.openMatchDetails(getActivity(),bundle);

            }
        });
        recyclerView.setAdapter(adapter);
    /*
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position < 0) return;
                if(adapter.isHeader(position)) {
                    List<LeagueMatchList> lst = list;
                    if (isFavorite)
                        lst = favoriteList;
                    MatchPOJO.LeagueId league = lst.get(position).getLeauge();

                    Utilities.openLeagueDetails(getActivity(),
                            league.getId(),
                            league.getLeagueNameTr(),
                            lst.get(position).get(0).getCountry().getCountryCode());
                }
            }
        }));*/

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));


        //startDate = currentDate+"Z";
        //endDate = Utilities.getNextDate(currentDate)+"Z";

        if (Utilities.isNetworkAvailable(getActivity())) {
            setSelectedDate(dateSelected,true);
            //getMatchRequest(startDate, endDate);

        } else {
            Utilities.showSnackBarInternet(getActivity(), HomeActivity.getInstance().getCoordinateLayout(), getResources().getString(R.string.no_internet_connection));
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    private void toggleFavoriteMatch(final MatchPOJO.Match match, final int absolutePosition, final boolean add){
        if(Preferences.isLogged()) {
            RestClient client = Utilities.buildRetrofit();
            Call<AddRemoveFavoriteRequest.Response> call;
            AddRemoveFavoriteRequest request = new AddRemoveFavoriteRequest(match.getId());

            if(add){
                call= client.addFavorite(request);
            } else  {
                call = client.removeFavorite(request);
            }
            Utilities.showProgressDialog(getActivity(), progressBar);
            call.enqueue(new Callback<AddRemoveFavoriteRequest.Response>() {
                @Override
                public void onResponse(Call<AddRemoveFavoriteRequest.Response> call, Response<AddRemoveFavoriteRequest.Response> response) {
                    Utilities.dismissProgressDialog(getActivity(),progressBar);
                    AddRemoveFavoriteRequest.Response body = response.body();

                    if(body.error == null){
                        if(add){
                            favoriteMatches.add(match.getId());
                            FirebaseMessaging.getInstance().subscribeToTopic(match.getId());
                            match.setIsFavorite(true);
                        }
                        else {
                            favoriteMatches.remove(match.getId());
                            FirebaseMessaging.getInstance().unsubscribeFromTopic( match.getId());
                            match.setIsFavorite(false);
                        }

                        //adapter.notifyItemChanged(absolutePosition);
                        adapter.notifyDataSetChanged();

                        Preferences.getDefaultPreferences().edit()
                                .putStringSet(Preferences.PREF_FAVORITES,favoriteMatches)
                                .apply();

                    } else {
                        if(body.errorType.equals("Authorization")) {
                            Preferences.setIsLogged(false);
                            Utilities.login(getActivity());
                            return;
                        }
                        else {
                            Utilities.showSnackBar(getActivity(), recyclerView, body.error);
                        }
                    }
                    Utilities.dismissProgressDialog(getActivity(),progressBar);

                }

                @Override
                public void onFailure(Call<AddRemoveFavoriteRequest.Response> call, Throwable t) {
                    t.printStackTrace();
                    Utilities.dismissProgressDialog(getActivity(),progressBar);
                }
            });
        } else {
            Utilities.showProgressDialog(getActivity(), progressBar);
            Utilities.login(getActivity(), true, new Utilities.LoginCallback() {
                @Override
                public void onSuccess() {
                    Utilities.dismissProgressDialog(getActivity(),progressBar);
                }
                @Override
                public void onError(String error) {
                    Utilities.dismissProgressDialog(getActivity(),progressBar);
                    Utilities.showSnackBar(getActivity(),recyclerView, error);
                }
            });

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void loadBitmatIntoView(ImageView view, String color1, String color2) {

        color1 = color1 == null ? "0" : color1;
        color2 = color2 == null ? "0" : color2;
        Bitmap bitmap = Cache.getBitmap(color1+color2);
        if(bitmap == null) {
            new BitmapWorkerTask(getActivity(),view).execute(color1,color2);

        } else {
            view.setImageBitmap(bitmap);
        }


    }

    private void setAdapterHorizontal() {

        horizontalAdapter = new HorizontalAdapter(getActivity(), Utilities.getCurrentWeek(getActivity(),dateSelected));
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);
        horizontalAdapter.setOnCalendarclickListener(new HorizontalAdapter.OnCalendarClickListener() {
            @Override
            public void onClick() {
                SelectDateFragment dialog =  SelectDateFragment.newInstance(FootballFragment.this);
                dialog.show(getFragmentManager(),"DatePicker");

            }
        });
        horizontalAdapter.setOnDateSelectedListener(new HorizontalAdapter.OnDateSelectedListener() {
            @Override
            public void onDateSelected(String date) {
                currentDate = Utilities.parseJSONDate(date);
                setSelectedDate(date,false);
            }
        });

        horizontal_recycler_view.setAdapter(horizontalAdapter);
        horizontal_recycler_view.addOnItemTouchListener(new OnHorizontalAdapterItemClickListener());

    }

    //@Override
    //public void onAttach(Activity activity) {
    //super.onAttach(activity);
    //}

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /* Make Rest Client Call For get match request */
    public void getMatchRequest(String startDate, String endDate) {
        Utilities.showProgressDialog(getActivity(),progressBar);
        //startDate = Utilities.formatDate(new Date(Utilities.parseJSONDate(startDate).getTime() + (1000 * 3600 * 3)),"yyyy-MM-dd'T'HH:mm:ssZ");
        //endDate = Utilities.formatDate(new Date(Utilities.parseJSONDate(endDate).getTime() + (1000 * 3600 * 3)),"yyyy-MM-dd'T'HH:mm:ssZ");
        Log.i("StartDate", startDate);
        Log.i("EndDate", endDate);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT + Constant.APPENDED_PATH + startDate
                        + Constant.SLASH
                        + endDate
                        + Constant.SLASH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // prepare call in Retrofit 2.0
        RestClient restClientAPI = retrofit.create(RestClient.class);

        Call<MatchPOJO> call = restClientAPI.matchRequest();

        //asynchronous call
        call.enqueue(this);
    }

    /* Get Response From LogIn */
    @Override
    public void onResponse(Call<MatchPOJO> call, Response<MatchPOJO> response) {
        MatchPOJO match = response.body();
        int code = response.code();
        if (code == RESPONSE_CODE) {
            Utilities.dismissProgressDialog(getActivity(), progressBar);

            if (match.getIsSuccess() == SUCCESS) {

                if (match.getData().getMatches() != null && match.getData().getMatches().size() > 0) {

                    List<MatchPOJO.LeagueId> leagueIds = new ArrayList<>();
                    //Iterator<MatchPOJO.Match> itr = match.getData().getMatches().iterator();

                    for (MatchPOJO.Match m : match.getData().getMatches()) {

                        MatchPOJO.LeagueId leagueId = m.getLeagueId();

                        if(!leagueIds.contains(leagueId)){
                            leagueIds.add(leagueId);
                        }
                    }

                    /*while (itr.hasNext()) {
                        MatchPOJO.Match element = itr.next();
                        MatchPOJO.LeagueId leagueId = element.getLeagueId();

                        if(!leagueIds.contains(leagueId)){
                            leagueIds.add(leagueId);
                        }
                    }*/

                    Collections.sort(leagueIds,LeagueListComparator.leagueOrderComparator);
                    for (int i = 0; i < leagueIds.size(); i++) {

                        LeagueMatchList leagueMatchList = new LeagueMatchList(leagueIds.get(i));
                        LeagueMatchList leagueMatchListFav = new LeagueMatchList(leagueIds.get(i));

                        for (int j = 0; j < match.getData().getMatches().size(); j++) {
                            if (leagueIds.get(i).equals(match.getData().getMatches().get(j).getLeagueId())) {
                                leagueMatchList.add(match.getData().getMatches().get(j));
                                if(favoriteMatches.contains(match.getData().getMatches().get(j).getId())){
                                    leagueMatchListFav.add(match.getData().getMatches().get(j));
                                    match.getData().getMatches().get(j).setIsFavorite(true);
                                }
                            }
                        }
                        list.add(leagueMatchList);
                        if(leagueMatchListFav.size()>0){
                            favoriteList.add(leagueMatchListFav);
                        }
                    }



                    adapter.notifyDataSetChanged();
                    recyclerView.invalidate();
                }

            } else {
                Log.e("Success", "Success??" + match.getIsSuccess());
            }
        }
    }


    @Override
    public void onFailure(Call<MatchPOJO> call, Throwable t) {
        Log.e("onFailure", "onFailure?? " + t.getMessage());
        Utilities.dismissProgressDialog(getActivity(), progressBar);
        Utilities.showSnackBar(getActivity(), HomeActivity.getInstance().getCoordinateLayout(),
                String.valueOf(getString(R.string.failed_to_connect_with_server)));

    }

    // Set Selected Date...
    public void setSelectedDate(String dateSelected,boolean initWeek) {
        this.dateSelected = dateSelected;
        list.clear();
        favoriteList.clear();
        if(initWeek){
            horizontalAdapter.clear();
            horizontalAdapter.add(Utilities.getCurrentWeek(getActivity(),dateSelected));
        }
        getMatchRequest(dateSelected, Utilities.toJSONDateString(Utilities.addDay(currentDate, 1)));
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private Activity ctx;
        //LruCache<String,Bitmap> cache;

        BitmapWorkerTask(Activity ctx,ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
            this.ctx = ctx;
        }

        // Decode image in background.
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected Bitmap doInBackground(String... params) {
            String color1= params[0];
            String color2 = params[1];
            Bitmap bitmap = Utilities.paintTeamIcon(ctx,color1,color2);
            Cache.addBitmap(color1+color2,bitmap);
            return bitmap;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {

            //if (imageViewReference != null && bitmap != null) {
            if (bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

}
