package com.finalproject.automated.refactoring.tool.utils.service.implementation;

import com.finalproject.automated.refactoring.tool.utils.service.MergeListHelper;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 25 May 2019
 */

@Service
public class MergeListHelperImpl implements MergeListHelper {

    private static final Integer FIRST_INDEX = 0;
    private static final Integer SECOND_INDEX = 1;

    @Override
    public void mergeListOfString(@NonNull List<String> listToMerge, @NonNull List<Integer> mergeIndex,
                                  @NonNull String delimiter) {
        Integer maxSize = mergeIndex.size() - SECOND_INDEX;

        for (Integer index = FIRST_INDEX; index < maxSize; index++) {
            Integer startPoint = mergeIndex.get(index);
            Integer endPoint = mergeIndex.get(++index);

            listToMerge.set(startPoint, String.join(delimiter, listToMerge.subList(startPoint, ++endPoint)));
        }

        for (Integer index = maxSize; index > FIRST_INDEX; index--) {
            Integer endPoint = mergeIndex.get(index);
            Integer startPoint = mergeIndex.get(--index) + SECOND_INDEX;

            for (Integer count = startPoint; count <= endPoint; count++) {
                listToMerge.remove(startPoint.intValue());
            }
        }
    }
}
