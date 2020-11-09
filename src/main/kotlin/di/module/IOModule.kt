package di.module

import dagger.Module
import dagger.Provides
import di.scope.SessionScope
import java.io.*
import java.net.Socket
import javax.inject.Named

@Module
class IOModule(private val firstPlayer: Socket, private val secondPlayer: Socket) {

    @Provides
    @Named("firstPlayer")
    @SessionScope
    fun firstPlayerReader(): BufferedReader = BufferedReader(InputStreamReader(firstPlayer.getInputStream()))

    @Provides
    @Named("secondPlayer")
    @SessionScope
    fun secondPlayerReader(): BufferedReader = BufferedReader(InputStreamReader(secondPlayer.getInputStream()))

    @Provides
    @Named("firstPlayer")
    @SessionScope
    fun firstPlayerWriter(): PrintWriter =
        PrintWriter(BufferedWriter(OutputStreamWriter(firstPlayer.getOutputStream())), true)

    @Provides
    @Named("secondPlayer")
    @SessionScope
    fun secondPlayerWriter(): PrintWriter =
        PrintWriter(BufferedWriter(OutputStreamWriter(secondPlayer.getOutputStream())), true)
}