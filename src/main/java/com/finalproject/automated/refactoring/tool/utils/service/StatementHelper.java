package com.finalproject.automated.refactoring.tool.utils.service;

import com.finalproject.automated.refactoring.tool.utils.model.request.IsStatementVA;
import lombok.NonNull;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 5 March 2019
 */

public interface StatementHelper {

    Boolean isStatement(@NonNull Character character, @NonNull IsStatementVA isStatementVA);
}
