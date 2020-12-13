package servermodel

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

@JsonAutoDetect
data class Answer @JsonCreator constructor(
    @field:JsonProperty("is_added") @param:JsonProperty("is_added") @get:JsonProperty("is_added")
    val isAdded: Boolean
)