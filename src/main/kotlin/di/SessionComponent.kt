package di

import dagger.Subcomponent
import di.module.SessionModule
import di.scope.SessionScope
import server.Session

@SessionScope
@Subcomponent(modules = [SessionModule::class])
interface SessionComponent {
    fun plusSession(): Session
}