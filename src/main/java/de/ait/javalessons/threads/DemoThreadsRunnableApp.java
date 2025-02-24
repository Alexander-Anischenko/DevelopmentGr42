package de.ait.javalessons.threads;

public class DemoThreadsRunnableApp {
    public static void main(String[] args) {
        DemoThreadRunnable demoThreadRunnable = new DemoThreadRunnable("Demo Thread - 1");
        demoThreadRunnable.start();

        DemoThreadRunnable demoThreadRunnable2 = new DemoThreadRunnable("Demo Thread - 2");
        demoThreadRunnable2.start();
    }
}
