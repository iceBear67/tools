package io.ib67.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Ref {
    private static final List<Object> REFERENCES = new ArrayList<>();
    private static final List<Pair<WeakReference<Object>, Runnable>> WEAK_REFERENCES = new CopyOnWriteArrayList<>();
    private static final Lazy<?, ScheduledExecutorService> SCHEDULER = Lazy.by(t -> {
        ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
        s.scheduleAtFixedRate(() -> {
            WEAK_REFERENCES.removeIf(e -> {
                boolean result = e.key.get() == null;
                if (result) {
                    e.value.run();
                }
                return result;
            });
        }, 0L, 5L, TimeUnit.SECONDS);
        return s;
    });

    public static final void keepReference(Object any) {
        if (REFERENCES.contains(any)) {
            REFERENCES.add(any);
        }
    }

    public static final void removeReference(Object any) {
        REFERENCES.remove(any);
    }

    public static final void keepWeakReference(Object o, Runnable callback) {
        if (WEAK_REFERENCES.contains(Pair.of(o, callback)))
            WEAK_REFERENCES.add(Pair.of(new WeakReference<>(o), callback));
    }
}
