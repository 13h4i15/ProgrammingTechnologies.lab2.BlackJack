package repository

import db.PlayerEntity
import db.SessionEntity
import dev.morphia.Datastore
import javax.inject.Inject
import javax.inject.Named

class SaveSessionResultRepository @Inject constructor(
    @Named("sessions") private val datastore: Datastore
) {
    fun save(winner: PlayerEntity, loser: PlayerEntity) {
        val id =
            datastore.createQuery(SessionEntity::class.java).filter { it.id != null }.maxByOrNull { it.id!! }?.id ?: -1
        datastore.save(SessionEntity(id + 1, winner, loser))
    }
}