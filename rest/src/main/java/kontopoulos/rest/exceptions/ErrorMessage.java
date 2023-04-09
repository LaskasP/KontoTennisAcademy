package kontopoulos.rest.exceptions;

import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    private List<String> messages;
    private String description;

    public int getStatusCode() {
        return statusCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public List<String> getMessages() {
        return messages;
    }

    public String getDescription() {
        return description;
    }
}