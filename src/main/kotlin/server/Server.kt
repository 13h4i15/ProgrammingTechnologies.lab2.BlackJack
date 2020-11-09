package server

import di.AppComponent
import di.DaggerAppComponent

val appComponent: AppComponent = DaggerAppComponent.builder().build()

fun main() {
    appComponent.plusServerSocket().use {
        while (true) {
            Session(it.accept(), it.accept())
        }
    }
}