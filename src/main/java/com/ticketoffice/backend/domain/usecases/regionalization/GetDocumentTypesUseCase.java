package com.ticketoffice.backend.domain.usecases.regionalization;

import com.ticketoffice.backend.domain.models.DocumentType;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface GetDocumentTypesUseCase extends Function<String, List<DocumentType>> {
}
