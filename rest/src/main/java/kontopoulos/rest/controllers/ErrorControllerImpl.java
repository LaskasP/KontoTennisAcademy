package kontopoulos.rest.controllers;

import kontopoulos.rest.exceptions.ResourceNotFoundException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorControllerImpl implements ErrorController {

    public static final String PATH_NOT_FOUND = "PATH NOT FOUND";
    public static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public void resourceNotFound() throws ResourceNotFoundException {
        throw new ResourceNotFoundException(PATH_NOT_FOUND);
    }
}