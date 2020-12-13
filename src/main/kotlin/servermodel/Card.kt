package servermodel

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@JsonAutoDetect
data class Card @JsonCreator constructor(
    @field:JsonProperty("cost") @param:JsonProperty("cost") @get:JsonProperty("cost")
    @Min(1) @Max(11) val cost: Int
)