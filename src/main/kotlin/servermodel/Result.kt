package servermodel

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@JsonAutoDetect
sealed class Result @JsonCreator constructor(
    @field:JsonProperty("player_points") @param:JsonProperty("player_points") @get:JsonProperty("player_points")
    @Min(0) @Max(31) val playerPoints: Int,
    @field:JsonProperty("opponent_points") @param:JsonProperty("opponent_points") @get:JsonProperty("opponent_points")
    @Min(0) @Max(31) val opponentPoints: Int
) {
    abstract val result: ResultState
}

@JsonAutoDetect
class Winner @JsonCreator constructor(playerPoints: Int, opponentPoints: Int) : Result(playerPoints, opponentPoints) {
    @JsonProperty("result")
    override val result = ResultState.WIN
}

@JsonAutoDetect
class Loser @JsonCreator constructor(playerPoints: Int, opponentPoints: Int) : Result(playerPoints, opponentPoints) {
    @JsonProperty("result")
    override val result = ResultState.LOSE
}

@JsonAutoDetect
class Draw @JsonCreator constructor(playerPoints: Int, opponentPoints: Int) : Result(playerPoints, opponentPoints) {
    @JsonProperty("result")
    override val result = ResultState.DRAW
}