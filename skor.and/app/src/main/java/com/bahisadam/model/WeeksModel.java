package com.bahisadam.model;

public class WeeksModel {
    String weeks;
    String days;
    Boolean isCurrentDate=false;
    int icon;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


    public Boolean getCurrentDate() {
        return isCurrentDate;
    }

    public void setCurrentDate(Boolean currentDate) {
        isCurrentDate = currentDate;
    }


    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }


}
