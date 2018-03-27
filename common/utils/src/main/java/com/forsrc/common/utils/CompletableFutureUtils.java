package com.forsrc.common.utils;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

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

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            final int index = i;
            new Thread() {
                public void run() {

                    CompletableFuture<Integer> completableFuture = CompletableFutureUtils
                            .withTimeout(Duration.ofMillis(3000));
//                    try {
//                        TimeUnit.SECONDS.sleep(1);
//                    } catch (InterruptedException e1) {
//                    }
                    completableFuture.complete(index);
                    try {
                        System.out.println(completableFuture.get());
                    } catch (InterruptedException e) {
                    } catch (ExecutionException e) {
                        System.out.println("-->" + index);
                    }
                };
            }.start();
        }
    }
}
