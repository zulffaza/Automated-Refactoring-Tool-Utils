package com.finalproject.automated.refactoring.tool.utils.service.implementation;

import com.finalproject.automated.refactoring.tool.utils.service.MergeListHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 25 May 2019
 */

public class MergeListHelperImplTest {

    private MergeListHelper mergeListHelper;

    @Before
    public void setUp() {
        mergeListHelper = new MergeListHelperImpl();
    }

    @Test
    public void mergeListOfString_success() {
        List<String> listToMerge = createListToMerge();
        List<Integer> mergeIndex = createMergeIndex();

        String delimiter = "";

        mergeListHelper.mergeListOfString(listToMerge, mergeIndex, delimiter);

        assertEquals(createListToMergeExpectations(), listToMerge);
    }

    @Test
    public void mergeListOfString_success_listAndIndexEmpty() {
        List<String> listToMerge = new ArrayList<>();
        List<Integer> mergeIndex = new ArrayList<>();

        String delimiter = "";

        mergeListHelper.mergeListOfString(listToMerge, mergeIndex, delimiter);

        assertEquals(new ArrayList<>(), listToMerge);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void mergeListOfString_failed_indexNotFound() {
        List<String> listToMerge = new ArrayList<>();
        List<Integer> mergeIndex = createMergeIndex();

        String delimiter = "";

        mergeListHelper.mergeListOfString(listToMerge, mergeIndex, delimiter);
    }

    @Test(expected = NullPointerException.class)
    public void mergeListOfString_failed_listIsNull() {
        List<String> listToMerge = null;
        List<Integer> mergeIndex = new ArrayList<>();

        String delimiter = "";

        mergeListHelper.mergeListOfString(listToMerge, mergeIndex, delimiter);
    }

    @Test(expected = NullPointerException.class)
    public void mergeListOfString_failed_indexIsNull() {
        List<String> listToMerge = new ArrayList<>();
        List<Integer> mergeIndex = null;

        String delimiter = "";

        mergeListHelper.mergeListOfString(listToMerge, mergeIndex, delimiter);
    }

    @Test(expected = NullPointerException.class)
    public void mergeListOfString_failed_delimiterIsNull() {
        List<String> listToMerge = new ArrayList<>();
        List<Integer> mergeIndex = new ArrayList<>();

        String delimiter = null;

        mergeListHelper.mergeListOfString(listToMerge, mergeIndex, delimiter);
    }

    private List<String> createListToMerge() {
        List<String> listToMerge = Arrays.asList("Hello", "World", "Merge", "List", "Impl", "Test");
        return new ArrayList<>(listToMerge);
    }

    private List<Integer> createMergeIndex() {
        List<Integer> mergeIndex = Arrays.asList(0, 1, 2, 5);
        return new ArrayList<>(mergeIndex);
    }

    private List<String> createListToMergeExpectations() {
        List<String> listToMerge = Arrays.asList("HelloWorld", "MergeListImplTest");
        return new ArrayList<>(listToMerge);
    }
}