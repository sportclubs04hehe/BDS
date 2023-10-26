package HTQ.BDS.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException{
    private final HttpStatus httpStatus;

    public AppException(HttpStatus status, String message){
        super(message);
        this.httpStatus = status;
    }
}
