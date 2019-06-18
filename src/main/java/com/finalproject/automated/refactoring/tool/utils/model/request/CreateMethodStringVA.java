package com.finalproject.automated.refactoring.tool.utils.model.request;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 16 June 2019
 */

@Getter
@Builder
public class CreateMethodStringVA<T> {

    private T property;

    private Integer index;

    private Integer maxSize;

    private Boolean isRegex;
}
