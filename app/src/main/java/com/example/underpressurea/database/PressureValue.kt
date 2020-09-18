package com.example.underpressurea.database

data class PressureValue(val min: Int = -1, val max:Int = -1, val level: Int = 0, val startTimeMilli: Long = System.currentTimeMillis())

