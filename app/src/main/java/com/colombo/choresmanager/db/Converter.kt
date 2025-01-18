package com.colombo.choresmanager.db

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Date

class Converter {

    @TypeConverter
    fun dateFromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun localDateTimeFromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }
    }

    @TypeConverter
    fun localDateTimeToTimestamp(date: LocalDateTime?): Long? {
        return date?.toEpochSecond(ZoneOffset.UTC)
    }

    @TypeConverter
    fun zonedDateTimeFromTimestamp(value: Long?): ZonedDateTime? {
        return value?.let { ZonedDateTime.ofInstant(Instant.ofEpochSecond(it), ZoneId.systemDefault()) }
    }

    @TypeConverter
    fun zonedDateTimeToTimestamp(date: ZonedDateTime?): Long? {
        return date?.toEpochSecond()
    }
}