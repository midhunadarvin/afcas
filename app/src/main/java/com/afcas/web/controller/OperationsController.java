package com.afcas.web.controller;

import com.afcas.objects.Operation;
import com.afcas.web.service.AuthorizationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/operations")
public class OperationsController {

    private final AuthorizationService authorizationService;

    public OperationsController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public String list(Model model) {
        try {
            List<Operation> operations = authorizationService.getOperationList();
            model.addAttribute("operations", operations);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load operations: " + e.getMessage());
        }
        return "operations";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        return "operation-form";
    }

    @PostMapping
    public String create(
            @RequestParam String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            RedirectAttributes redirectAttributes) {
        try {
            Operation operation = Operation.builder()
                    .id(id)
                    .name(name != null ? name : id)
                    .description(description)
                    .build();
            authorizationService.addOrUpdateOperation(operation);
            redirectAttributes.addFlashAttribute("success", "Operation '" + id + "' created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create operation: " + e.getMessage());
        }
        return "redirect:/operations";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            authorizationService.removeOperation(id);
            redirectAttributes.addFlashAttribute("success", "Operation '" + id + "' deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete operation: " + e.getMessage());
        }
        return "redirect:/operations";
    }
}
