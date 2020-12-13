package di

import dagger.Component
import di.module.*
import java.net.ServerSocket
import javax.inject.Singleton

@Singleton
@Component(modules = [SocketModule::class, JsonModule::class, MorphiaModule::class, FileModule::class])
interface AppComponent {
    fun plusServerSocket(): ServerSocket

    fun plusSessionComponent(sessionModule: SessionModule): SessionComponent
}