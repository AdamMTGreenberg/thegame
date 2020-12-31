package com.amtgreenberg.thegame.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.amtgreenberg.thegame.datasource.local.RaffleDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit


/**
 * Test suite for the [RaffleRepository] default implementation.
 */
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DefaultRaffleRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Rule
    var mockitoRule = MockitoJUnit.rule()

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private lateinit var raffleRepository: RaffleRepository
    private lateinit var raffleDao: RaffleDao

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

    @Test
    fun addEntry() {

    }

    @Test
    fun addParticipant() {
    }

    @Test
    fun addParticipantEntries() {
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