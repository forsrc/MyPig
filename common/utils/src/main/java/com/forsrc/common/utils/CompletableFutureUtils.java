package com.forsrc.common.utils;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public class CompletableFutureUtils {

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(1,
            getThreadFactory());

    public static <T> CompletableFuture<T> withTimeout(Duration duration) {
        final CompletableFuture<T> promise = new CompletableFuture<>();
        SCHEDULED_EXECUTOR_SERVICE.schedule(() -> {
            final TimeoutException ex = new TimeoutException("CompletableFutureUtils Timeout after " + duration);
            return promise.completeExceptionally(ex);
        }, duration.toMillis(), TimeUnit.MILLISECONDS);
        return promise;
    }

    private static ThreadFactory getThreadFactory() {
        final ThreadFactory backingThreadFactory = Executors.defaultThreadFactory();
        final AtomicLong count = new AtomicLong(0);
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = backingThreadFactory.newThread(runnable);
                thread.setName(String.format("CompletableFutureUtils-Timeout-%s", count.getAndIncrement()));
                thread.setDaemon(true);
                // thread.setPriority(priority);
                // thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
                return thread;
            }
        };
    }

    public static <T> CompletableFuture<T> withinTimeout(CompletableFuture<T> future, Duration duration) {
        final CompletableFuture<T> timeout = withTimeout(duration);
        return future.applyToEither(timeout, Function.identity());
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            final int index = i;
            new Thread() {
                public void run() {

                    CompletableFuture<Integer> completableFuture = CompletableFutureUtils
                            .withTimeout(Duration.ofMillis(3000));
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e1) {
                    }
                    completableFuture.complete(index);
                    try {
                        System.out.println(completableFuture.get());
                    } catch (InterruptedException e) {
                    } catch (ExecutionException e) {
                        System.out.println(e.getMessage() + "-->" + index);
                    }
                }

                ;
            }.start();


        }

        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        CompletableFuture<Void> future = withinTimeout(completableFuture, Duration.ofMillis(5000))
                .thenAccept(c -> System.out.println("$" + c))
                .exceptionally(e -> {
                    System.out.println("---> Timeout: " + e.getMessage());
                    return null;
                });
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
            }
            System.out.println("----------------------------");
            completableFuture.complete(999);
        }).start();

        try {
            completableFuture.get();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException -> " + e.getMessage());
        } catch (ExecutionException e) {
            System.out.println("ExecutionException -> " + e.getMessage());
        }
    }
}