package servermodel

import com.google.gson.annotations.SerializedName
import javax.validation.constraints.Max
import javax.validation.constraints.Min

sealed class Result(
    @SerializedName("player_points") @Min(0) @Max(31) val playerPoints: Int,
    @SerializedName("opponent_points") @Min(0) @Max(31) val opponentPoints: Int
) {
    abstract val result: ResultState
}

class Winner(playerPoints: Int, opponentPoints: Int) : Result(playerPoints, opponentPoints) {
    @SerializedName("result")
    override val result = ResultState.WIN
}

class Loser(playerPoints: Int, opponentPoints: Int) : Result(playerPoints, opponentPoints) {
    @SerializedName("result")
    override val result = ResultState.LOSE
}

class Draw(playerPoints: Int, opponentPoints: Int) : Result(playerPoints, opponentPoints) {
    @SerializedName("result")
    override val result = ResultState.DRAW
}