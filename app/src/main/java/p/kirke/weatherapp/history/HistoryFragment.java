package p.kirke.weatherapp.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import p.kirke.weatherapp.MainActivity;
import p.kirke.weatherapp.R;
import p.kirke.weatherapp.db.WeatherHistory;
import p.kirke.weatherapp.db.WeatherHistoryRepository;

public class HistoryFragment extends Fragment implements HistoryView {

    @BindView(R.id.no_info_yet)
    TextView noInfoYet;
    @BindView(R.id.history_list)
    RecyclerView recyclerView;

    private WeatherHistoryRepository repository;
    private HistoryPresenter presenter;

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
        if (presenter == null) {
            presenter = new HistoryPresenter(this, repository);
        }
        presenter.start();
    }

    @Override
    public void showList(List<WeatherHistory> historyList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new HistoryAdapter(historyList));
    }

    @Override
    public void showNoInfoYetMessage() {
        noInfoYet.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoInfoYetMessage() {
        noInfoYet.setVisibility(View.GONE);
    }

    @Override
    public void onError(int message) {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.showError(message);
        }
    }

    @Override
    public void hideError() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.hideError();
        }
    }
}
