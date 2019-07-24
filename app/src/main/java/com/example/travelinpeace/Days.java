package com.example.travelinpeace;

public class Days {
    String dayId;
    String dayName;

    public Days() {
    }

    public Days(String dayId, String dayName) {
        this.dayId = dayId;
        this.dayName = dayName;
    }

    public String getDayId() {
        return dayId;
    }

    public String getDayName() {
        return dayName;
    }
}
