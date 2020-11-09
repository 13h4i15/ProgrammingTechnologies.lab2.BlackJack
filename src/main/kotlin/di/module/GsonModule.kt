package di.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides

@Module
class GsonModule {
    @Provides
    fun gson(): Gson = Gson()
}