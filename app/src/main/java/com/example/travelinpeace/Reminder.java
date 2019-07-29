package com.example.travelinpeace;

public class Reminder {
    String reminderId;
    String reminderName;

    public Reminder() {
    }

    public Reminder(String reminderId, String reminderName) {
        this.reminderId = reminderId;
        this.reminderName = reminderName;
    }

    public String getReminderId() {
        return reminderId;
    }

    public String getReminderName() {
        return reminderName;
    }
}
