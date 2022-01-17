package com.elamparithi.parkypark.utils

object Constants {
    //if coupon gets disabled
    const val COUPON_STATUS_DISABLED = 0

    //if coupon is active for the user
    const val COUPON_STATUS_ACTIVE = 1

    //user used the coupon
    const val COUPON_STATUS_USED = 2

    //total floor count
    const val TOTAL_FLOOR_PER_PARKING_LOT = 2

    //total space count
    const val TOTAL_PARKING_SPACE_PER_FLOOR = 5

    const val PARKING_TYPE_PARK_NOW = 1
    const val PARKING_TYPE_RESERVATION = 2

    //when parking space is free
    const val PARKING_SPACE_STATUS_FREE = 1

    //when parking space is in the intermediate state ie, space allotted but user didn't confirm the allocation.
    const val PARKING_SPACE_STATUS_IN_PROGRESS = 2

    //when the space is taken
    const val PARKING_SPACE_STATUS_OCCUPIED = 3

    //After allotted parking
    const val BOOKING_STATUS_IN_PROGRESS = 1

    //user confirmed the given parking lot
    const val BOOKING_STATUS_CONFIRMED = 2

    //user parked her/his vehicle successfully
    const val BOOKING_STATUS_ACTIVE = 3

    //user picked up her/his vehicle successfully
    const val BOOKING_STATUS_COMPLETED = 4

    //user canceled his/her allotment  after confirmed
    const val BOOKING_STATUS_CANCELED = 5

    //After booking created
    const val PAYMENT_STATUS_BOOKING_CREATED = 1

    //when user confirmed and parked their vehicle
    const val PAYMENT_STATUS_IN_DUE = 2

    //when user paid
    const val PAYMENT_STATUS_PAID = 3

    //when user canceled the booking
    const val PAYMENT_STATUS_BOOKING_CANCELED = 4
}