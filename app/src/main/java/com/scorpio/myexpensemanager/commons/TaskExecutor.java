package com.scorpio.myexpensemanager.commons;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Task executor that execute a tasks in background
 * Created by User on 27-02-2018.
 */

public class TaskExecutor implements ExecutorService {
    private ExecutorService executor;

    public TaskExecutor() {
        executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        executor.execute(runnable);
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }

    @NonNull
    @Override
    public List<Runnable> shutdownNow() {
        return executor.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executor.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executor.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, @NonNull TimeUnit unit) throws
            InterruptedException {
        return false;
    }

    @NonNull
    @Override
    public <T> Future<T> submit(@NonNull Callable<T> task) {
        return executor.submit(task);
    }

    @NonNull
    @Override
    public <T> Future<T> submit(@NonNull Runnable task, T result) {
        return executor.submit(task, result);
    }

    @NonNull
    @Override
    public Future<?> submit(@NonNull Runnable task) {
        return executor.submit(task);
    }

    @NonNull
    @Override
    public <T> List<Future<T>> invokeAll(@NonNull Collection<? extends Callable<T>> tasks) throws
            InterruptedException {
        return executor.invokeAll(tasks);
    }

    @NonNull
    @Override
    public <T> List<Future<T>> invokeAll(@NonNull Collection<? extends Callable<T>> tasks, long
            timeout, @NonNull TimeUnit unit) throws InterruptedException {
        return executor.invokeAll(tasks, timeout, unit);
    }

    @NonNull
    @Override
    public <T> T invokeAny(@NonNull Collection<? extends Callable<T>> tasks) throws
            InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public <T> T invokeAny(@NonNull Collection<? extends Callable<T>> tasks, long timeout,
                           @NonNull TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException {
        return null;
    }
}
