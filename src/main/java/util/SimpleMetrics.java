package util;

import java.util.HashMap;
import java.util.Map;

public class SimpleMetrics implements Metrics {
    private final Map<String, Long> counters = new HashMap<>();
    private long startTime;
    private long elapsedTime;

    @Override
    public void startTimer() {
        startTime = System.nanoTime();
    }

    @Override
    public void stopTimer() {
        elapsedTime = System.nanoTime() - startTime;
    }

    @Override
    public long getElapsedTime() {
        return elapsedTime;
    }

    @Override
    public void increment(String counter) {
        counters.put(counter, counters.getOrDefault(counter, 0L) + 1);
    }

    @Override
    public void reset() {
        counters.clear();
        elapsedTime = 0;
    }

    @Override
    public void printSummary() {
        System.out.println("----- Metrics Summary -----");
        for (Map.Entry<String, Long> entry : counters.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.printf("Time (ms): %.4f%n", elapsedTime / 1_000_000.0);
        System.out.println("---------------------------");
    }
}
