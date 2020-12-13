package server

import com.fasterxml.jackson.databind.ObjectMapper
import db.PlayerEntity
import di.scope.SessionScope
import model.PointsPool
import repository.SaveSessionResultRepository
import servermodel.*
import servermodel.file.SessionFinal
import util.nextCard
import java.io.*
import java.net.Socket
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random

@SessionScope
class Session @Inject constructor(
    private val mapper: ObjectMapper,
    private val saveRepository: SaveSessionResultRepository,
    private val firstPlayerPool: PointsPool,
    private val secondPlayerPool: PointsPool,
    @Named("firstPlayer") private val firstPlayer: Socket,
    @Named("secondPlayer") private val secondPlayer: Socket,
    @Named("firstPlayer") private val firstPlayerReader: BufferedReader,
    @Named("secondPlayer") private val secondPlayerReader: BufferedReader,
    @Named("firstPlayer") private val firstPlayerWriter: PrintWriter,
    @Named("secondPlayer") private val secondPlayerWriter: PrintWriter,
    @Named("result") private val resultWriter: PrintWriter
) : Thread() {

    private val cardsOut = mutableListOf<Card>()

    private val isPoolsClosed: Boolean
        get() = firstPlayerPool.isClosed && secondPlayerPool.isClosed

    init {
        start()
    }

    override fun run() {
        super.run()

        try {
            generateSendAndSaveCard(firstPlayerWriter, firstPlayerPool)
            generateSendAndSaveCard(secondPlayerWriter, secondPlayerPool)

            firstPlayerWriter.println(mapper.writeValueAsString(RoundNotification(true)))

            var turnNumber = 0
            do {
                ++turnNumber

                val turningPlayerWriter = if (turnNumber % 2 == 1) firstPlayerWriter else secondPlayerWriter
                val turningPlayerReader = if (turnNumber % 2 == 1) firstPlayerReader else secondPlayerReader
                val turningPlayerPool = if (turnNumber % 2 == 1) firstPlayerPool else secondPlayerPool

                val opponentWriter = if (turnNumber % 2 == 1) secondPlayerWriter else firstPlayerWriter
                val opponentPool = if (turnNumber % 2 == 1) secondPlayerPool else firstPlayerPool

                if (turningPlayerPool.isClosed) {
                    sendRoundNotification(opponentWriter, true)
                    continue
                }

                if (mapper.readValue(turningPlayerReader.readLine(), Answer::class.java).isAdded) {
                    generateSendAndSaveCard(turningPlayerWriter, turningPlayerPool)
                    turningPlayerWriter.println(mapper.writeValueAsString(Points(turningPlayerPool.points)))

                    if (turningPlayerPool.points >= 21) {
                        if (!opponentPool.isClosed) {
                            sendRoundNotification(opponentWriter, false)
                            opponentPool.close()
                        }
                    }
                } else {
                    turningPlayerPool.close()
                }
                if (!opponentPool.isClosed) sendRoundNotification(opponentWriter, true)
            } while (!isPoolsClosed)

            val winnerScore: Int
            if (firstPlayerPool.points != secondPlayerPool.points) {
                val isFirstPlayerWinner = ((firstPlayerPool.points >= 21 || secondPlayerPool.points >= 21)
                        && (firstPlayerPool.points == 21 || secondPlayerPool.points > 21))
                        || ((firstPlayerPool.points < 21 && secondPlayerPool.points < 21)
                        && (firstPlayerPool.points > secondPlayerPool.points))

                val winner = if (isFirstPlayerWinner) firstPlayer else secondPlayer
                val loser = if (isFirstPlayerWinner) secondPlayer else firstPlayer

                val winnerWriter = if (isFirstPlayerWinner) firstPlayerWriter else secondPlayerWriter
                val loserWriter = if (isFirstPlayerWinner) secondPlayerWriter else firstPlayerWriter

                val winnerPointsPool = if (isFirstPlayerWinner) firstPlayerPool else secondPlayerPool
                val loserPointsPool = if (isFirstPlayerWinner) secondPlayerPool else firstPlayerPool

                winnerWriter.println(mapper.writeValueAsString(Winner(winnerPointsPool.points, loserPointsPool.points)))
                loserWriter.println(mapper.writeValueAsString(Loser(loserPointsPool.points, winnerPointsPool.points)))

                saveRepository.save(
                    PlayerEntity(winner.toString(), winnerPointsPool.points),
                    PlayerEntity(loser.toString(), loserPointsPool.points)
                )

                winnerScore = winnerPointsPool.points
            } else {
                val result = mapper.writeValueAsString(Draw(firstPlayerPool.points, secondPlayerPool.points))
                firstPlayerWriter.println(result)
                secondPlayerWriter.println(result)

                saveRepository.save(
                    PlayerEntity(firstPlayer.toString(), firstPlayerPool.points),
                    PlayerEntity(secondPlayer.toString(), secondPlayerPool.points)
                )

                winnerScore = firstPlayerPool.points
            }
            val final = SessionFinal(firstPlayer.toString(), secondPlayer.toString(), winnerScore)
            resultWriter.println(mapper.writeValueAsString(final))
        } catch (ioException: IOException) {
        } finally {
            firstPlayer.close()
            secondPlayer.close()
        }
    }

    private fun sendRoundNotification(writer: PrintWriter, isNext: Boolean) {
        writer.println(mapper.writeValueAsString(RoundNotification(isNext)))
    }

    private fun generateSendAndSaveCard(writer: PrintWriter, pool: PointsPool) {
        val card = Random.nextCard(cardsOut)
        cardsOut.add(card)
        writer.println(mapper.writeValueAsString(card))
        pool.addCard(card)
    }
}