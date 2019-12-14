package p.kirke.weatherapp.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.ButterKnife;
import p.kirke.weatherapp.MainActivity;
import p.kirke.weatherapp.R;
import p.kirke.weatherapp.db.WeatherHistory;
import p.kirke.weatherapp.db.WeatherHistoryRepository;

public class HistoryFragment extends Fragment implements HistoryCallback {

    private WeatherHistoryRepository repository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = new WeatherHistoryRepository(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (repository != null) {
            repository.getAllData(this);
        } else {
            onError();
        }
    }

    @Override
    public void onResponse(List<WeatherHistory> historyList) {
        //TODO
        RecyclerView recyclerView = ((RecyclerView) getView());
        if (recyclerView != null) {

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new HistoryAdapter(historyList));
        }
    }

    @Override
    public void onError() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.showError(R.string.error_generic);
        }
    }
}
