package p.kirke.weatherapp;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadHandler {

    private static ThreadHandler instance;
    private Handler handler;
    private ExecutorService executorService;

    private ThreadHandler() {
        createExecutorService();
    }

    private void createExecutorService() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public static ThreadHandler getInstance() {
        if (instance == null) {
            instance = new ThreadHandler();
        }

        return instance;
    }

    public void runOnBackground(Runnable task) {
        if (executorService.isShutdown() || executorService.isTerminated()) {
            executorService.shutdownNow();
            createExecutorService();
        }
        executorService.submit(task);
    }

    void destroy() {
        executorService.shutdown();
        instance = null;
    }

    public void runOnUi(Runnable task) {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        handler.post(task);
    }
}
