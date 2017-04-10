package com.bahisadam.model;

import java.util.Comparator;

public class LeagueListComparator {

    private String leagueName;

    public LeagueListComparator(String leagueName) {
        this.leagueName = leagueName;
    }

    /*Comparator for sorting the list by League Name*/
    public static Comparator<MatchPOJO.Match> leagueNameComparator = new Comparator<MatchPOJO.Match>() {

        public int compare(MatchPOJO.Match l1, MatchPOJO.Match l2) {
            String leagueName1 = l1.getLeagueId().getLeagueName().toUpperCase();
            String leagueName2 = l2.getLeagueId().getLeagueName().toUpperCase();

            //ascending order
            return leagueName1.compareTo(leagueName2);




            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };
    public  static Comparator<MatchPOJO.LeagueId> leagueOrderComparator =  new Comparator<MatchPOJO.LeagueId>() {
        @Override
        public int compare(MatchPOJO.LeagueId l1, MatchPOJO.LeagueId l2) {
            Integer leagueOrder1 = l1.getOrder();
            Integer leagueOrder2 = l2.getOrder();
            return leagueOrder1.compareTo(leagueOrder2);
        }
    };
}
