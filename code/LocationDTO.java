package com.graduationwork.tracediary;

public class LocationDTO {
    public String _date;
    public String _time;
    public String _place;
    public Double _latitude;
    public Double _longitude;
    public float _accuracy;
    public String _comment;


    public LocationDTO(String _date, String _time, String _place) {
        this._date = _date;
        this._time = _time;
        this._place = _place;
    }

    public LocationDTO(String _date, String _time, String _place, Double _latitude, Double _longitude, float _accuracy) {
        this._date = _date;
        this._time = _time;
        this._place = _place;
        this._latitude = _latitude;
        this._longitude = _longitude;
        this._accuracy = _accuracy;
    }

    public LocationDTO(String _date, String _time, String _place, Double _latitude, Double _longitude, float _accuracy, String _comment) {
        this._date = _date;
        this._time = _time;
        this._place = _place;
        this._latitude = _latitude;
        this._longitude = _longitude;
        this._accuracy = _accuracy;
        this._comment = _comment;
    }
}
