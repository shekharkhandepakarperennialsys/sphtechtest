package com.sphtech.test

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.stub
import com.nhaarman.mockito_kotlin.whenever
import com.sphtech.shared.core.base.BaseRepository
import com.sphtech.shared.database.SPHTechDao
import com.sphtech.shared.database.SPHTechRoomRepository
import com.sphtech.shared.entities.DataAmountResponse
import com.sphtech.shared.entities.RecordsData
import com.sphtech.shared.network.ApiClient
import com.sphtech.shared.network.ApiService
import com.sphtech.shared.network.repository.DataAmountRepository
import com.sphtech.test.ui.home.HomeViewModel
import io.reactivex.Single
import kotlinx.coroutines.CompletableDeferred
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class DataTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var vm: HomeViewModel

    @Mock
    private lateinit var context: Context

    @Mock
    var apiClient: ApiClient = mock()
    private lateinit var repo: DataAmountRepository
    private lateinit var factDatabaseRepository: SPHTechRoomRepository

    var apiService: ApiService = mock()
    var baseRepository: BaseRepository = mock()
    var facts = ArrayList<RecordsData>()

    @Mock
    lateinit var factInfoDao: SPHTechDao

    private var resourceId = "a807b7ab-6cad-4aa6-87d0-e283a7353a0f"
    private var limit = 50

    @Before
    fun setup() {
        context = mock(Context::class.java)
        factDatabaseRepository = SPHTechRoomRepository(context)
        apiService.apiClient = apiClient
        repo = DataAmountRepository(apiService, baseRepository)
        factDatabaseRepository.SPHTechDao = factInfoDao
        vm = HomeViewModel(context, repo)
        vm.allList = facts
    }

//    @Test
//    fun testAPICall() {
//        val mockresponse = null
//        Mockito.`when`(apiService.apiClient.callDataAmount(resourceId, limit))
//            .then(mockresponse)
//    }

    @Test
    fun testAPICall2() {
        vm.dataAmountAPIObserver1.observeForever {
            assert(true)
        }
        vm.callDataAmountAPI()
    }

    @Test
    fun testAPICall3() {
        val service = mock<ApiService> {
            on { callDataAmountAPIAsync(resourceId, limit) } doReturn tests
        }
        verify(service).callDataAmountAPIAsync(resourceId, limit).toDeferred()
    }

    @Test
    fun testDataList() {
        assert(vm.allList.size == 0)
    }

    @Test
    fun testDBCall() {
        initDummy()
        Mockito.`when`(factDatabaseRepository.SPHTechDao?.getList())
            .thenReturn(facts)
        Assert.assertNotNull(vm.allList)
    }

    @Test
    fun testDBSize() {
        initDummy()
        Mockito.`when`(factDatabaseRepository.SPHTechDao?.getList())
            .thenReturn(vm.allList)
        Assert.assertEquals(2, vm.allList.size)
    }

    @Test
    fun testDBInsert() {
        initDummy()
        Mockito.`when`(factDatabaseRepository.SPHTechDao?.saveList(facts))
            .thenReturn(null)
        Mockito.`when`(factDatabaseRepository.SPHTechDao?.getList())
            .thenReturn(facts)
        Assert.assertEquals(2, facts.size)
    }

    @Test
    fun testTileShown() {
        initDummy()
        Mockito.`when`(factDatabaseRepository.SPHTechDao?.getList())
            .thenReturn(facts)
        Assert.assertEquals("2011-Q1", vm.allList[0].quarter)
        Assert.assertEquals("0.001", vm.allList[0].volumeOfMobileData)
    }

    private fun initDummy() {
        facts.add(RecordsData("0.001", "2011-Q1", 1))
        facts.add(RecordsData("0.011", "2011-Q2", 2))
    }
}

fun <T> T.toDeferred() = CompletableDeferred(this)
