package com.example.travelinpeace;

public class DayActivities {
    String  activityId;
    String activityName;
    String activityDay;
    String activityTime;

    public DayActivities() {
    }

    public DayActivities(String activityId, String activityName, String activityDay, String activityTime) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.activityDay = activityDay;
        this.activityTime = activityTime;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getActivityDay() {
        return activityDay;
    }

    public String getActivityTime() {
        return activityTime;
    }
}
