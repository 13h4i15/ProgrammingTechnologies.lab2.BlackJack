package clientmodel;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import servermodel.ResultState;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@JsonAutoDetect
public class Result {
    @JsonProperty("result")
    @NotNull ResultState resultState;

    @JsonProperty("player_points")
    @Max(30)
    @Min(3)
    int playerPoints;

    @Max(30)
    @Min(3)
    @JsonProperty("opponent_points")
    int opponentPoints;

    @JsonCreator
    public Result(@NotNull @JsonProperty("result") ResultState resultState, @JsonProperty("player_points") int playerPoints, @JsonProperty("opponent_points") int opponentPoints) {
        this.resultState = resultState;
        this.playerPoints = playerPoints;
        this.opponentPoints = opponentPoints;
    }

    @Override
    public String toString() {
        return String.format("Result: %s! Your points: %d; Opponent points: %d", resultState.toString(), playerPoints, opponentPoints);
    }
}
