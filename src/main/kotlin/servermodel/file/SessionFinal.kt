package servermodel.file

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.jsonSchema.annotation.JsonHyperSchema

@JsonAutoDetect
@JsonHyperSchema
data class SessionFinal @JsonCreator constructor(
    @field:JsonProperty("first_player") @param:JsonProperty("first_player") @get:JsonProperty("first_player") val firstPlayer: String,
    @field:JsonProperty("second_player") @param:JsonProperty("second_player") @get:JsonProperty("second_player") val secondPlayer: String,
    @field:JsonProperty("winner_score") @param:JsonProperty("winner_score") @get:JsonProperty("winner_score") val winnerScore: Int
) {
}