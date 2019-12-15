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
import p.kirke.weatherapp.util.Const;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
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

        presenter.start(true);

        verify(view).showName(ANY_NAME);
    }

    @Test
    public void show_loading_after_showing_name_on_start() {
        given(preferencesSingleton.getName()).willReturn(ANY_NAME);
        given(permissionHandler.hasReadExternalStoragePermission()).willReturn(true);

        presenter.start(true);

        InOrder order = inOrder(view);
        order.verify(view).showLoading(true);
    }

    @Test
    public void not_show_loading_on_start_if_no_internet_connection() {
        given(preferencesSingleton.getName()).willReturn(ANY_NAME);
        given(preferencesSingleton.getPrefPictureLocation()).willReturn(ANY_PICTURE_LOCATION);

        presenter.start(false);

        verify(view, never()).showLoading(true);
    }

    @Test
    public void show_error_on_start_if_no_internet_connection() {
        // nothing to prepare

        presenter.start(false);

        verify(view).onError(R.string.error_no_internet);
    }

    @Test
    public void get_user_location_if_has_permissions_on_start() {
        given(permissionHandler.hasLocationPermission()).willReturn(true);

        presenter.start(true);

        verify(locationHandler).getUserLocation(presenter);
    }

    @Test
    public void request_location_permission_if_does_not_have_permissions_on_start() {
        given(permissionHandler.hasLocationPermission()).willReturn(false);

        presenter.start(true);

        verify(permissionHandler).requestLocationPermissions();
    }

    @Test
    public void hide_loading_on_data_response_if_is_cache() {
        // nothing to prepare

        presenter.onResponse(null, true);

        verify(view).showLoading(false);
    }

    @Test
    public void hide_loading_on_data_response_if_is_not_cache() {
        // nothing to prepare

        presenter.onResponse(null, false);

        verify(view).showLoading(false);
    }

    @Test
    public void show_error_on_data_response_if_response_is_null() {
        // nothing to prepare

        presenter.onResponse(null, true);

        verify(view).onError(R.string.error_generic);
    }

    @Test
    public void notify_view_of_response_on_data_response_if_response_is_valid() {
        // nothing to prepare

        presenter.onResponse(getValidResponse(), true);

        verify(view).showWeatherData(ANY_NAME, ANY_TEMP, ANY_FEELABLE_TEMP);
    }

    private WeatherResponse getValidResponse() {
        return new WeatherResponse(new WeatherCharacteristics(9.7324, 11.2342), ANY_NAME);
    }

    @Test
    public void save_data_to_preferences_singleton_on_response_if_response_is_valid() {
        // nothing to prepare

        presenter.onResponse(getValidResponse(), false);

        verify(preferencesSingleton).setPrefLastKnownDate(any());
        verify(preferencesSingleton).setPrefLastKnownLocation(ANY_NAME);
    }

    @Test
    public void save_data_to_local_database_on_response_if_response_is_valid() {
        // nothing to prepare

        presenter.onResponse(getValidResponse(), false);

        verify(repository).addNewHistoryElement(weatherHistoryCaptor.capture());
        WeatherHistory capturedValue = weatherHistoryCaptor.getValue();
        assertThat(capturedValue.subLocality).isEqualTo(ANY_NAME);
        assertThat(capturedValue.feelableTemperature).isEqualTo(ANY_FEELABLE_TEMP);
        assertThat(capturedValue.temperature).isEqualTo(ANY_TEMP);
    }

    @Test
    public void not_save_data_to_preferences_singleton_on_response_if_response_is_valid_but_is_cache() {
        // nothing to prepare

        presenter.onResponse(getValidResponse(), true);

        verify(preferencesSingleton, never()).setPrefLastKnownDate(any());
        verify(preferencesSingleton, never()).setPrefLastKnownLocation(ANY_NAME);
    }

    @Test
    public void not_save_data_to_local_database_on_response_if_response_is_valid_but_is_cache() {
        // nothing to prepare

        presenter.onResponse(getValidResponse(), true);

        verify(repository, never()).addNewHistoryElement(weatherHistoryCaptor.capture());
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
        given(permissionHandler.isLocationPermissionGranted(Const.LOCATION_REQUEST_CODE, ANY_PERMISSION,
                ANY_RESULT)).willReturn(true);

        presenter.onPermissionResponse(Const.LOCATION_REQUEST_CODE, ANY_PERMISSION, ANY_RESULT);

        verify(locationHandler).getUserLocation(presenter);
    }

    @Test
    public void notify_view_of_error_if_permission_is_not_granted_on_permission_response() {
        given(permissionHandler.isLocationPermissionGranted(Const.LOCATION_REQUEST_CODE, ANY_PERMISSION,
                ANY_RESULT)).willReturn(false);

        presenter.onPermissionResponse(Const.LOCATION_REQUEST_CODE, ANY_PERMISSION, ANY_RESULT);

        verify(view).onError(R.string.error_denied_location);
    }

    @Test
    public void display_user_image_on_permission_request_if_is_external_permission_response_and_is_successful() {
        given(preferencesSingleton.getPrefPictureLocation()).willReturn(ANY_PICTURE_LOCATION);
        given(permissionHandler.isReadExternalStoragePermissionGranted(Const.READ_EXTERNAL_STORAGE_REQUEST_CODE, ANY_PERMISSION,
                ANY_RESULT)).willReturn(true);

        presenter.onPermissionResponse(Const.READ_EXTERNAL_STORAGE_REQUEST_CODE, ANY_PERMISSION, ANY_RESULT);

        verify(view).displayUserImage(ANY_PICTURE_LOCATION);
    }

    @Test
    public void show_error_on_permission_request_if_is_external_permission_response_and_is_not_successful() {
        given(permissionHandler.isLocationPermissionGranted(Const.READ_EXTERNAL_STORAGE_REQUEST_CODE, ANY_PERMISSION,
                ANY_RESULT)).willReturn(false);

        presenter.onPermissionResponse(Const.READ_EXTERNAL_STORAGE_REQUEST_CODE, ANY_PERMISSION, ANY_RESULT);

        verify(view).onError(R.string.error_denied_external_storage);
    }

    @Test
    public void show_image_placeholder_if_is_external_permission_response_and_is_not_successful() {
        given(permissionHandler.isLocationPermissionGranted(Const.READ_EXTERNAL_STORAGE_REQUEST_CODE, ANY_PERMISSION,
                ANY_RESULT)).willReturn(false);

        presenter.onPermissionResponse(Const.READ_EXTERNAL_STORAGE_REQUEST_CODE, ANY_PERMISSION, ANY_RESULT);

        verify(view).showImagePlaceholder();
    }

    @Test
    public void request_external_storage_permission_on_handle_user_image_if_does_not_have_it() {
        given(permissionHandler.hasReadExternalStoragePermission()).willReturn(false);

        presenter.handleUserImage();

        verify(permissionHandler).requestReadExternalStoragePermission();
    }

    @Test
    public void display_user_image_on_start_if_has_external_storage_permission() {
        given(permissionHandler.hasReadExternalStoragePermission()).willReturn(true);
        given(preferencesSingleton.getPrefPictureLocation()).willReturn(ANY_PICTURE_LOCATION);

        presenter.handleUserImage();

        verify(view).displayUserImage(ANY_PICTURE_LOCATION);
    }
}

