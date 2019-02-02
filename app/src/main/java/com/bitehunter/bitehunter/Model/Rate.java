package com.bitehunter.bitehunter.Model;

/**
 * Created by Pasan .M. Semage on 2017-12-19.
 */

public class Rate {
    private int reviewID;
    private int customerID;
    private String customerName;
    private String date;
    private int rate;
    private String review;

    public Rate(int reviewID, int customerID, String customerName, String date, int rate, String review) {
        this.reviewID = reviewID;
        this.customerID = customerID;
        this.customerName = customerName;
        this.date = date;
        this.rate = rate;
        this.review = review;
    }

    public int getReviewID() {
        return reviewID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDate() {
        return date;
    }

    public int getRate() {
        return rate;
    }

    public String getReview() {
        return review;
    }
}
