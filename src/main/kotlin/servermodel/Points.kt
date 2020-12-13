package servermodel

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@JsonAutoDetect
data class Points @JsonCreator constructor(
    @field:JsonProperty("points") @param:JsonProperty("points") @get:JsonProperty("points")
    @Min(3) @Max(30) val points: Int
)