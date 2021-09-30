import io.ib67.util.Cooldown;
import io.ib67.util.loop.Loop;
import org.junit.jupiter.api.Test;

public class TestUnit {
    @Test
    public void testCooldown() {
        long time = System.currentTimeMillis();
        Cooldown cd = Cooldown.of(100);
        cd.refresh();
        cd.waitFor();
        assert System.currentTimeMillis() - time >= 100;
    }

    @Test
    public void testLoop() {
        Loop.run(loop -> {
            System.out.println(1);
            return true;
        }, 1);
    }
}
