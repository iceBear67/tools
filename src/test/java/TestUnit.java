import io.ib67.util.Cooldown;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class TestUnit {
    @Test
    public void testCooldown() {
        long time = System.currentTimeMillis();
        Cooldown cd = Cooldown.of(Duration.ofMillis(100));
        cd.refresh();
        cd.waitFor();
        assert System.currentTimeMillis() - time >= 100;
    }
}
