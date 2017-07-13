package com.myrescribe.model.util;

/**
 * Created by riteshpandhurkar on 13/7/17.
 */

public class TimePeriod {
    private String monthName;
    private String year;

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "TimePeriod{" +
                "monthName='" + monthName + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
