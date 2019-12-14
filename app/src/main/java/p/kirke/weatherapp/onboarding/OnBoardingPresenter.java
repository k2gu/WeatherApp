package p.kirke.weatherapp.onboarding;

import p.kirke.weatherapp.PermissionHandler;
import p.kirke.weatherapp.PreferencesSingleton;
import p.kirke.weatherapp.R;

class OnBoardingPresenter {

    private OnboardingView view;
    private PreferencesSingleton preferencesSingleton;
    private PermissionHandler permissionHandler;

    OnBoardingPresenter(OnboardingView view, PreferencesSingleton preferencesSingleton, PermissionHandler permissionHandler) {
        this.view = view;
        this.preferencesSingleton = preferencesSingleton;
        this.permissionHandler = permissionHandler;
    }

    void start(String name) {
        if (!name.isEmpty()) {
            preferencesSingleton.setName(name.trim());
            getPermissionsAndOpenGallery();
        } else {
            view.onError(R.string.error_no_name_inserted);
        }
    }

    private void getPermissionsAndOpenGallery() {
        if (permissionHandler.hasReadExternalStoragePermission()) {
            view.openGallery();
        } else {
            permissionHandler.requestReadExternalStoragePermission();
        }
    }

    void onRequestPermissionResponse(boolean permissionGranted) {
        if (permissionGranted) {
            view.openGallery();
        } else {
            view.onError(R.string.error_denied_external_storage);
        }
    }

    void onImageResponse(String imgDecodableString) {
        preferencesSingleton.setPrefPictureLocation(imgDecodableString);
        view.savePicture(imgDecodableString);
        view.openHomeFragment();
    }

    void onImageSelectionCancelled() {
        view.onError(R.string.error_image_selection_cancelled);
    }
}
