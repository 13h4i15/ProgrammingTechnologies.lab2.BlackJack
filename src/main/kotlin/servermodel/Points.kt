package servermodel

import com.google.gson.annotations.SerializedName
import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class Points(@Min(3) @Max(30) @SerializedName("points") val points: Int)