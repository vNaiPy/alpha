package com.naipy.alpha.modules.exceptions.services;

import com.naipy.alpha.modules.exceptions.enums.ErrorAppType;
import com.naipy.alpha.modules.exceptions.models.ErrorMessage;
import com.naipy.alpha.modules.exceptions.repository.ErrorRepository;
import com.naipy.alpha.modules.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ErrorManager extends ServiceUtils {

    private final ErrorRepository errorRepository;

    @Autowired
    public ErrorManager(ErrorRepository errorRepository) {
        this.errorRepository = errorRepository;
    }

    public void storeTheError (Throwable e, ErrorAppType errorAppType) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .id(generateUUID())
                .message(e.getMessage())
                .technicalError(e.getCause().getMessage())
                .lineNumberError(e.getStackTrace()[0].getLineNumber())
                .errorType(errorAppType)
                .moment(Instant.now()).build();
        errorRepository.save(errorMessage);
    }
}
