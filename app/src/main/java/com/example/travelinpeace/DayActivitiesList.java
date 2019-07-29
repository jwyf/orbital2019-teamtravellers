package com.example.travelinpeace;

public class DayActivitiesList {
    String dayName;
    String dayId;
    String activityString;

    public DayActivitiesList() {
    }

    public DayActivitiesList(String dayName, String dayId, String activityString) {
        this.dayName = dayName;
        this.dayId = dayId;
        this.activityString = activityString;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }

    public void setActivityString(String activityString) {
        this.activityString = activityString;
    }

    public String getDayName() {
        return dayName;
    }

    public String getDayId() {
        return dayId;
    }

    public String getActivityString() {
        return activityString;
    }
}
