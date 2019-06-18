package com.finalproject.automated.refactoring.tool.utils.model.request;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 17 June 2019
 */

@Getter
@Builder
public class ReplaceFileVA {

    private String filePath;

    private String target;

    private String replacement;
}
