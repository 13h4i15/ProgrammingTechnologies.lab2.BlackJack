package di.module

import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Module
import dagger.Provides

@Module
class JsonModule {
    @Provides
    fun gson(): ObjectMapper = ObjectMapper()
}