package com.ubs.interview.rest.configuration;

import com.ubs.interview.rest.response.ErrorResponse;
import com.ubs.interview.service.exception.CsvReadException;
import com.ubs.interview.service.exception.DuplicateKeyException;
import com.ubs.interview.service.exception.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler({ IllegalArgumentException.class })
	protected ResponseEntity<ErrorResponse> badRequest(Exception exception) {
		return errorResponse(HttpStatus.BAD_REQUEST, exception);
	}

	@ExceptionHandler({ Exception.class })
	protected ResponseEntity<ErrorResponse> internalServerError(Exception exception) {
		return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception);
	}

	@ExceptionHandler({ ObjectNotFoundException.class })
	protected ResponseEntity<ErrorResponse> notFound(Exception exception) {
		return errorResponse(HttpStatus.NOT_FOUND, exception);
	}

	@ExceptionHandler({ DuplicateKeyException.class})
	protected ResponseEntity<ErrorResponse> conflict(Exception exception) {
		return errorResponse(HttpStatus.CONFLICT, exception);
	}

	@ExceptionHandler({ CsvReadException.class })
	protected ResponseEntity<ErrorResponse> unprocessableEntity(Exception exception) {
		return errorResponse(HttpStatus.UNPROCESSABLE_ENTITY, exception);
	}

	private ResponseEntity<ErrorResponse> errorResponse(HttpStatus status, Exception exception) {
		log.warn(status.name(), exception);
	    ErrorResponse errorResponse = ErrorResponse.builder().error(status.getReasonPhrase()).message(exception.getMessage()).build();
		return ResponseEntity.status(status).body(errorResponse);
	}
}