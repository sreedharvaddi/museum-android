package com.sree.museum.ui.nav

sealed class Destination(val route: String) {
    object Home : Destination("home")
    object Detail : Destination("detail?path={path}") {
        fun createRoute(path: String) = "detail?path=${java.net.URLEncoder.encode(path, "UTF-8")}"
    }
}