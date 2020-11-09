package model

import servermodel.Card
import javax.inject.Inject
import javax.validation.constraints.Max
import javax.validation.constraints.Min

class PointsPool @Inject constructor() {
    private var _isClosed = false
    val isClosed: Boolean
        get() = _isClosed

    @Min(0)
    @Max(30)
    private var _points: Int = 0
        set(value) {
            field = value
            if (value >= 21) close()
        }
    val points: Int
        get() = _points

    fun addCard(card: Card) {
        _points += card.cost
    }

    fun close() {
        _isClosed = true
    }
}