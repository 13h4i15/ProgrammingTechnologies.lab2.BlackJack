package di.module

import com.mongodb.MongoClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MongoDbModule {
    @Provides
    @Singleton
    fun mongoClient(): MongoClient {
        return MongoClient()
    }
}