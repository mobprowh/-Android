package com.bahisadam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atata on 17/12/2016.
 */

public class TournamentListRequest {
    List<Country> countries = null;

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public class Country{
        @SerializedName("_id")
        @Expose
        Integer id;
        @SerializedName("country_name_tr")
        @Expose
        private String countryNameTr;
        @SerializedName("country_name")
        @Expose
        private String countryName;
        @SerializedName("country_code")
        @Expose
        private String countryCode;
        @SerializedName("order")
        @Expose
        private Integer order;
        @SerializedName("seo_name")
        @Expose
        private String seoName;
        @SerializedName("leagues")
        @Expose
        private List<League> leagues = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCountryNameTr() {
            return countryNameTr;
        }

        public void setCountryNameTr(String countryNameTr) {
            this.countryNameTr = countryNameTr;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public String getSeoName() {
            return seoName;
        }

        public void setSeoName(String seoName) {
            this.seoName = seoName;
        }

        public List<League> getLeagues() {
            return leagues;
        }

        public void setLeagues(List<League> leagues) {
            this.leagues = leagues;
        }
    }
    public class League {

        @SerializedName("_id")
        @Expose
        private Integer id;
        @SerializedName("league_name")
        @Expose
        private String leagueName;
        @SerializedName("league_name_tr")
        @Expose
        private String leagueNameTr;
        @SerializedName("order")
        @Expose
        private Integer order;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getLeagueName() {
            return leagueName;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }

        public String getLeagueNameTr() {
            return leagueNameTr;
        }

        public void setLeagueNameTr(String leagueNameTr) {
            this.leagueNameTr = leagueNameTr;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }
    }
}
