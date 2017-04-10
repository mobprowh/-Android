package com.bahisadam.model;

import java.util.Comparator;

/**
 * Created by atata on 01/12/2016.
 */

public class EventListComparator {
    public static Comparator<MatchPOJO.Event> compareEventByMinute = new Comparator<MatchPOJO.Event>() {
        @Override
        public int compare(MatchPOJO.Event t1, MatchPOJO.Event t2) {
            if(t1.getMinute() == null) return -1;
            if(t2.getMinute() == null) return 1;
            return t1.getMinute() - t2.getMinute();
        }
    };
 }
