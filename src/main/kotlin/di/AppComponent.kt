package di

import dagger.Component
import di.module.GsonModule
import di.module.IOModule
import di.module.SocketModule
import server.Session
import java.net.ServerSocket
import javax.inject.Singleton

@Singleton
@Component(modules = [SocketModule::class, GsonModule::class])
interface AppComponent {
    fun plusServerSocket(): ServerSocket

    fun plusIOComponent(ioModule: IOModule): IOComponent
}