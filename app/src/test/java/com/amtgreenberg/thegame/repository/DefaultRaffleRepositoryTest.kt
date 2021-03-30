package com.amtgreenberg.thegame.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.amtgreenberg.thegame.data.ResultData
import com.amtgreenberg.thegame.datasource.local.RaffleDao
import com.amtgreenberg.thegame.model.Entry
import com.amtgreenberg.thegame.model.Participant
import junit.framework.Assert.fail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.time.OffsetDateTime


/**
 * Test suite for the [RaffleRepository] default implementation.
 */
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DefaultRaffleRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private lateinit var raffleRepository: RaffleRepository
    private lateinit var raffleDao: RaffleDao

    private val currentTime = OffsetDateTime.now()
    private val testEntries = arrayOf(
        Entry(1, "Adam", currentTime),
        Entry(2, "Dan", currentTime),
        Entry(3, "Roy", currentTime),
        Entry(4, "Henry", currentTime)
    )
    private val firstParticipant = Participant("Adam")
    private val testParticipants = arrayOf(
        firstParticipant,
        Participant("Dan"),
        Participant("Roy"),
        Participant("Henry")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        // Create a "fake" DAO
        raffleDao = TestRaffleDao()
        // Init the instance of RaffleRepository we are using
        raffleRepository = DefaultRaffleRepository(raffleDao, testCoroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    private fun addDefaultData() {
        (raffleDao as TestRaffleDao).entries.addAll(testEntries)
        (raffleDao as TestRaffleDao).participants.addAll(testParticipants)
    }

    @Test
    fun addEntry() {
        addDefaultData()
        runBlocking {
            when (val result = raffleRepository.addEntry(testParticipants[0])) {
                is ResultData.Success -> {
                    val prevHighestEntry = testEntries.maxBy { it.entryNumber }!!
                    assertThat(result.data.entryNumber).isEqualTo(prevHighestEntry.entryNumber + 1)
                    assertThat(result.data.participantName).isEqualTo(testParticipants[0].name)
                }
                is ResultData.Error -> {
                    fail()
                }
            }
        }
    }

    @Test
    fun addEntryNewParticipant() {
        runBlocking {
            when (val result = raffleRepository.addEntry(testParticipants[0])) {
                is ResultData.Success -> {
                    assertThat(result.data.entryNumber).isEqualTo(1)
                    assertThat(result.data.participantName).isEqualTo(testParticipants[0].name)
                }
                is ResultData.Error -> {
                    fail(result.exception.message)
                }
            }
        }
    }

    @Test
    fun addParticipant() {
        runBlocking {
            when (val result = raffleRepository.addParticipant(testParticipants[0])) {
                is ResultData.Success -> {
                    assertThat(result.data.name).isEqualTo(testParticipants[0].name)
                    assertThat((raffleDao as TestRaffleDao).participants)
                        .containsOnly(firstParticipant)
                }
                is ResultData.Error -> {
                    fail(result.exception.message)
                }
            }
        }
    }

    @Test
    fun addParticipantEntries() {
        (raffleDao as TestRaffleDao).participants.addAll(testParticipants)

        runBlocking {
            val entryIds = listOf(3, 70, 12, 29)
            when (val results = raffleRepository.addParticipantEntries(testParticipants[0], entryIds)) {
                is ResultData.Success -> {
                    assertThat(results.data).hasSize(entryIds.size)
                    val sortedEntryIds = entryIds.sortedDescending()
                    sortedEntryIds.forEachIndexed { index, id ->
                        
                    }

                }
                is ResultData.Error -> {
//  TODO                  fail(results.exception.message)
                }
            }
        }
    }

    @Test
    fun addNewParticipantEntries() {
    }

    @Test
    fun addParticipantEntriesOffsetMonthNonDefault() {
    }

    @Test
    fun addParticipantEntriesUnsortedEntryIds() {
    }

    @Test
    fun obtainRaffleWinner() {
    }

    @Test
    fun `invalidateOldEntries$app_debug`() {
    }

    @Test
    fun `getLastMonth$app_debug`() {
    }

    @Test
    fun `getThisMonthsEntries$app_debug`() {
    }

    @Test
    fun `getAllOfThisMonthsEntries$app_debug`() {
    }

    @Test
    fun `getBeginningMonth$app_debug`() {
    }
}