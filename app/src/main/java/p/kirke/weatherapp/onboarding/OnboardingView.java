package p.kirke.weatherapp.onboarding;

import p.kirke.weatherapp.ErrorHandlingView;

public interface OnboardingView extends ErrorHandlingView {

    void openGallery();

    void openHomeFragment();
}
