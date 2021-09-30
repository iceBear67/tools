package io.ib67.util.loop;

import java.util.Optional;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface Loop {
    static Loop run(Function<Loop, Boolean> runnable, int period) {
        LoopImpl loop = new LoopImpl(runnable, period);
        LoopImpl.threadPool.execute(loop::run);
        return loop;
    }

    static Loop runAlways(Runnable runnable, int period) {
        return run(loop -> {
            runnable.run();
            return true;
        }, period);
    }

    static <T> Loop forQueue(Queue<T> queue, BiConsumer<Loop, T> consumer, int period) {
        return run(loop -> {
            Optional.ofNullable(queue.poll()).ifPresent(t -> consumer.accept(loop, t));
            return true;
        }, period);
    }

    void cancel();


}
