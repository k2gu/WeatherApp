package p.kirke.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import p.kirke.weatherapp.home.HomeFragment;
import p.kirke.weatherapp.onboarding.OnBoardingFragment;
import p.kirke.weatherapp.pushnotification.NotificationHandler;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fragment_container)
    public ViewPager viewPager;
    @BindView(R.id.tab_layout)
    public TabLayout tabLayout;
    @BindView(R.id.error_message)
    TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        createFragmentViewPager();
        setUpPushNotification();
    }

    private void setUpPushNotification() {
        NotificationHandler notification = new NotificationHandler(this);
        notification.scheduleNotification();
    }

    private void createFragmentViewPager() {
        TabAdapter adapter = getTabAdapterWithFragments();
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private TabAdapter getTabAdapterWithFragments() {
        boolean hasUserData = PreferencesSingleton.getSingletonInstance(getBaseContext()).hasUserData();
        return new TabAdapter(getSupportFragmentManager(), hasUserData, getTabTitles());
    }

    private String[] getTabTitles() {
        return new String[]{
                getString(R.string.home_fragment_title),
                getString(R.string.history_fragment_title)
        };
    }

    public void replaceOnboardingFragment() {
        TabAdapter adapter = (TabAdapter) viewPager.getAdapter();
        if (adapter != null) {
            adapter.replaceFragmentOnboarding();
        }
    }

    public void showError(int message) {
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
    }

    public void hideError() {
        errorMessage.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment fragment = getAliveFragmentInPosition0();
        if (fragment instanceof HomeFragment || fragment instanceof OnBoardingFragment) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private Fragment getAliveFragmentInPosition0() {
        TabAdapter adapter = (TabAdapter) viewPager.getAdapter();
        return adapter != null ? adapter.getFragment() : null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getAliveFragmentInPosition0();
        if (fragment instanceof OnBoardingFragment) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
