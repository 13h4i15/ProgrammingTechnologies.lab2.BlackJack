package di

import dagger.Component
import di.module.*
import server.Session
import java.net.ServerSocket
import javax.inject.Singleton

@Singleton
@Component(modules = [SocketModule::class, GsonModule::class, MorphiaModule::class])
interface AppComponent {
    fun plusServerSocket(): ServerSocket

    fun plusIOComponent(ioModule: IOModule): IOComponent
}