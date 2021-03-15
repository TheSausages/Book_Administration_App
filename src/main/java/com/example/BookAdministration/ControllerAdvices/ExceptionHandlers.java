package com.example.BookAdministration.ControllerAdvices;

import com.example.BookAdministration.Exceptions.EntityHasChildrenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(EntityHasChildrenException.class)
    public String EntityHasChildrenHandler(HttpServletRequest request, RedirectAttributes redirectAttributes, EntityHasChildrenException e) {
        redirectAttributes.addFlashAttribute("Exception", true);
        redirectAttributes.addAttribute("exceptionMessage", e.getMessage());

        if (request.getRequestURI().contains("authors")) {
            return "redirect:/authors/list";
        } else {
            return "redirect:/publishers/list";
        }
    }
}
