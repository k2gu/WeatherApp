package p.kirke.weatherapp.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.ButterKnife;
import p.kirke.weatherapp.R;
import p.kirke.weatherapp.db.WeatherHistoryRepository;
import p.kirke.weatherapp.db.WeatherHistory;

public class HistoryFragment extends Fragment implements HistoryCallback {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WeatherHistoryRepository repository = new WeatherHistoryRepository(getContext());
        //repository.insertNewInfo(new WeatherHistory("Tallinn", 0, 0));
        repository.getAllData(this);
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
}
