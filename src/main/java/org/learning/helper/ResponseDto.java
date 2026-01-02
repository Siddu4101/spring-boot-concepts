package org.learning.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/*use this record all arg constructor to customize the response*/
public record ResponseDto(HttpStatusCode statusCode, String status, String message) {
   /*to use canonical constructor which creates everytime new object with default success message not-recommented to
    use the below that also gives same behaviour with no new instance everytime*/
    public ResponseDto(){
        this(HttpStatus.OK, "Success", "All OK");
    }

    /*to use it as a single instance everywhere when no custom message needed*/
    public static final ResponseDto SUCCESS_RESPONSE = new ResponseDto();
}
