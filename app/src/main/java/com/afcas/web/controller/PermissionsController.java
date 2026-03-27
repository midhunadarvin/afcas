package com.afcas.web.controller;

import com.afcas.objects.*;
import com.afcas.web.service.AuthorizationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/permissions")
public class PermissionsController {

    private final AuthorizationService authorizationService;

    public PermissionsController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public String list(Model model) {
        try {
            List<Principal> principals = authorizationService.getPrincipalList();
            model.addAttribute("principals", principals);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load data: " + e.getMessage());
        }
        return "permissions";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        try {
            List<Principal> principals = authorizationService.getPrincipalList();
            List<Operation> operations = authorizationService.getOperationList();
            model.addAttribute("principals", principals);
            model.addAttribute("operations", operations);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load data: " + e.getMessage());
        }
        return "permission-form";
    }

    @PostMapping
    public String create(
            @RequestParam String principalId,
            @RequestParam String operationId,
            @RequestParam String resourceId,
            @RequestParam ResourceAccessPredicateType accessPredicateType,
            RedirectAttributes redirectAttributes) {
        try {
            ResourceAccessPredicate predicate = ResourceAccessPredicate.builder()
                    .principalId(principalId)
                    .operationId(operationId)
                    .resourceId(resourceId)
                    .accessPredicateType(accessPredicateType)
                    .build();
            authorizationService.addAccessPredicate(predicate);
            redirectAttributes.addFlashAttribute("success", 
                    "Permission " + accessPredicateType + " created for " + principalId + " on " + operationId + "/" + resourceId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create permission: " + e.getMessage());
        }
        return "redirect:/permissions";
    }

    @PostMapping("/remove")
    public String remove(
            @RequestParam String principalId,
            @RequestParam String operationId,
            @RequestParam String resourceId,
            @RequestParam ResourceAccessPredicateType accessPredicateType,
            RedirectAttributes redirectAttributes) {
        try {
            ResourceAccessPredicate predicate = ResourceAccessPredicate.builder()
                    .principalId(principalId)
                    .operationId(operationId)
                    .resourceId(resourceId)
                    .accessPredicateType(accessPredicateType)
                    .build();
            authorizationService.removeAccessPredicate(predicate);
            redirectAttributes.addFlashAttribute("success", "Permission removed successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to remove permission: " + e.getMessage());
        }
        return "redirect:/permissions";
    }
}
