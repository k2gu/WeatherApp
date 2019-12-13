package p.kirke.weatherapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import p.kirke.weatherapp.history.HistoryFragment;
import p.kirke.weatherapp.home.HomeFragment;
import p.kirke.weatherapp.onboarding.OnBoardingFragment;
import p.kirke.weatherapp.util.Const;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fragment_container)
    public ViewPager viewPager;
    @BindView(R.id.tab_layout)
    public TabLayout tabLayout;
    //TODO Remove
    private OnBoardingFragment onboardingFragment = new OnBoardingFragment();
    private HomeFragment homeFragment = new HomeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        createFragmentViewPager();
    }

    private void createFragmentViewPager() {
        TabAdapter adapter = getTabAdapterWithFragments();
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private TabAdapter getTabAdapterWithFragments() {
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        boolean hasUserData = PreferencesSingleton.getSingletonInstance(getBaseContext()).hasUserData();
        adapter.addFragment(hasUserData ? homeFragment : onboardingFragment, getString(R.string.home_fragment_title));
        adapter.addFragment(new HistoryFragment(), getString(R.string.history_fragment_title));
        return adapter;
    }

    public void replaceOnboardingFragment() {
        TabAdapter adapter = (TabAdapter) viewPager.getAdapter();
        if (adapter != null) {
            adapter.replaceFragment(0, homeFragment, getString(R.string.home_fragment_title));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
         // TODO dont save fragments, ask from adapter
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Const.LOCATION_REQUEST_CODE == requestCode) {
            homeFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (Const.READ_EXTERNAL_STORAGE_REQUEST_CODE == requestCode) {
            onboardingFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        // TODO notify
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO
        onboardingFragment.onActivityResult(requestCode, resultCode, data);
    }
}
