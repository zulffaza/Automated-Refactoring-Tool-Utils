package com.finalproject.automated.refactoring.tool.utils.service;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author fazazulfikapp
 * @version 1.0.0
 * @since 21 October 2018
 */

public interface ThreadsWatcher {

    void waitAllThreadsDone(List<Future> futures, Integer waitingTime);
}
