package server

import di.AppComponent
import di.DaggerAppComponent
import di.module.SessionModule

val appComponent: AppComponent = DaggerAppComponent.builder().build()

fun main() {
    appComponent.plusServerSocket().use {
        while (true) {
            appComponent.plusSessionComponent(SessionModule(it.accept(), it.accept())).plusSession()
        }
    }
}