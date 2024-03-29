package p.kirke.weatherapp.home;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import p.kirke.weatherapp.MainActivity;
import p.kirke.weatherapp.PermissionHandler;
import p.kirke.weatherapp.PreferencesSingleton;
import p.kirke.weatherapp.R;
import p.kirke.weatherapp.ThreadHandler;
import p.kirke.weatherapp.db.WeatherHistoryRepository;

public class HomeFragment extends Fragment implements HomeView {

    @BindView(R.id.user_avatar)
    ImageView userAvatar;
    @BindView(R.id.greeting)
    TextView greeting;
    @BindView(R.id.introduction_text)
    TextView introduction;
    @BindView(R.id.loading_bar)
    ProgressBar loadingBar;

    private HomePresenter presenter;
    private boolean resumingFromError = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        getPresenter().handleUserImage();
        return view;
    }

    private HomePresenter getPresenter() {
        if (presenter == null) {
            presenter = new HomePresenter(this, PreferencesSingleton.getSingletonInstance(getContext()),
                    new WeatherHistoryRepository(getContext()), new PermissionHandler(getActivity()),
                    new LocationHandler(getActivity()));
        }
        return presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!resumingFromError) {
            getPresenter().start(hasNetworkConnection());
        }
        resumingFromError = false;
    }

    private boolean hasNetworkConnection() {
        Activity activity = getActivity();
        if (activity != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        }
        return false;
    }

    @Override
    public void showName(String name) {
        greeting.setText(getString(R.string.greeting, name));
    }

    @Override
    public void showWeatherData(String subLocality, int actualTemperature, int feelableTemperature) {
        introduction.setText(getString(R.string.welcome_text, subLocality, actualTemperature, feelableTemperature));
    }

    @Override
    public void onError(int message) {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.showError(message);
            resumingFromError = true;
        }
    }

    @Override
    public void hideError() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.hideError();
        }
    }

    @Override
    public void showLoading(boolean showLoading) {
        introduction.setVisibility(showLoading ? View.GONE : View.VISIBLE);
        loadingBar.setVisibility(showLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void displayUserImage(String image) {
        ThreadHandler threadHandler = ThreadHandler.getInstance();
        threadHandler.runOnBackground(() -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(image);
                    threadHandler.runOnUi(() -> userAvatar.setImageBitmap(bitmap));
                }

        );
    }

    @Override
    public void showImagePlaceholder() {
        Activity activity = getActivity();
        if (activity != null) {
            userAvatar.setImageDrawable(getResources().getDrawable(R.drawable.ic_user, activity.getTheme()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        presenter.onPermissionResponse(requestCode, permissions, grantResults);
    }
}
