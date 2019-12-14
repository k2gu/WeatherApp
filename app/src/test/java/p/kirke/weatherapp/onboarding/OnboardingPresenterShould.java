package p.kirke.weatherapp.onboarding;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import p.kirke.weatherapp.PermissionHandler;
import p.kirke.weatherapp.PreferencesSingleton;
import p.kirke.weatherapp.R;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class OnboardingPresenterShould {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    OnboardingView view;

    @Mock
    PreferencesSingleton preferencesSingleton;

    @Mock
    PermissionHandler permissionHandler;

    private OnBoardingPresenter presenter;
    private static final String ANY_NAME = "Any name";
    private static final String ANY_NAME_WITH_SPACES = "     Any name      ";
    private static final String[] ANY_PERMISSION = new String[]{};
    private static final int[] ANY_RESULT = new int[]{};

    @Before
    public void setUp() {
        presenter = new OnBoardingPresenter(view, preferencesSingleton, permissionHandler);
    }

    @Test
    public void save_name_to_preferences_singleton_on_start_if_name_is_not_empty() {
        // nothing to prepare

        presenter.start(ANY_NAME);

        verify(preferencesSingleton).setName(ANY_NAME);
    }

    @Test
    public void trim_name_before_saving_name_to_preferences_singleton_on_start_if_name_is_not_empty() {
        // nothing to prepare

        presenter.start(ANY_NAME_WITH_SPACES);

        verify(preferencesSingleton).setName(ANY_NAME);
    }

    @Test
    public void ask_view_to_open_gallery_on_save_name_if_name_is_not_empty_and_has_external_storage_permissions() {
        given(permissionHandler.hasReadExternalStoragePermission()).willReturn(true);

        presenter.start(ANY_NAME);

        verify(view).openGallery();
    }

    @Test
    public void request_permissions_on_save_name_if_name_is_not_empty_but_does_not_have_external_storage_permissions() {
        given(permissionHandler.hasReadExternalStoragePermission()).willReturn(false);

        presenter.start(ANY_NAME);

        verify(permissionHandler).requestReadExternalStoragePermission();
    }

    @Test
    public void show_error_on_start_if_name_is_empty() {
        // nothing to prepare

        presenter.start("");

        verify(view).onError(R.string.error_no_name_inserted);
    }

    @Test
    public void show_error_on_start_if_name_is_null() {
        // nothing to prepare

        presenter.start(null);

        verify(view).onError(R.string.error_no_name_inserted);
    }

    @Test
    public void open_gallery_on_request_permission_response_if_has_permissions() {
        given(permissionHandler.isReadExternalStoragePermissionGranted(0, ANY_PERMISSION,
                ANY_RESULT)).willReturn(true);

        presenter.onRequestPermissionResponse(0, ANY_PERMISSION, ANY_RESULT);

        verify(view).openGallery();
    }

    @Test
    public void show_error_request_permission_response_if_does_not_have_permissions() {
        given(permissionHandler.isReadExternalStoragePermissionGranted(0, ANY_PERMISSION,
                ANY_RESULT)).willReturn(false);

        presenter.onRequestPermissionResponse(0, ANY_PERMISSION, ANY_RESULT);

        verify(view).onError(R.string.error_denied_external_storage);
    }

    @Test
    public void save_image_decodable_string_to_preferences_singleton_on_image_response() {
        //nothing to prepare

        presenter.onImageResponse(ANY_NAME);

        verify(preferencesSingleton).setPrefPictureLocation(ANY_NAME);
    }

    @Test
    public void open_home_fragment_on_image_response() {
        //nothing to prepare

        presenter.onImageResponse(ANY_NAME);

        verify(view).openHomeFragment();
    }

    @Test
    public void show_error_on_image_selection_cancelled() {
        // nothing to prepare

        presenter.onImageSelectionCancelled();

        verify(view).onError(R.string.error_image_selection_cancelled);
    }
}
