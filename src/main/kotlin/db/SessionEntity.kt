package db

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Id
import dev.morphia.annotations.Property

@Entity("Session")
data class SessionEntity(
    @Id @Property("id") var id: Int? = null,
    var firstPlayer: PlayerEntity? = null,
    var secondPlayer: PlayerEntity? = null
)