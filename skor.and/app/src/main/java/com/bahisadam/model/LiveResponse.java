package com.bahisadam.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by atata on 13/12/2016.
 */

public class LiveResponse {
    public List<LiveItem> items;

    public LiveResponse() {
        items = new LinkedList<LiveItem>();
    }

    public class LiveItem{
        Integer id;
        String flag;
        private String league_name;
        private String league_name_tr;
        String country;
        Integer order;
        List<MatchPOJO.Match> matches;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getLeague_name() {
            return league_name;
        }

        public void setLeague_name(String league_name) {
            this.league_name = league_name;
        }

        public String getLeague_name_tr() {
            return league_name_tr;
        }

        public void setLeague_name_tr(String league_name_tr) {
            this.league_name_tr = league_name_tr;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public List<MatchPOJO.Match> getMatches() {
            return matches;
        }

        public void setMatches(List<MatchPOJO.Match> matches) {
            this.matches = matches;
        }
    }

}
