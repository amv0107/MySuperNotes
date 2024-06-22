package com.amv.simple.app.mysupernotes.data.note

data class Attachment (
    val type: Type = Type.AUDIO,
    val path: String = "",
    val description: String = "",
    val fileName: String = "",
) {
    enum class Type {
        AUDIO,
        PHOTO,
        VIDEO,
        GENERIC
    }
}