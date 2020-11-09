package clientmodel;

import servermodel.ResultState;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class Result {
    @SerializedName("result")
    @NotNull ResultState resultState;

    @SerializedName("player_points")
    @Max(30)
    @Min(3)
    int playerPoints;

    @Max(30)
    @Min(3)
    @SerializedName("opponent_points")
    int opponentPoints;

    public Result(@NotNull ResultState resultState, int playerPoints, int opponentPoints) {
        this.resultState = resultState;
        this.playerPoints = playerPoints;
        this.opponentPoints = opponentPoints;
    }

    @Override
    public String toString() {
        return String.format("Result: %s! Your points: %d; Opponent points: %d", resultState.toString(), playerPoints, opponentPoints);
    }
}
