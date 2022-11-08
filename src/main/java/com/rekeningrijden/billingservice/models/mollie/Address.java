package com.rekeningrijden.billingservice.models.mollie;

public class Address {
    private String streetAndNumber;     // The street and street number of the address.
    private String streetAdditional;    // Any additional addressing details, for example an apartment number.
    private String postalCode;          //The postal code of the address. Required for countries that use postal codes. May only be omitted for these country codes:
                                        // AE AN AO AW BF BI BJ BO BS BV BW BZ CD CF CG CI CK CM DJ DM ER FJ GA GD GH GM GN GQ GY HK JM KE KI KM KN KP LC ML MO MR
                                        // MS MU MW NA NR NU PA QA RW SB SC SL SO SR ST SY TF TK TL TO TT TV UG VU YE ZM ZW
    private String city;                // The city of the address.
    private String region;              // The region of the address.
    private String country;             // The country of the address in ISO 3166-1 alpha-2 format.
}
