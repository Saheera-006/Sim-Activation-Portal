package com.sim.portal.exception;

/** Central place for all customised messages (compile-time constants so they can be used in annotations). */
public final class Messages {
    private Messages() { }

    public static final String INVALID_SIM_DETAILS = "Invalid details, please check again SIM number/Service number!";
    public static final String SIM_ALREADY_ACTIVE = "SIM already active";
    public static final String EMAIL_DOB_REQUIRED = "Email/dob value is required";
    public static final String INVALID_EMAIL = "Invalid email";
    public static final String INVALID_DOB_FORMAT = "dob should be in yyyy-mm-dd format";
    public static final String NO_REQUEST_PLACED = "No request placed for you.";
    public static final String INVALID_NAME = "Firstname/Lastname should be maximum of 15 characters";
    public static final String NO_CUSTOMER_FOUND = "No customer found for the provided details";
    public static final String INVALID_CONFIRM_EMAIL = "Invalid email details!!";
    public static final String ADDRESS_TOO_LONG = "Address should be maximum of 25 characters";
    public static final String ADDRESS_REQUIRED = "Address is required";
    public static final String INVALID_PIN = "Pin should be 6 digit number";
    public static final String INVALID_CITY_STATE = "City/State should not contain any special characters except space";
    public static final String INVALID_ID = "Id should be 16 digit";
    public static final String ALL_FIELDS_MANDATORY = "All fields are mandatory";
    public static final String INVALID_DETAILS = "Invalid details";
    public static final String INCORRECT_DOB = "Incorrect date of birth details";
}
