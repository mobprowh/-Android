package com.bahisadam.adapter;

import com.bahisadam.model.MatchPOJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atata on 28/11/2016.
 */

public class MatchAdapter {
    private List<UpdateMatchListener> subscribers;
    public MatchAdapter(){
        subscribers = new ArrayList<UpdateMatchListener>();
    }

    public interface UpdateMatchListener{
        public void updateMatch(MatchPOJO.MatchDetailed matchDetailed);
        public void updateData();
        public void updateGoalAverages();
        public void updateStandings();
    }
    public void subscribe(UpdateMatchListener subscriber){
        subscribers.add(subscriber);
    }

    public void notifyDatasetChanged(MatchPOJO.MatchDetailed match){
        for(int i=0;i < subscribers.size(); i++){
            subscribers.get(i).updateMatch(match);

        }
    }
    public void notifyDatasetChanged() {
        for (int i = 0; i < subscribers.size(); i++) {
            subscribers.get(i).updateData();
        }
    }
    public void updateGoalAverages(){
        for(int i=0; i < subscribers.size(); i++ ){
            subscribers.get(i).updateGoalAverages();
        }
    }
    public void updateStandings(){
        for(int i=0; i < subscribers.size(); i++ ){
            subscribers.get(i).updateStandings();
        }
    }
    public void unSubscribe(UpdateMatchListener subscriber){
        for(int i =0; i < subscribers.size(); i++ ){
            if(subscriber == subscribers.get(i)) subscribers.remove(i);
        }
    }

}
