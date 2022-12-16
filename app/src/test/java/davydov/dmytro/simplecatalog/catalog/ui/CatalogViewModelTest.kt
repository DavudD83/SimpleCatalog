package davydov.dmytro.simplecatalog.catalog.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import davydov.dmytro.simplecatalog.catalog.MainCoroutineScopeRule
import davydov.dmytro.simplecatalog.catalog.domain.LoadCatalogItemsUseCase
import davydov.dmytro.simplecatalog.catalog.navigation.CatalogNavigator
import davydov.dmytro.simplecatalog.core.logger.Logger
import davydov.dmytro.simplecatalog.core.ui.StringRepository
import io.reactivex.rxjava3.core.Flowable
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class CatalogViewModelTest {

    lateinit var viewModel: CatalogViewModel

    @Mock
    lateinit var loadCatalogItemsUseCase: LoadCatalogItemsUseCase

    @Mock
    lateinit var stringRepository: StringRepository

    @Mock
    lateinit var catalogNavigator: CatalogNavigator

    @Mock
    lateinit var generalStateUiObserver: Observer<GeneralCatalogUiState>

    @Mock
    lateinit var logger: Logger

    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(loadCatalogItemsUseCase.invoke()).thenReturn(Flowable.empty())
        viewModel =
            CatalogViewModel(loadCatalogItemsUseCase, stringRepository, catalogNavigator, logger)
        viewModel.generalState.observeForever(generalStateUiObserver)
    }

    @Test
    fun `should emit initial loading visible when refresh is loading`() {
        val state = CombinedLoadStates(
            refresh = LoadState.Loading,
            LoadState.NotLoading(false),
            LoadState.NotLoading(false),
            LoadStates(
                LoadState.Loading,
                LoadState.NotLoading(false),
                LoadState.NotLoading(false)
            )
        )
        val expected = GeneralCatalogUiState(initialLoadingVisible = true)
        viewModel.onLoadStateChanged(state, 0)
        verify(generalStateUiObserver).onChanged(expected)
    }

    @Test
    fun `should emit list visible when refresh is not loading`() {
        val state = CombinedLoadStates(
            refresh = LoadState.NotLoading(false),
            LoadState.NotLoading(false),
            LoadState.NotLoading(false),
            LoadStates(
                LoadState.Loading,
                LoadState.NotLoading(false),
                LoadState.NotLoading(false)
            )
        )
        val expected = GeneralCatalogUiState(listVisible = true)
        viewModel.onLoadStateChanged(state, 0)
        verify(generalStateUiObserver).onChanged(expected)
    }

    @Test
    fun `should emit error text when refresh is error`() {
        val state = CombinedLoadStates(
            refresh = LoadState.Error(Throwable()),
            LoadState.NotLoading(false),
            LoadState.NotLoading(false),
            LoadStates(
                LoadState.Loading,
                LoadState.NotLoading(false),
                LoadState.NotLoading(false)
            )
        )
        val expectedText = "Text"
        `when`(stringRepository.getString(anyInt())).thenReturn(expectedText)
        val expected = GeneralCatalogUiState(initialErrorText = expectedText)
        viewModel.onLoadStateChanged(state, 0)
        verify(generalStateUiObserver).onChanged(expected)
    }

    @Test
    fun `should emit empty text when refresh is not loading, append reached end, no items`() {
        val state = CombinedLoadStates(
            refresh = LoadState.NotLoading(false),
            LoadState.NotLoading(false),
            append = LoadState.NotLoading(true),
            LoadStates(
                LoadState.Loading,
                LoadState.NotLoading(false),
                LoadState.NotLoading(false)
            )
        )
        val expectedText = "Text"
        `when`(stringRepository.getString(anyInt())).thenReturn(expectedText)
        val expected = GeneralCatalogUiState(emptyItemsText = expectedText)
        viewModel.onLoadStateChanged(state, 0)
        verify(generalStateUiObserver).onChanged(expected)
    }
}