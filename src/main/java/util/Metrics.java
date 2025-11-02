package util;

public interface Metrics {
    void startTimer();
    void stopTimer();
    long getElapsedTime();

    void increment(String counter);
    void reset();

    void printSummary();
}
