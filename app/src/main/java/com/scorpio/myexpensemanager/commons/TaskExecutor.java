package com.scorpio.myexpensemanager.commons;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Task executor that execute a tasks in background
 * Created by User on 27-02-2018.
 */

public class TaskExecutor implements Executor {
    private final Executor executor;

    public TaskExecutor() {
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@NonNull Runnable runnable) {

    }
}
