package di.module

import com.mongodb.MongoClient
import dagger.Module
import dagger.Provides
import dev.morphia.Datastore
import dev.morphia.Morphia
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [MongoDbModule::class])
class MorphiaModule {
    @Provides
    @Singleton
    @Named("sessions")
    fun datastore(morphia: Morphia, mongoClient: MongoClient): Datastore {
        return morphia.createDatastore(mongoClient, "sessions")
    }

    @Provides
    @Singleton
    fun morphia(): Morphia = Morphia()
}