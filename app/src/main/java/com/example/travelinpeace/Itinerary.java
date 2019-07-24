package com.example.travelinpeace;

public class Itinerary {
    private String Activity1;
    private String Activity2;
    private String Activity3;

    public Itinerary() {
    }

    public Itinerary(String activity1, String activity2, String activity3) {
        Activity1 = activity1;
        Activity2 = activity2;
        Activity3 = activity3;
    }

    public String getActivity1() {
        return Activity1;
    }

    public String getActivity2() {
        return Activity2;
    }

    public String getActivity3() {
        return Activity3;
    }
}
