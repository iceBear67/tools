package io.ib67.util;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExceptionBottle {
    private static final Map<String, ExceptionBottle> modules = new HashMap<>();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'");
    private final List<Pair<Throwable, Pair<String, Long>>> problems = new ArrayList<>();
    private final String module;

    private ExceptionBottle(String name) {
        this.module = name;
    }

    public static ExceptionBottle getDefaultBottle() {
        return getByModule("GLOBAL");
    }

    public static ExceptionBottle getByModule(String name) {
        return modules.computeIfAbsent(name, ExceptionBottle::new);
    }

    public void collect(Throwable t, String message) {
        problems.add(Pair.of(t, Pair.of(message, System.currentTimeMillis())));
    }

    public void warn(String message) {
        collect(new WarnMessage(message), "");
    }

    public void collect(Throwable t) {
        collect(t, "Nope");
    }

    public String toString() {
        StringJoiner sj = new StringJoiner("\n");
        sj.add("== ExceptionBottle Output ==");
        sj.add("Module: " + module);
        sj.add("System: " + System.getProperty("os.name") + " Java: " + System.getProperty("java.version"));
        sj.add("Last Error: " + (problems.size() == 0 ? "None" : Functional.from(() -> {
            Pair<Throwable, Pair<String, Long>> p = problems.get(0);
            return p.key.toString() + " at " + sdf.format(p.value.value);
        })));
        sj.add("Report Time: " + sdf.format(new Date()));
        sj.add(" ");
        sj.add("== System Load ==");
        sj.add("System load average: " + ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage() + " CPUs: " + ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors());
        sj.add("Heap Memory: ");
        sj.add(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().toString());
        sj.add("Non-Heap Memory: ");
        sj.add(ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().toString());
        if (problems.size() != 0) {
            sj.add(" ");
            sj.add("== ERRORS ==");
            for (Pair<Throwable, Pair<String, Long>> problem : problems) {
                sj.add("Time: " + sdf.format(problem.value.value));
                if (!(problem.key instanceof WarnMessage)) {
                    sj.add("Self-Description by code: " + problem.value.key);
                }
                NestedPrintWriter wr = new NestedPrintWriter(System.err);
                problem.key.printStackTrace(wr);
                sj.add(wr.toString());
            }
        }
        return sj.toString();
    }

    public static class WarnMessage extends Throwable {
        public WarnMessage(String s) {
            super(s);
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }

        @Override
        public StackTraceElement[] getStackTrace() {
            return new StackTraceElement[0];
        }
    }

    private static class NestedPrintWriter extends PrintWriter {
        private StringJoiner joiner = new StringJoiner("\n");


        public NestedPrintWriter(OutputStream outputStream) {
            super(outputStream);
        }


        public NestedPrintWriter(File file, String s) throws FileNotFoundException, UnsupportedEncodingException {
            super(file, s);
        }

        @Override
        public void print(String s) {
            joiner.add(s);
        }

        @Override
        public String toString() {
            return joiner.toString();
        }
    }
}
