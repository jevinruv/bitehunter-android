package com.bitehunter.bitehunter.Others;

/**
 * Created by Jevin on 15-Oct-17.
 */

public class RemoteURL {
    //public static final String host = "http://bitehunter.ga/database_operations/";
    public static final String host = "http://bitehunter.vimly.ml/database_operations/";
    public static final String HOSTURL = "http://bitehunter.vimly.ml/database_operations/android/";


    public static final String getRestaurants = HOSTURL + "get_restaurants.php";
    public static final String getTablePositions = HOSTURL + "get_table_positions.php";
    public static final String getReservedTables = HOSTURL + "get_reserved_tables.php";

    //user related
    public static final String addCustomer = host + "register_customer.php";
    public static final String updateCustomer = HOSTURL + "update_customer.php";
    public static final String getCustomer = HOSTURL + "get_customer_details.php";
    public static final String loginCustomer = HOSTURL + "login_customer.php";
    public static final String unregisterCustomer = host + "unregister_customer.php";
    public static final String getRateAndReviewDetaills = host + "rate_review_details.php";
    public static final String getMealRateAndReview = host + "meal_rate_review_details.php";
    public static final String insertMealRate = host + "meal_rate.php";
    public static final String rate_restaurant = host + "rate.php";

    public static final String getHomeActivityItems = HOSTURL + "home_activity_populate.php";

    public static final String getMealDetails = HOSTURL + "get_meal_details.php";
    public static final String getGetRestaurantDetails = HOSTURL + "get_restaurant_details.php";
    public static final String getMealPrices = HOSTURL + "get_meal_prices.php";
    public static final String getRestaurantMenu = HOSTURL + "get_restaurant_menu.php";
    public static final String getRestaurantList = HOSTURL + "get_restaurant_list.php";


    public static final String addReservationDetails = HOSTURL + "add_reservation_details.php";


    //Data URL
    public static final String DATA_URL = HOSTURL + "feed.php?page=";

    //JSON TAGS
    public static final String TAG_IMAGE_URL = "image";
    public static final String TAG_NAME = "name";
    public static final String TAG_PUBLISHER = "publisher";


}