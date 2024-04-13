package com.amv.simple.app.mysupernotes.presentation.settings.domain

enum class DataStoreFormatDateTime(val title: String, val pattern: String) {
    PATTERN_1("31/12/2024 - 13:32", "dd/MM/yyyy - HH:mm"),
    PATTERN_2("12/31/2024 - 13:32", "MM/dd/yyyy - HH:mm"),
    PATTERN_3("2024/31/12 - 13:32", "yyyy/dd/MM - HH:mm"),
    PATTERN_4("2024/12/31 - 13:32", "yyyy/MM/dd - HH:mm"),
    PATTERN_5("2024-12-31 - 13:32", "yyyy-MM-dd - HH:mm"),
    PATTERN_6("2024-31-12 - 13:32", "yyyy-dd-MM - HH:mm"),
    PATTERN_7("31-12-2024 - 13:32", "dd-MM-yyyy - HH:mm"),
    PATTERN_8("12-31-2024 - 13:32", "MM-dd-yyyy - HH:mm"),
    PATTERN_9("31.12.2024 - 13:32", "dd.MM.yyyy - HH:mm"),
    PATTERN_10("2024.12.31 - 13:32", "yyyy.MM.dd - HH:mm"),
    PATTERN_11("2024.31.12 - 13:32", "yyyy.dd.MM - HH:mm"),
}