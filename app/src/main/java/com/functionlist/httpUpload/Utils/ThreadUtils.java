package com.functionlist.httpUpload.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yan on 2018/1/9.
 */

public class ThreadUtils {
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    public static void execute(Runnable runnable) {
        executorService.execute(runnable);
    }
}
