package db

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Property

@Entity("Player")
data class PlayerEntity(
    @Property("ip4") var ip4: String? = null,
    @Property("score") var score: Int? = null,
)