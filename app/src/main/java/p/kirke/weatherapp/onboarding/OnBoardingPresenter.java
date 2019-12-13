package p.kirke.weatherapp.onboarding;

import p.kirke.weatherapp.PermissionHandler;
import p.kirke.weatherapp.PreferencesSingleton;

class OnBoardingPresenter {

    private OnboardingView view;
    private PreferencesSingleton preferencesSingleton;
    private PermissionHandler permissionHandler;

    OnBoardingPresenter(OnboardingView view, PreferencesSingleton preferencesSingleton, PermissionHandler permissionHandler) {
        this.view = view;
        this.preferencesSingleton = preferencesSingleton;
        this.permissionHandler = permissionHandler;
    }

    void start() {
        if (permissionHandler.hasReadExternalStoragePermission()) {
            view.openGallery();
        } else {
            permissionHandler.requestReadExternalStoragePermission();
        }
    }

    void onRequestPermission(int requestCode, String[] permissions, int[] grantResults) {
        view.openGallery();
    }

    void onImageResponse(String imgDecodableString) {
        preferencesSingleton.setPrefPictureLocation(imgDecodableString);
        view.savePicture(imgDecodableString);
        view.openHomeFragment();
    }
}
