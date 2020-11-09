package servermodel

import com.google.gson.annotations.SerializedName
import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class Card(@Min(1) @Max(11) @SerializedName("cost") val cost: Int)