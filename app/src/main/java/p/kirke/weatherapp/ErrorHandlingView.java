package p.kirke.weatherapp;

public interface ErrorHandlingView {

    void onError(int message);

    void hideError();
}
