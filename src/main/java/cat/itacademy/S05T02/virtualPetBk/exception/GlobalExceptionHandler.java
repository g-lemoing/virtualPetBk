package cat.itacademy.S05T02.virtualPetBk.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.sql.SQLDataException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    ProblemDetail problemDetail = null;

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException(BadCredentialsException e){
        problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), e.getMessage());
        problemDetail.setProperty("description", "The username or password is incorrect");
        return problemDetail;
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ProblemDetail handleBadCredentialsException(ExpiredJwtException e){
        problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), e.getMessage());
        problemDetail.setProperty("description", "The JWT token has expired");
        return problemDetail;
    }

    @ExceptionHandler(SignatureException.class)
    public ProblemDetail handleBadCredentialsException(SignatureException e){
        problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), e.getMessage());
        problemDetail.setProperty("description", "The JWT signature is invalid");
        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleBadCredentialsException(AccessDeniedException e){
        problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), e.getMessage());
        problemDetail.setProperty("description", "Not authorized to access this resource");
        return problemDetail;
    }

    @ExceptionHandler(UserNameAlreadyExistsException.class)
    public ProblemDetail handleUserNameExistsInDatabaseException(UserNameAlreadyExistsException e){
        problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(409), e.getMessage());
        problemDetail.setProperty("description", "User already exists, please use another user name.");
        return problemDetail;
    }

    @ExceptionHandler(PetNameAlreadyExistsException.class)
    public ProblemDetail handlePetNameExistsInDatabaseException(PetNameAlreadyExistsException e){
        problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(409), e.getMessage());
        problemDetail.setProperty("description", "Pet name already exists for this user, please choose another name.");
        return problemDetail;
    }

    @ExceptionHandler(PetNotFoundException.class)
    public ProblemDetail handlePetNotFoundException(PetNotFoundException e){
        problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), e.getMessage());
        problemDetail.setProperty("description", "Pet not found in database.");
        return problemDetail;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFoundException(UserNotFoundException e){
        problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), e.getMessage());
        problemDetail.setProperty("description", "User not found in database.");
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException e){
        problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage());
        problemDetail.setProperty("description", "The request contains bad arguments.");
        return problemDetail;
    }

    @ExceptionHandler(SQLDataException.class)
    public ProblemDetail handleSQLDataException(SQLDataException e){
        problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), e.getMessage());
        problemDetail.setProperty("description", "A problem happened when reading / writing  from / to the database.");
        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException e){
        problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage());
        problemDetail.setProperty("description", "A data integrity issue occurred with data provided.");
        return problemDetail;
    }
}
