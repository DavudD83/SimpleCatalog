package davydov.dmytro.simplecatalog.catalog.data

import androidx.paging.*
import davydov.dmytro.simplecatalog.catalog.RxTestingRule
import davydov.dmytro.simplecatalog.catalog.anyNonNull
import davydov.dmytro.simplecatalog.catalog.data.CatalogRemoteMediator.Companion.MAX_ID
import davydov.dmytro.simplecatalog.catalog.data.CatalogRemoteMediator.Companion.NEXT_KEY_STRATEGY
import davydov.dmytro.simplecatalog.catalog.data.CatalogRemoteMediator.Companion.SINCE_ID
import davydov.dmytro.simplecatalog.core.storage.LocalStorage
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
@OptIn(ExperimentalPagingApi::class)
class CatalogRemoteMediatorTest {

    lateinit var mediator: CatalogRemoteMediator

    @Mock
    lateinit var api: CatalogApi

    @Mock
    lateinit var dao: CatalogItemsDao

    @Mock
    lateinit var mapper: RemoteToLocalItemMapper

    @Mock
    lateinit var localStorage: LocalStorage

    @get:Rule
    val rxTestRule = RxTestingRule()

    private val pagingState =
        PagingState<Int, LocalCatalogItem>(emptyList(), 0, PagingConfig(20), 0)

    private val localItems = listOf(
        LocalCatalogItem(0, "b", "text", "url", 0.5f),
        LocalCatalogItem(0, "a", "text", "url", 0.5f)
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mediator = CatalogRemoteMediator(api, dao, mapper, localStorage)
        `when`(api.loadCatalogItems(null, null)).thenReturn(Single.just(emptyList()))
        `when`(localStorage.getInt(anyString(), anyInt())).thenReturn(MAX_ID)
        `when`(mapper.map(anyNonNull())).thenReturn(localItems)
    }

    @Test
    fun `should invoke load page with null when refresh`() {
        mediator.loadSingle(LoadType.REFRESH, pagingState)
            .test()
        verify(api).loadCatalogItems(null, null)
    }

    @Test
    fun `should return success end reached when prepend`() {
        mediator.loadSingle(LoadType.PREPEND, pagingState)
            .test()
            .assertValue { it is RemoteMediator.MediatorResult.Success && it.endOfPaginationReached }
        verifyNoInteractions(api)
    }

    @Test
    fun `should invoke load page with last item catalog id as max id when append and max id strategy`() {
        `when`(localStorage.getInt(anyString(), anyInt())).thenReturn(MAX_ID)
        val maxId = "id"
        `when`(dao.getLastItem()).thenReturn(localItems.first().copy(catalogItemId = maxId))
        mediator.loadSingle(LoadType.APPEND, pagingState)
            .test()
        verify(api).loadCatalogItems(maxId, null)
    }

    @Test
    fun `should invoke load page with last item catalog id as since id when append and since id strategy`() {
        `when`(localStorage.getInt(anyString(), anyInt())).thenReturn(SINCE_ID)
        val id = "id"
        `when`(dao.getLastItem()).thenReturn(localItems.first().copy(catalogItemId = id))
        mediator.loadSingle(LoadType.APPEND, pagingState)
            .test()
        verify(api).loadCatalogItems(null, id)
    }

    @Test
    fun `should save max id strategy when refresh, first id bigger than last`() {
        mediator.loadSingle(LoadType.REFRESH, pagingState)
            .test()
        verify(localStorage).putInt(NEXT_KEY_STRATEGY, MAX_ID)
    }

    @Test
    fun `should save since id strategy when refresh, first id less than last`() {
        `when`(mapper.map(anyNonNull())).thenReturn(localItems.reversed())
        mediator.loadSingle(LoadType.REFRESH, pagingState)
            .test()
        verify(localStorage).putInt(NEXT_KEY_STRATEGY, SINCE_ID)
    }

    @Test
    fun `should return success end reached when last item null when append`() {
        `when`(dao.getLastItem()).thenReturn(null)
        mediator.loadSingle(LoadType.APPEND, pagingState)
            .test()
            .assertValue { it is RemoteMediator.MediatorResult.Success && it.endOfPaginationReached }
    }

    @Test
    fun `should replace all when successful refresh`() {
        mediator.loadSingle(LoadType.REFRESH, pagingState)
            .test()
        verify(dao).replaceAll(localItems)
    }

    @Test
    fun `should insert all when successful append`() {
        mediator.loadSingle(LoadType.REFRESH, pagingState)
            .test()
        verify(dao).replaceAll(localItems)
    }

    @Test
    fun `should return success with end not reached when page not empty`() {
        mediator.loadSingle(LoadType.REFRESH, pagingState)
            .test()
            .assertValue { it is RemoteMediator.MediatorResult.Success && !it.endOfPaginationReached }
    }

    @Test
    fun `should return success with end reached when page  empty`() {
        `when`(mapper.map(anyNonNull())).thenReturn(emptyList())
        mediator.loadSingle(LoadType.REFRESH, pagingState)
            .test()
            .assertValue { it is RemoteMediator.MediatorResult.Success && it.endOfPaginationReached }
    }

    @Test
    fun `should return error when error during loading page`() {
        val error = Throwable()
        `when`(api.loadCatalogItems(null, null)).thenReturn(Single.error(error))
        mediator.loadSingle(LoadType.REFRESH, pagingState)
            .test()
            .assertValue { it is RemoteMediator.MediatorResult.Error && it.throwable == error }
    }

    @Test
    fun `should return skip initial refresh when items present`() {
        `when`(dao.getItemsCount()).thenReturn(Single.just(10))
        mediator.initializeSingle()
            .test()
            .assertValue(RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH)
    }

    @Test
    fun `should return initial refresh when no items`() {
        `when`(dao.getItemsCount()).thenReturn(Single.just(0))
        mediator.initializeSingle()
            .test()
            .assertValue(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH)
    }
}