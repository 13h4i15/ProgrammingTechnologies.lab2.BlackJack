package di

import dagger.Subcomponent
import di.module.IOModule
import di.scope.SessionScope
import server.Session

@SessionScope
@Subcomponent(modules = [IOModule::class])
interface IOComponent {
    fun inject(session: Session)
}