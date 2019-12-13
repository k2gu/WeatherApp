package p.kirke.weatherapp.onboarding;

import p.kirke.weatherapp.PermissionHandler;
import p.kirke.weatherapp.PreferencesSingleton;

public class OnboardingPresenter {

    private OnboardingView view;
    private PreferencesSingleton preferencesSingleton;
    private PermissionHandler permissionHandler;

    OnboardingPresenter(OnboardingView view, PreferencesSingleton preferencesSingleton, PermissionHandler permissionHandler) {
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
}
