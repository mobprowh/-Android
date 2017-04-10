package com.bahisadam.model;

import com.bahisadam.utility.Utilities;

import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by atata on 01/12/2016.
 */

public class MatchListComparator {
    public static Comparator<MatchPOJO.Match> compareMatchByDate = new Comparator<MatchPOJO.Match>() {
        @Override
        public int compare(MatchPOJO.Match match, MatchPOJO.Match t1) {
            Calendar cal1 = Utilities.parseDate(match.getMatchDate());
            Calendar cal2 = Utilities.parseDate(t1.getMatchDate());
            int res = cal2.getTimeInMillis() >= cal1.getTimeInMillis() ? 1 : -1;
            return res;
        }
    };
    public static Comparator<MatchPOJO.Match> compareMatchByDateReversed = new Comparator<MatchPOJO.Match>() {
        @Override
        public int compare(MatchPOJO.Match match, MatchPOJO.Match t1) {
            Calendar cal1 = Utilities.parseDate(match.getMatchDate());
            Calendar cal2 = Utilities.parseDate(t1.getMatchDate());
            int res = cal2.getTimeInMillis() >= cal1.getTimeInMillis() ? -1 : 1;
            return res;
        }
    };
 }
