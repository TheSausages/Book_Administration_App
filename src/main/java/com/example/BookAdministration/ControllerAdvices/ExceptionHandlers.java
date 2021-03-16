package com.example.BookAdministration.ControllerAdvices;

import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Exceptions.EntityHasChildrenException;
import com.example.BookAdministration.Exceptions.EntityNotFoundException;
import com.example.BookAdministration.Exceptions.PasswordsNotMatchingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(PasswordsNotMatchingException.class)
    public String PasswordsNotMatchingHandler(HttpServletRequest request, RedirectAttributes redirectAttributes, PasswordsNotMatchingException e) {

        redirectAttributes.addFlashAttribute("Exception", true);
        redirectAttributes.addFlashAttribute("exceptionMessage", e.getMessage());

        return "redirect:/registration";
    }


    @ExceptionHandler(EntityHasChildrenException.class)
    public String EntityHasChildrenHandler(HttpServletRequest request, RedirectAttributes redirectAttributes, EntityHasChildrenException e) {
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
        String whichController = request.getRequestURI().split("/")[1];
        String whichOperation = request.getRequestURI().split("/")[2];

        System.out.println(request.getRequestURI());

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
