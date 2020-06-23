package com.sphtech.test

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.mock
import com.sphtech.shared.core.base.BaseAPIRepository
import com.sphtech.shared.database.SPHTechDao
import com.sphtech.shared.database.SPHTechRoomRepository
import com.sphtech.shared.entities.DataAmountResponse
import com.sphtech.shared.entities.RecordsData
import com.sphtech.shared.network.ApiClient
import com.sphtech.shared.network.ApiService
import com.sphtech.shared.network.repository.DataAmountRepository
import com.sphtech.test.ui.home.HomeViewModel
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.Mockito.mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.util.concurrent.Callable


@RunWith(MockitoJUnitRunner::class)
class DataTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var homeViewModel: HomeViewModel

    @Mock
    private lateinit var context: Context

    @Mock
    var apiClient: ApiClient = mock()
    private lateinit var dataAmountRepository: DataAmountRepository
    private lateinit var sphTechRoomRepository: SPHTechRoomRepository

    var apiService: ApiService = mock()
    var baseAPIRepository: BaseAPIRepository = mock()
    var recordsDataList = ArrayList<RecordsData>()

    @Mock
    lateinit var sphTechDao: SPHTechDao

    private var resourceId = "a807b7ab-6cad-4aa6-87d0-e283a7353a0f"
    private var limit = 50

    @Spy
    lateinit var deferred: Deferred<Response<DataAmountResponse>>
    @Mock
    lateinit var results: Response<DataAmountResponse>

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler?>? -> Schedulers.trampoline() }
        context = mock(Context::class.java)
        sphTechRoomRepository = SPHTechRoomRepository(context)
        apiService.apiClient = apiClient
        dataAmountRepository = DataAmountRepository(apiService, baseAPIRepository)
        sphTechRoomRepository.sphTechDao = sphTechDao
        homeViewModel = HomeViewModel(context, dataAmountRepository)
        homeViewModel.allList = recordsDataList
    }

    @Test
    fun testDataList() {
        assert(homeViewModel.allList.size == 0)
    }

    @Test
    fun testAPICall() {
        homeViewModel.dataAmountAPIObserver.observeForever {
            assert(true)
        }
        homeViewModel.callDataAmountAPI()
    }

    @Test
    fun testApi(): Unit = runBlocking {
        lenient().`when`(apiClient.callDataAmount(resourceId, limit)).thenReturn(deferred)
        lenient().`when`(deferred.await()).thenReturn(results)
        val liveData: List<RecordsData> = homeViewModel.allList
        delay(5000L)
        Assert.assertEquals(liveData, recordsDataList)
    }

    @Test
    fun testDBCall() {
        initDummy()
        lenient().`when`(sphTechRoomRepository.sphTechDao?.getList())
            .thenReturn(recordsDataList)
        Assert.assertNotNull(homeViewModel.allList)
    }

    @Test
    fun testDBSize() {
        initDummy()
        lenient().`when`(sphTechRoomRepository.sphTechDao?.getList())
            .thenReturn(homeViewModel.allList)
        Assert.assertEquals(2, homeViewModel.allList.size)
    }

    @Test
    fun testDBInsert() {
        initDummy()
        lenient().`when`(sphTechRoomRepository.sphTechDao?.saveList(recordsDataList))
            .thenReturn(null)
        lenient().`when`(sphTechRoomRepository.sphTechDao?.getList())
            .thenReturn(recordsDataList)
        Assert.assertEquals(2, recordsDataList.size)
    }

    @Test
    fun testTileShown() {
        initDummy()
        lenient().`when`(sphTechRoomRepository.sphTechDao?.getList())
            .thenReturn(recordsDataList)
        Assert.assertEquals("2011-Q1", homeViewModel.allList[0].quarter)
        Assert.assertEquals("0.001", homeViewModel.allList[0].volumeOfMobileData)
    }

    private fun initDummy() {
        recordsDataList.add(RecordsData("0.001", "2011-Q1", 1))
        recordsDataList.add(RecordsData("0.011", "2011-Q2", 2))
    }
}

