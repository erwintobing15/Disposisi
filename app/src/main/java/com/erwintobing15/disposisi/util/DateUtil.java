package com.erwintobing15.disposisi.util;

public class DateUtil {

    public static String formatDate(String date) {

        String [] arrDate = date.split("-");

        switch (arrDate[1]) {
            case "01":
                arrDate[1] = "Jan";
                break;
            case "02":
                arrDate[1] = "Feb";
                break;
            case "03":
                arrDate[1] = "Mar";
                break;
            case "04":
                arrDate[1] = "Apr";
                break;
            case "05":
                arrDate[1] = "Mei";
                break;
            case "06":
                arrDate[1] = "Jun";
                break;
            case "07":
                arrDate[1] = "Jul";
                break;
            case "08":
                arrDate[1] = "Agu";
                break;
            case "09":
                arrDate[1] = "Sep";
                break;
            case "10":
                arrDate[1] = "Okt";
                break;
            case "11":
                arrDate[1] = "Nov";
                break;
            case "12":
                arrDate[1] = "Des";
                break;
        }

        return arrDate[2] + " " + arrDate[1] + " " + arrDate[0];
    }
}
