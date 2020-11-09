package server

import com.google.gson.Gson
import db.PlayerEntity
import di.module.IOModule
import model.PointsPool
import repository.SaveSessionResultRepository
import servermodel.*
import util.nextCard
import java.io.*
import java.net.Socket
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random

class Session(private val firstPlayer: Socket, private val secondPlayer: Socket) : Thread() {
    @Inject
    lateinit var saveRepository: SaveSessionResultRepository

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var firstPlayerPool: PointsPool

    @Inject
    lateinit var secondPlayerPool: PointsPool

    @Inject
    @Named("firstPlayer")
    lateinit var firstPlayerReader: BufferedReader

    @Inject
    @Named("secondPlayer")
    lateinit var secondPlayerReader: BufferedReader

    @Inject
    @Named("firstPlayer")
    lateinit var firstPlayerWriter: PrintWriter

    @Inject
    @Named("secondPlayer")
    lateinit var secondPlayerWriter: PrintWriter

    private val cardsOut = mutableListOf<Card>()

    private val isPoolsClosed: Boolean
        get() = firstPlayerPool.isClosed && secondPlayerPool.isClosed

    init {
        appComponent.plusIOComponent(IOModule(firstPlayer, secondPlayer)).inject(this)
        start()
    }

    override fun run() {
        super.run()

        try {
            generateSendAndSaveCard(firstPlayerWriter, firstPlayerPool)
            generateSendAndSaveCard(secondPlayerWriter, secondPlayerPool)

            firstPlayerWriter.println(gson.toJson(RoundNotification(true)))

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

                if (gson.fromJson(turningPlayerReader.readLine(), Answer::class.java).isAdded) {
                    generateSendAndSaveCard(turningPlayerWriter, turningPlayerPool)
                    turningPlayerWriter.println(gson.toJson(Points(turningPlayerPool.points)))

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

                winnerWriter.println(gson.toJson(Winner(winnerPointsPool.points, loserPointsPool.points)))
                loserWriter.println(gson.toJson(Loser(loserPointsPool.points, winnerPointsPool.points)))

                saveRepository.save(
                    PlayerEntity(winner.toString(), winnerPointsPool.points),
                    PlayerEntity(loser.toString(), loserPointsPool.points)
                )
            } else {
                val result = gson.toJson(Draw(firstPlayerPool.points, secondPlayerPool.points))
                firstPlayerWriter.println(result)
                secondPlayerWriter.println(result)

                saveRepository.save(
                    PlayerEntity(firstPlayer.toString(), firstPlayerPool.points),
                    PlayerEntity(secondPlayer.toString(), secondPlayerPool.points)
                )
            }
        } catch (ioException: IOException) {
        } finally {
            firstPlayer.close()
            secondPlayer.close()
        }
    }

    private fun sendRoundNotification(writer: PrintWriter, isNext: Boolean) {
        writer.println(gson.toJson(RoundNotification(isNext)))
    }

    private fun generateSendAndSaveCard(writer: PrintWriter, pool: PointsPool) {
        val card = Random.nextCard(cardsOut)
        cardsOut.add(card)
        writer.println(gson.toJson(card))
        pool.addCard(card)
    }
}