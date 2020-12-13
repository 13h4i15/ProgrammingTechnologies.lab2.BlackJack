package di.module

import dagger.Module
import dagger.Provides
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.PrintWriter
import javax.inject.Named
import javax.inject.Singleton

@Module
class FileModule {
    @Provides
    @Named("result")
    @Singleton
    fun resultWrite(): PrintWriter {
        return PrintWriter(BufferedWriter(FileWriter("results.txt", true)), true)
    }
}