import io.ib67.util.Cooldown;
import org.junit.jupiter.api.Test;

public class TestUnit {
    @Test
    public void testCooldown(){
        long time = System.currentTimeMillis();
        Cooldown cd = Cooldown.of(1000);
        cd.refresh();
        cd.waitFor();
        assert System.currentTimeMillis() - time >=1000;
    }
}
