package p.kirke.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import p.kirke.weatherapp.customviews.LockableViewPager;
import p.kirke.weatherapp.home.HomeFragment;
import p.kirke.weatherapp.onboarding.OnBoardingFragment;
import p.kirke.weatherapp.pushnotification.NotificationHandler;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fragment_container)
    public LockableViewPager viewPager;
    @BindView(R.id.tab_layout)
    public TabLayout tabLayout;
    @BindView(R.id.error_container)
    ConstraintLayout errorContainer;
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
        NotificationHandler.getInstance().scheduleNotification(this);
    }

    private void createFragmentViewPager() {
        TabAdapter adapter = getTabAdapterWithFragments();
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private TabAdapter getTabAdapterWithFragments() {
        boolean hasUserData = PreferencesSingleton.getSingletonInstance(getBaseContext()).hasUserData();
        tabLayout.setVisibility(hasUserData ? View.VISIBLE : View.GONE);
        viewPager.setSwipeable(hasUserData);
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
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setSwipeable(true);
        }
    }

    public void showError(int message) {
        errorContainer.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
    }

    public void hideError() {
        errorContainer.setVisibility(View.GONE);
    }

    @OnClick(R.id.button_close)
    public void onClickErrorClose() {
        errorContainer.setVisibility(View.GONE);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        ThreadHandler.getInstance().destroy();
    }
}
