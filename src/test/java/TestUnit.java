import io.ib67.util.Cooldown;
import io.ib67.util.Randomly;
import io.ib67.util.bukkit.Text;
import io.ib67.util.bukkit.Texts;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

public class TestUnit {
    @Test
    public void testCooldown() {
        long time = System.currentTimeMillis();
        Cooldown cd = Cooldown.of(Duration.ofMillis(100));
        cd.refresh();
        cd.waitFor();
        assert System.currentTimeMillis() - time >= 100;
    }

    public void onPlayerJoin(Player player) {

        String welcomeMessages = Text.from("config.joinMessages")
                .visit(Texts.placeholderApi(player))
                .trim()
                .visit(e -> e.equals("time") ? Instant.now().toString() : null)
                .join("blabla", "abababa")
                .placeholder("luckyPlayer", Randomly.pick(Bukkit.getOnlinePlayers()).map(Player::getName))
                .toString();
        player.sendMessage(welcomeMessages);

    }
}
