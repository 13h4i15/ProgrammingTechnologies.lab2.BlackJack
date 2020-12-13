package servermodel

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

@JsonAutoDetect
data class RoundNotification @JsonCreator constructor(
    @field:JsonProperty("is_next_round") @param:JsonProperty("is_next_round") @get:JsonProperty("is_next_round")
    val isNextRound: Boolean
)