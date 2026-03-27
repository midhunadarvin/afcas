package com.afcas.web.controller;

import com.afcas.objects.*;
import com.afcas.web.service.AuthorizationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/authorization")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public String index(Model model) {
        try {
            List<Principal> principals = authorizationService.getPrincipalList();
            List<Operation> operations = authorizationService.getOperationList();
            model.addAttribute("principals", principals);
            model.addAttribute("operations", operations);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load data: " + e.getMessage());
        }
        return "authorization";
    }

    @PostMapping("/check")
    public String checkAuthorization(
            @RequestParam String principalId,
            @RequestParam String operationId,
            @RequestParam String resourceId,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            boolean isAuthorized = authorizationService.isAuthorized(principalId, operationId, resourceId);
            model.addAttribute("principalId", principalId);
            model.addAttribute("operationId", operationId);
            model.addAttribute("resourceId", resourceId);
            model.addAttribute("isAuthorized", isAuthorized);
            
            List<Principal> principals = authorizationService.getPrincipalList();
            List<Operation> operations = authorizationService.getOperationList();
            model.addAttribute("principals", principals);
            model.addAttribute("operations", operations);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to check authorization: " + e.getMessage());
            return "redirect:/authorization";
        }
        return "authorization";
    }

    @GetMapping("/digest/{principalId}")
    public String getDigest(@PathVariable String principalId, Model model) {
        try {
            List<ResourceAccessPredicate> digest = authorizationService.getAuthorizationDigest(principalId);
            model.addAttribute("principalId", principalId);
            model.addAttribute("digest", digest);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to get authorization digest: " + e.getMessage());
        }
        return "authorization-digest";
    }

    @PostMapping("/authorized-operations")
    public String getAuthorizedOperations(
            @RequestParam String principalId,
            @RequestParam String resourceId,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            List<Operation> operations = authorizationService.getAuthorizedOperations(principalId, resourceId);
            model.addAttribute("principalId", principalId);
            model.addAttribute("resourceId", resourceId);
            model.addAttribute("operations", operations);
            
            List<Principal> principals = authorizationService.getPrincipalList();
            model.addAttribute("principals", principals);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to get authorized operations: " + e.getMessage());
            return "redirect:/authorization";
        }
        return "authorized-operations";
    }

    @PostMapping("/authorized-resources")
    public String getAuthorizedResources(
            @RequestParam String principalId,
            @RequestParam String operationId,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            List<Resource> resources = authorizationService.getAuthorizedResources(principalId, operationId);
            model.addAttribute("principalId", principalId);
            model.addAttribute("operationId", operationId);
            model.addAttribute("resources", resources);
            
            List<Principal> principals = authorizationService.getPrincipalList();
            List<Operation> operations = authorizationService.getOperationList();
            model.addAttribute("principals", principals);
            model.addAttribute("operations", operations);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to get authorized resources: " + e.getMessage());
            return "redirect:/authorization";
        }
        return "authorized-resources";
    }
}
