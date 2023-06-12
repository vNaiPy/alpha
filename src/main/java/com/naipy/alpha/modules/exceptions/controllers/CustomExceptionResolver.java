package com.naipy.alpha.modules.exceptions.controllers;

import com.naipy.alpha.modules.exceptions.services.DatabaseException;
import com.naipy.alpha.modules.exceptions.services.ResourceNotFoundException;
import com.naipy.alpha.modules.exceptions.services.StoreNotRegisteredException;
import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class CustomExceptionResolver extends DataFetcherExceptionResolverAdapter {
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof ResourceNotFoundException) {
            return graphqlErrorBuilder(ex, env, ErrorType.NOT_FOUND);
        }
        else if (ex instanceof StoreNotRegisteredException) {
            return graphqlErrorBuilder(ex, env, ErrorType.BAD_REQUEST);
        }
        else if (ex instanceof DatabaseException) {
            return graphqlErrorBuilder(ex, env, ErrorType.BAD_REQUEST);
        }
        else {
            return null;
        }
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
