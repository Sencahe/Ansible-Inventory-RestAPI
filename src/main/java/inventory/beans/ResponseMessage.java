package inventory.beans;

import lombok.Data;


@Data
public class ResponseMessage {
    
    public static final String NOT_FOUND_BY_ID = "The entity could not be found by the provided id on the request.";
    public static final String DELETED_OK = "The record has been successfully deleted";
    
    private String message;

    public ResponseMessage() {
    }
    
    public ResponseMessage(String message) {
        this.message = message;
    }
    
    
}
