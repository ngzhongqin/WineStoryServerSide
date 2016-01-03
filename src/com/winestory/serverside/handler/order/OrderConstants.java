package com.winestory.serverside.handler.order;

/**
 * Created by zhongqinng on 27/12/15.
 * Constants for ORDER
 */
public class OrderConstants {
    public static String CITY_SINGAPORE = "SINGAPORE";
    public static String COUNTRY_SINGAPORE = "SINGAPORE";

    public static String ACTION_SUBMIT_ORDER = "SUBMIT_ORDER";
    public static String ACTION_GET_ALL_USER_ORDERS = "GET_ALL_USER_ORDERS";

    public static String COLOUR_ORDER_SUCC = "G";
    public static String CODE_ORDER_SUCC = "ORD001-100";
    public static String MSG_ORDER_SUCC  = "You have successfully placed an order.";

    public static String COLOUR_ORDER_FAIL = "R";
    public static String CODE_ORDER_FAIL = "ORD001-601";
    public static String MSG_ORDER_FAIL  = "Something went wrong when placing your order.";

    public static String COLOUR_GET_ALL_USER_ORDERS_SUCC = "G";
    public static String CODE_GET_ALL_USER_ORDERS_SUCC = "ORD002-100";
    public static String MSG_GET_ALL_USER_ORDERS_SUCC  = "Listing all your orders.";

    public static String COLOUR_GET_ALL_USER_ORDERS_FAIL = "R";
    public static String CODE_GET_ALL_USER_ORDERS_FAIL = "ORD002-601";
    public static String MSG_GET_ALL_USER_ORDERS_FAIL  = "We can't seem to find any prior orders.";


}
