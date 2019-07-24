package com.example.travelinpeace;

public class Countries {
    String countriesId;
    String countriesName;

    public Countries() {
    }

    public Countries(String countriesId, String countriesName) {
        this.countriesId = countriesId;
        this.countriesName = countriesName;
    }

    public String getCountriesId() {
        return countriesId;
    }

    public String getCountriesName() {
        return countriesName;
    }
}
