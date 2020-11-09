package di.module

import dagger.Module
import dagger.Provides
import java.net.ServerSocket
import javax.inject.Singleton

@Module
class SocketModule {
    @Singleton
    @Provides
    fun socket(): ServerSocket = ServerSocket(1488)
}