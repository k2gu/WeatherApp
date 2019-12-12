package p.kirke.weatherapp.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import p.kirke.weatherapp.R;
import p.kirke.weatherapp.db.WeatherHistory;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<WeatherHistory> dataList;

    HistoryAdapter(List<WeatherHistory> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View binding = inflater.inflate(R.layout.history_adapter_item, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bindView();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.location)
        TextView location;
        @BindView(R.id.temperature)
        TextView temperature;
        @BindView(R.id.feelable_temperature)
        TextView feelableTemperature;

        ViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bindView() {
            WeatherHistory currentItem = dataList.get(getAdapterPosition());

            date.setText(currentItem.date);
            location.setText(currentItem.city);
            temperature.setText(getTemperatureWithUnit(currentItem.temperature));
            feelableTemperature.setText(getTemperatureWithUnit(currentItem.feelableTemperature));
        }

        private String getTemperatureWithUnit(int temperature) {
            return itemView.getContext().getString(R.string.temperature_format, temperature);
        }
    }
}

