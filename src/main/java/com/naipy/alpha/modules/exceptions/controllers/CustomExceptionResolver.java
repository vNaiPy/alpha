package com.naipy.alpha.modules.exceptions.controllers;

import com.naipy.alpha.modules.exceptions.enums.ErrorAppType;
import com.naipy.alpha.modules.exceptions.services.ErrorManager;
import com.naipy.alpha.modules.exceptions.services.*;
import com.naipy.alpha.modules.user.exceptions.UserCannotBeAuthenticateException;
import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class CustomExceptionResolver extends DataFetcherExceptionResolverAdapter {

    private final ErrorManager errorManager;

    @Autowired
    public CustomExceptionResolver(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof ResourceNotFoundException) {
            errorManager.storeTheError(ex, ErrorAppType.RESOURCE_NOT_FOUND);
            return graphqlErrorBuilder(ex, env, ErrorType.NOT_FOUND);
        }
        else if (ex instanceof UserCannotBeAuthenticateException) {
            errorManager.storeTheError(ex, ErrorAppType.USER_UNAUTHORIZED);
            return graphqlErrorBuilder(ex, env, ErrorType.UNAUTHORIZED);
        }
        else if (ex instanceof StoreNotRegisteredException) {
            errorManager.storeTheError(ex, ErrorAppType.STORE_NOT_REGISTERED);
            return graphqlErrorBuilder(ex, env, ErrorType.BAD_REQUEST);
        }
        else if (ex instanceof DatabaseException) {
            errorManager.storeTheError(ex, ErrorAppType.DATABASE_ERROR);
            return graphqlErrorBuilder(ex, env, ErrorType.BAD_REQUEST);
        }
        else if (ex instanceof StoreAlreadyRegisteredException) {
            errorManager.storeTheError(ex, ErrorAppType.STORE_ALREADY_REGISTER);
            return graphqlErrorBuilder(ex, env, ErrorType.BAD_REQUEST);
        }
        else if (ex instanceof InvalidParameterException) {
            errorManager.storeTheError(ex, ErrorAppType.INVALID_PARAM);
            return graphqlErrorBuilder(ex, env, ErrorType.BAD_REQUEST);
        }
        else if (ex instanceof ExternalResponseNotReceivedException) {
            errorManager.storeTheError(ex, ErrorAppType.EXTERNAL_RESPONSE_NOT_RECEIVED);
            return graphqlErrorBuilder(ex, env, ErrorType.NOT_FOUND);
        }
        else if (ex instanceof GenericErrorException) {
            errorManager.storeTheError(ex, ErrorAppType.GENERIC_ERROR);
            return graphqlErrorBuilder(ex, env, ErrorType.INTERNAL_ERROR);
        }
        else
            return null;
    }

    private GraphQLError graphqlErrorBuilder (Throwable ex, DataFetchingEnvironment env, ErrorClassification errorType) {
        return GraphqlErrorBuilder.newError()
                .errorType(errorType)
                .message(ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }
}
