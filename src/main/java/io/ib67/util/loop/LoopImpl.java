package io.ib67.util.loop;

import io.ib67.Util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;

final class LoopImpl implements Loop {
    protected static final Executor threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
    private volatile boolean shouldRun = true;
    private int period;
    private Function<Loop, Boolean> runnable;

    public LoopImpl(Function<Loop, Boolean> runnable, int period) {
        this.runnable = runnable;
        this.period = period;
    }

    @Override
    public void cancel() {
        this.shouldRun = false;
    }

    public void run() {
        while (shouldRun) {
            shouldRun = runnable.apply(this);
            Util.runCatching(() -> {
                Thread.sleep(period);
                return null;
            });
        }
    }
}
