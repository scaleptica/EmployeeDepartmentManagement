package org.paytm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class RecordAlreadyExistsException extends RuntimeException {

  public RecordAlreadyExistsException(String errorMessage) {
    super(errorMessage);
  }
}
