package control.exception;

import io.smallrye.graphql.api.ErrorCode;

@ErrorCode("ALREADY_EXISTING")
    public class AlreadyExistingException extends RuntimeException {
        public AlreadyExistingException(String id) {
            super("Relation: " + id + " was found.");
        }
    }