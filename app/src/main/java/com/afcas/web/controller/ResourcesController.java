package com.afcas.web.controller;

import com.afcas.objects.Resource;
import com.afcas.web.service.AuthorizationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/resources")
public class ResourcesController {

    private final AuthorizationService authorizationService;

    public ResourcesController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public String list(Model model) {
        try {
            // Note: getOperationList doesn't exist for resources in the current API
            // We'll show a placeholder message or handle it differently
            model.addAttribute("resources", new ArrayList<Resource>());
            model.addAttribute("info", "Resource listing requires additional API support. Use the CLI or add resources via permissions.");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load resources: " + e.getMessage());
        }
        return "resources";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        return "resource-form";
    }

    @PostMapping
    public String create(
            @RequestParam String id,
            @RequestParam String name,
            RedirectAttributes redirectAttributes) {
        try {
            Resource resource = Resource.builder()
                    .id(id)
                    .name(name)
                    .build();
            authorizationService.addOrUpdateResource(resource);
            redirectAttributes.addFlashAttribute("success", "Resource '" + id + "' created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create resource: " + e.getMessage());
        }
        return "redirect:/resources";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            authorizationService.removeResource(id);
            redirectAttributes.addFlashAttribute("success", "Resource '" + id + "' deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete resource: " + e.getMessage());
        }
        return "redirect:/resources";
    }
}
