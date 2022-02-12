package io.ib67.util;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Randomly {
    private Randomly() {

    }

    @SuppressWarnings("unchecked")
    public static <T> T pickOrNull(Collection<T> t) {
        if (t.size() == 0) return null;
        return (T) t.toArray()[ThreadLocalRandom.current().nextInt(t.size())];
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> pick(Collection<T> t) {
        return (Optional<T>) Optional.ofNullable(t.toArray()[ThreadLocalRandom.current().nextInt(t.size())]);
    }
}
