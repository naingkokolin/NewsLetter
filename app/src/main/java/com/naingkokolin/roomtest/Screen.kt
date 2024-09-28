package com.naingkokolin.roomtest

sealed class Screen(var route: String) {
    object FirstScreen: Screen("first_screen")
    object DetailScreen: Screen("detail_screen")
}