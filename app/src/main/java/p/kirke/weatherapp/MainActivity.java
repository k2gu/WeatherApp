package p.kirke.weatherapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import p.kirke.weatherapp.history.HistoryFragment;
import p.kirke.weatherapp.home.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fragment_container)
    public ViewPager viewPager;
    @BindView(R.id.tab_layout)
    public TabLayout tabLayout;
    private LocationHandler locationHandler = new LocationHandler();
    //TODO Remove
    private HomeFragment fragment = new HomeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        locationHandler.requestPermission(this);
        createFragmentViewPager();
    }

    private void setUpPushNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 15);
        cal.add(Calendar.MINUTE, 28);
        cal.add(Calendar.SECOND, 0);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 24 * 60 * 60 * 1000, broadcast);
        }
    }

    private void createFragmentViewPager() {
        TabAdapter adapter = getTabAdapterWithFragments();
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private TabAdapter getTabAdapterWithFragments() {
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(fragment, getString(R.string.home_fragment_title));
        adapter.addFragment(new HistoryFragment(), getString(R.string.history_fragment_title));
        return adapter;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
