package p.kirke.weatherapp.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import p.kirke.weatherapp.R;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter == null) {
            presenter = new HomePresenter(this);
        }
        //TODO  a lot of ifs missing

        presenter.getData();
    }

    @Override
    public void showName(String name) {
        greeting.setText(getString(R.string.greeting, name));
    }

    @Override
    public void showWeatherData(String city, int actualTemperature, int feelableTemperature) {
        introduction.setText(getString(R.string.welcome_text, city, actualTemperature, feelableTemperature));
    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading(boolean showLoading) {
        introduction.setVisibility(showLoading ? View.GONE : View.VISIBLE);
        loadingBar.setVisibility(showLoading ? View.VISIBLE : View.GONE);
    }
}
