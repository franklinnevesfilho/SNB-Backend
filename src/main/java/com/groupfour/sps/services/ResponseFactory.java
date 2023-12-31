package com.groupfour.sps.services;

import com.groupfour.sps.models.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Franklin Neves Filho
 */
@Service
public class ResponseFactory {

    /**
     * @param response
     * @return An instance of ResponseEntity<Response> with HttpStatus.OK
     */
    public ResponseEntity<Response> generateOkResponse(Response response){
        return genericResponse(response, HttpStatus.OK);
    }

    /**
     *
     * @return An instance of ResponseEntity<Response> with HttpStatus.BAD_REQUEST
     */
    public ResponseEntity<Response> generateBadResponse(){
        return genericResponse(null, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param errors A list of Strings representing errors that occurred
     * @return An instance of ResponseEntity<Response> with HttpStatus.BAD_REQUEST
     */
    public ResponseEntity<Response> generateBadResponse(List<String> errors){
        Response response = Response.builder().errors(errors).build();
        return genericResponse(response, HttpStatus.BAD_REQUEST);
    }

    /**
     *
     * @return An instance of ResponseEntity<Response> with HttpStatus.NO_CONTENT
     */
    public ResponseEntity<Response> generateNoContentResponse() {
        return genericResponse(null, HttpStatus.NO_CONTENT);
    }

    /**
     *
     * @param payload the optional object returning
     * @param status the http status of the response
     * @return An instance of ResponseEntity<Response>
     */
    private ResponseEntity<Response> genericResponse(Response payload, HttpStatus status){
        Response response = Response.builder().build();
        if(payload != null){
            response = payload;
        }
        return new ResponseEntity<>(response, status);
    }
}
