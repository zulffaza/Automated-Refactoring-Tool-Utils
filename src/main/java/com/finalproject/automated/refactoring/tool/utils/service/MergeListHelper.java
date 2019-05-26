package com.finalproject.automated.refactoring.tool.utils.service;

import lombok.NonNull;

import java.util.List;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 25 May 2019
 */

public interface MergeListHelper {

    void mergeListOfString(@NonNull List<String> listToMerge, @NonNull List<Integer> mergeIndex,
                           @NonNull String delimiter);
}
