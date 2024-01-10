package dev.shulika.restapiexample.constant;

public interface ServiceConst {

    String BAD_CREDENTIALS = "Authentication Failure: Bad Credentials, check email/password";
    String ACCESS_DENIED = "Authorization Failure: Access denied, you dont have permission to access this resource";
    String PERSON_EXIST = "Person already exist! email: ";
    String PERSON_NOT_FOUND = "Person not found! id: ";
    String PERSON_NOT_FOUND_EMAIL = "Person not found! email: ";
    String INVALID_EMAIL_PASSWORD = "Invalid email or password";
    String COMMENT_NOT_FOUND = "Comment not found! id: ";
    String TASK_NOT_FOUND = "Task not found! id: ";

}
