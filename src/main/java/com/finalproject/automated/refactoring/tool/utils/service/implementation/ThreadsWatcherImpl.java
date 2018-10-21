package com.finalproject.automated.refactoring.tool.utils.service.implementation;

import com.finalproject.automated.refactoring.tool.utils.service.ThreadsWatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author fazazulfikapp
 * @version 1.0.0
 * @since 21 October 2018
 */

@Service
public class ThreadsWatcherImpl implements ThreadsWatcher {

    @Override
    public void waitAllThreadsDone(List<Future> futures, Integer waitingTime) {
        while (true) {
            Long finishedThreads = getFinishedThreadsCount(futures);

            if (isAllFinished(finishedThreads, futures.size()))
                break;

            waitForThreads(waitingTime);
        }
    }

    private Long getFinishedThreadsCount(List<Future> futures) {
        return futures.stream()
                .filter(Future::isDone)
                .count();
    }

    private Boolean isAllFinished(Long finishedThreads, Integer numberOfThreads) {
        return finishedThreads.equals(numberOfThreads.longValue());
    }

    private void waitForThreads(Integer waitingTime) {
        try {
            Thread.sleep(waitingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
