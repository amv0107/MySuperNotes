package com.amv.simple.app.mysupernotes.domain.util

//sealed class TypeList{
//    object ArchiveList: TypeList()
//    object FavoriteList: TypeList()
//    object DeleteList: TypeList()
//    object MainList: TypeList()
//}

enum class TypeList{
    ARCHIVE_LIST,
    FAVORITE_LIST,
    DELETE_LIST,
    MAIN_LIST,
}
