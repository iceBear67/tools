package io.ib67.util.bukkit;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class BukkitText implements Text {
    private static final Pattern TEMPLATE_PLACEHOLDER = Pattern.compile("\\{\\{(.+)}}");
    private final List<UnaryOperator<String>> operators = new ArrayList<>();
    private StringBuilder builder;

    BukkitText(StringBuilder sb) {
        this.builder = sb;
    }

    BukkitText(CharSequence builder) {
        this.builder = new StringBuilder();
        join(builder);
    }

    BukkitText(Text text) {
        this.builder = new StringBuilder(text.toString());
    }

    @Override
    public Text stripAllColor() {
        // strip all.
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.stripColor(sb.toString()));
        builder = sb;
        return this;
    }

    @Override
    public Text stripColor(CharSequence string) {
        builder.append(ChatColor.stripColor(string.toString()));
        return this;
    }

    @Override
    public Text visit(UnaryOperator<String> placeholderOper) {
        operators.add(placeholderOper);
        return this;
    }

    @Override
    public Text map(String k, Object v) {
        visit(z -> k.equals(z) ? v.toString() : null);
        return this;
    }

    @Override
    public Text join(CharSequence sequence) {
        builder.append(ChatColor.RESET).append(ChatColor.translateAlternateColorCodes('&', sequence.toString())).append(ChatColor.RESET);

        return this;
    }

    @Override
    public Text reverse() {
        builder.reverse();
        return this;
    }

    @Override
    public Text trim() {
        builder = new StringBuilder(builder.toString().trim());
        return this;
    }

    @Override
    public Text map(UnaryOperator<String> mapper) {
        return new BukkitText(mapper.apply(builder.toString()));
    }

    private String setPlaceholders(String s) {
        Matcher matcher = TEMPLATE_PLACEHOLDER.matcher(s);
        return matcher.replaceAll(t ->
                operators.stream()
                        .map(operator -> operator.apply(t.group(1).trim()))
                        .filter(Objects::nonNull)
                        .findFirst().map(Text::colored).orElse(t.group(1))
                        + ChatColor.RESET);
    }

    public Text line(CharSequence s) {
        builder.append('\n');
        join(s);
        return this;
    }

    @Override
    public String toString() {
        if (operators.size() > 0) {
            return setPlaceholders(builder.toString());
        }
        return builder.toString();
    }
}
