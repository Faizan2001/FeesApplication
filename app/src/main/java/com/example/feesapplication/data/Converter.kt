package com.example.feesapplication.data

import androidx.room.TypeConverter

class Converter {

    @TypeConverter
    fun fromFeeStatus(feeStatus: FeeStatus): String{
        return feeStatus.name
    }

    @TypeConverter
    fun toFeeStatus(feeStatus:String): FeeStatus {
        return FeeStatus.valueOf(feeStatus)
    }
}