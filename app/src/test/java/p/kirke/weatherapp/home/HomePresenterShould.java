package p.kirke.weatherapp.home;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import p.kirke.weatherapp.PermissionHandler;
import p.kirke.weatherapp.PreferencesSingleton;
import p.kirke.weatherapp.R;
import p.kirke.weatherapp.db.WeatherHistory;
import p.kirke.weatherapp.db.WeatherHistoryRepository;
import p.kirke.weatherapp.model.WeatherCharacteristics;
import p.kirke.weatherapp.model.WeatherResponse;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

public class HomePresenterShould {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    HomeView view;

    @Mock
    PreferencesSingleton preferencesSingleton;

    @Mock
    WeatherHistoryRepository repository;

    @Mock
    PermissionHandler permissionHandler;

    @Mock
    LocationHandler locationHandler;
    @Captor
    private ArgumentCaptor<WeatherHistory> weatherHistoryCaptor;

    private HomePresenter presenter;
    private static final String ANY_NAME = "Any name";
    private static final String ANY_PICTURE_LOCATION = "Any location";
    private static final int ANY_TEMP = 10;
    private static final int ANY_FEELABLE_TEMP = 11;
    private static final String[] ANY_PERMISSION = new String[]{};
    private static final int[] ANY_RESULT = new int[]{};

    @Before
    public void setUp() {
        presenter = new HomePresenter(view, preferencesSingleton, repository, permissionHandler, locationHandler);
    }

    @Test
    public void show_name_on_start() {
        given(preferencesSingleton.getName()).willReturn(ANY_NAME);

        presenter.start();

        verify(view).showName(ANY_NAME);
    }

    @Test
    public void display_user_image_on_start() {
        given(preferencesSingleton.getPrefPictureLocation()).willReturn(ANY_PICTURE_LOCATION);

        presenter.start();

        verify(view).displayUserImage(ANY_PICTURE_LOCATION);
    }

    @Test
    public void show_loading_after_showing_picture_and_name_on_start() {
        given(preferencesSingleton.getName()).willReturn(ANY_NAME);
        given(preferencesSingleton.getPrefPictureLocation()).willReturn(ANY_PICTURE_LOCATION);

        presenter.start();

        InOrder order = inOrder(view);
        verify(view).showName(ANY_NAME);
        verify(view).displayUserImage(ANY_PICTURE_LOCATION);
        order.verify(view).showLoading(true);
    }

    @Test
    public void get_user_location_if_has_permissions_on_start() {
        given(permissionHandler.hasLocationPermission()).willReturn(true);

        presenter.start();

        verify(locationHandler).getUserLocation(presenter);
    }

    @Test
    public void request_location_permission_if_does_not_have_permissions_on_start() {
        given(permissionHandler.hasLocationPermission()).willReturn(false);

        presenter.start();

        verify(permissionHandler).requestLocationPermissions();
    }

    @Test
    public void hide_loading_on_location_result_if_has_this_days_info_and_location_hasnt_changed() {
        // TODO
    }

    @Test
    public void hide_loading_on_data_response() {
        // nothing to prepare

        presenter.onResponse(any());

        verify(view).showLoading(false);
    }

    @Test
    public void show_error_on_data_response_if_response_is_null() {
        // nothing to prepare

        presenter.onResponse(null);

        verify(view).onError(R.string.error_generic);
    }

    @Test
    public void notify_view_of_response_on_data_response_if_response_is_valid() {
        // nothing to prepare

        presenter.onResponse(getValidResponse());

        verify(view).showWeatherData(ANY_NAME, ANY_TEMP, ANY_FEELABLE_TEMP);
    }

    private WeatherResponse getValidResponse() {
        return new WeatherResponse(new WeatherCharacteristics(9.7324, 11.2342), ANY_NAME);
    }

    @Test
    public void save_data_to_preferences_singleton_on_response_if_response_is_valid() {
        // nothing to prepare

        presenter.onResponse(getValidResponse());

        verify(preferencesSingleton).setPrefLastKnownDate(any());
        verify(preferencesSingleton).setPrefLastKnownLocation(ANY_NAME);
    }

    @Test
    public void save_data_to_local_database_on_response_if_response_is_valid() {
        // nothing to prepare

        presenter.onResponse(getValidResponse());

        verify(repository).addNewHistoryElement(weatherHistoryCaptor.capture());
        WeatherHistory capturedValue = weatherHistoryCaptor.getValue();
        assertThat(capturedValue.subLocality).isEqualTo(ANY_NAME);
        assertThat(capturedValue.feelableTemperature).isEqualTo(ANY_FEELABLE_TEMP);
        assertThat(capturedValue.temperature).isEqualTo(ANY_TEMP);
    }

    @Test
    public void notify_view_of_image_selected() {
        // nothing to prepare

        presenter.onImageSelected(ANY_NAME);

        verify(view).displayUserImage(ANY_NAME);
    }

    @Test
    public void notify_view_of_error() {
        // nothing to prepare

        presenter.onError();

        verify(view).onError(R.string.error_generic);
    }

    @Test
    public void get_user_location_on_permission_response_if_permission_granted() {
        given(permissionHandler.isLocationPermissionGranted(0, ANY_PERMISSION,
                ANY_RESULT)).willReturn(true);

        presenter.onPermissionResponse(0, ANY_PERMISSION, ANY_RESULT);

        verify(locationHandler).getUserLocation(presenter);
    }

    @Test
    public void notify_view_of_error_if_permission_is_not_granted_on_permission_response() {
        given(permissionHandler.isLocationPermissionGranted(0, ANY_PERMISSION,
                ANY_RESULT)).willReturn(false);

        presenter.onPermissionResponse(0, ANY_PERMISSION, ANY_RESULT);

        verify(view).onError(R.string.error_denied_location);
    }
}

