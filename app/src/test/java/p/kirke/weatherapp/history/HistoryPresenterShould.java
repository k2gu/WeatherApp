package p.kirke.weatherapp.history;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import p.kirke.weatherapp.R;
import p.kirke.weatherapp.db.WeatherHistory;
import p.kirke.weatherapp.db.WeatherHistoryRepository;

import static org.mockito.Mockito.verify;

public class HistoryPresenterShould {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    HistoryView view;

    @Mock
    WeatherHistoryRepository repository;

    private HistoryPresenter presenter;
    private static final String ANY_NAME = "Any name";
    private static final String ANY_DATE = "10.12 2019";
    private static final int ANY_TEMP = 10;
    private static final int ANY_FEELABLE_TEMP = 11;

    @Before
    public void setUp() {
        presenter = new HistoryPresenter(view, repository);
    }

    @Test
    public void show_loading_on_start() {
        // nothing to prepare

        presenter.start();

        verify(view).showLoading();
    }

    @Test
    public void request_data_from_repository_on_start() {
        // nothing to prepare

        presenter.start();

        verify(repository).getAllData(presenter);
    }

    @Test
    public void pass_list_from_response_to_view_on_response_if_response_is_not_null_nor_empty() {
        List<WeatherHistory> historyList = getValidResponse();

        presenter.onResponse(historyList);

        verify(view).showList(historyList);
    }

    private List<WeatherHistory> getValidResponse() {
        List<WeatherHistory> historyList = new ArrayList<>();
        historyList.add(new WeatherHistory(ANY_NAME, ANY_TEMP, ANY_FEELABLE_TEMP, ANY_DATE));
        return historyList;
    }

    @Test
    public void hide_no_info_message_on_response_if_response_is_not_null_nor_empty() {
        List<WeatherHistory> historyList = getValidResponse();

        presenter.onResponse(historyList);

        verify(view).hideNoInfoYetMessage();
    }

    @Test
    public void hide_loading_on_start() {
        List<WeatherHistory> historyList = getValidResponse();

        presenter.onResponse(historyList);

        verify(view).hideLoading();
    }

    @Test
    public void show_no_info_message_if_response_is_null_on_response() {
        // nothing to prepare

        presenter.onResponse(null);

        verify(view).showNoInfoYetMessage();
    }

    @Test
    public void show_no_info_message_if_response_is_empty_on_response() {
        // nothing to prepare

        presenter.onResponse(new ArrayList<>());

        verify(view).showNoInfoYetMessage();
    }

    @Test
    public void show_error_on_error() {
        // nothing to prepare

        presenter.onError();

        verify(view).onError(R.string.error_generic);
    }
}
