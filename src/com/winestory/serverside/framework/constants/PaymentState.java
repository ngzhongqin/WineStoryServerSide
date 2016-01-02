package com.winestory.serverside.framework.constants;

/**
 * Created by zhongqinng on 1/1/16.
 * PaymentState Constants
 */
public class PaymentState {
    public static String CHARGE_PEND = "CHARGE_PEND";
    public static String STRIPE_AUTHENTICATION_EXCEPTION =  "STRIPE_AUTHENTICATION_EXCEPTION";
    public static String STRIPE_INVALID_REQUEST_EXCEPTION = "STRIPE_INVALID_REQUEST_EXCEPTION";
    public static String STRIPE_API_CONNECTION_EXCEPTION = "STRIPE_API_CONNECTION_EXCEPTION";
    public static String STRIPE_API_EXCEPTION = "STRIPE_API_EXCEPTION";
    public static String STRIPE_CARD_EXCEPTION = "STRIPE_CARD_EXCEPTION";

    public static String CHARGE_SUCC = "CHARGE_SUCC";
}
