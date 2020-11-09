package servermodel

import com.google.gson.annotations.SerializedName

data class RoundNotification(@SerializedName("is_next_round") val isNextRound: Boolean)