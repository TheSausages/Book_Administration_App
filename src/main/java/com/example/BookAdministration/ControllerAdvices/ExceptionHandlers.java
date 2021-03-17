package com.example.BookAdministration.ControllerAdvices;

import com.example.BookAdministration.Exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionHandlers {
    private final Logger logger = LoggerFactory.getLogger(ExceptionHandlers.class);

    @ExceptionHandler(PasswordsNotMatchingException.class)
    public String PasswordsNotMatchingHandler(HttpServletRequest request, RedirectAttributes redirectAttributes, PasswordsNotMatchingException e) {
        logger.info("Registration Failed: Passwords did not match");

        redirectAttributes.addFlashAttribute("Exception", true);
        redirectAttributes.addFlashAttribute("exceptionMessage", e.getMessage());

        return "redirect:/registration";
    }

    @ExceptionHandler(PasswordLengthException.class)
    public String PasswordExceptionHandler(HttpServletRequest request, RedirectAttributes redirectAttributes, PasswordLengthException e) {
        logger.info("Registration Failed: Passwords is too Long!");

        redirectAttributes.addFlashAttribute("Exception", true);
        redirectAttributes.addFlashAttribute("exceptionMessage", e.getMessage());

        return "redirect:/registration";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String ConstraintViolationHandler(HttpServletRequest request, RedirectAttributes redirectAttributes, ConstraintViolationException e) {
        logger.info("Registration failed: Username or Password constraint was violated!");

        redirectAttributes.addFlashAttribute("Exception", true);
        redirectAttributes.addFlashAttribute("exceptionMessage", e.getMessage());

        return "redirect:/registration";
    }

    @ExceptionHandler(EntityHasChildrenException.class)
    public String EntityHasChildrenHandler(HttpServletRequest request, RedirectAttributes redirectAttributes, EntityHasChildrenException e) {
        logger.info("Operation ended with failure: the object has children!");

        String whichController = request.getRequestURI().split("/")[1];

        redirectAttributes.addFlashAttribute("Exception", true);
        redirectAttributes.addFlashAttribute("exceptionMessage", e.getMessage());

        switch (whichController) {
            case "authors":
                return "redirect:/authors/list";
            case "publishers":
                return "redirect:/publishers/list";
            default:
                return "redirect:/home";
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String EntityNotFoundHandler(HttpServletRequest request, RedirectAttributes redirectAttributes, EntityNotFoundException e) {
        logger.info("Operation ended with failure: the object could not be found!");

        String whichController = request.getRequestURI().split("/")[1];

        redirectAttributes.addFlashAttribute("Exception", true);
        redirectAttributes.addFlashAttribute("exceptionMessage", e.getMessage());

        switch (whichController) {
            case "authors":
                return "redirect:/authors/list";
            case "publishers":
                return "redirect:/publishers/list";
            case "books":
                return "redirect:/books/catalog";
            default:
                return "redirect:/home";
        }
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    public String EntityAlreadyExistHandler(HttpServletRequest request, RedirectAttributes redirectAttributes, EntityAlreadyExistException e) {
        logger.info("Operation ended with failure: the object already exists");

        String whichController = request.getRequestURI().split("/")[1];
        String whichOperation = request.getRequestURI().split("/")[2];

        redirectAttributes.addFlashAttribute("Exception", true);
        redirectAttributes.addFlashAttribute("exceptionMessage", e.getMessage());

        if (whichController.equals("registration")) {
            return "redirect:/registration";
        }

        if (whichController.equals("authors") || whichController.equals("publishers") || whichController.equals("books")) {
            if (whichOperation.equals("edit"))  {
                String whichId = request.getRequestURI().split("/")[3];

                return "redirect:/" + whichController + "/" + whichOperation + "/" + whichId;
            }

            if (whichOperation.equals("new")) {
                if (whichController.equals("books")) {
                    return "redirect:/" + whichController + "/" + whichOperation;
                }

                String whichView = request.getRequestURI().split("/")[4];

                return "redirect:/" + whichController + "/" + whichOperation + "/" + whichView;
            }
        }

        return "redirect:/home";
    }
}
