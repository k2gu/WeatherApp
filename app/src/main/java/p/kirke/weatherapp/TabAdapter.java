package p.kirke.weatherapp;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import p.kirke.weatherapp.history.HistoryFragment;
import p.kirke.weatherapp.home.HomeFragment;
import p.kirke.weatherapp.onboarding.OnBoardingFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] fragmentTitles;
    private boolean hasUserInfo;
    private SparseArray<Fragment> aliveFragments = new SparseArray<>();

    TabAdapter(FragmentManager fragmentManager, boolean hasUserInfo, String[] fragmentTitles) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.hasUserInfo = hasUserInfo;
        this.fragmentTitles = fragmentTitles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return hasUserInfo ? new HomeFragment() : new OnBoardingFragment();
            case 1:
                return new HistoryFragment();
        }
        return new Fragment();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (aliveFragments.get(0) instanceof OnBoardingFragment && hasUserInfo) {
            return POSITION_NONE;
        } else {
            return POSITION_UNCHANGED;
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup viewGroup, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(viewGroup, position);
        aliveFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup viewGroup, int position, @NonNull Object object) {
        aliveFragments.remove(position);
        super.destroyItem(viewGroup, position, object);
    }

    Fragment getFragment() {
        return aliveFragments.size() > 0 ? aliveFragments.get(0) : null;
    }

    void replaceFragmentOnboarding() {
        hasUserInfo = true;
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles[position];
    }

    @Override
    public int getCount() {
        return 2;
    }
}
