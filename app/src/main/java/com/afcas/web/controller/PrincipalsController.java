package com.afcas.web.controller;

import com.afcas.objects.Principal;
import com.afcas.objects.PrincipalType;
import com.afcas.web.service.AuthorizationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/principals")
public class PrincipalsController {

    private final AuthorizationService authorizationService;

    public PrincipalsController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public String list(Model model) {
        try {
            List<Principal> principals = authorizationService.getPrincipalList();
            model.addAttribute("principals", principals);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load principals: " + e.getMessage());
        }
        return "principals";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("principal", new Principal(null, null, null, null, null, null, null));
        return "principal-form";
    }

    @PostMapping
    public String create(
            @RequestParam String name,
            @RequestParam PrincipalType principalType,
            @RequestParam(required = false) String displayName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String dataSource,
            RedirectAttributes redirectAttributes) {
        try {
            Principal principal = Principal.builder()
                    .id(name)
                    .name(name)
                    .principalType(principalType)
                    .displayName(displayName)
                    .email(email)
                    .description(description)
                    .dataSource(dataSource)
                    .build();
            authorizationService.addOrUpdatePrincipal(principal, dataSource != null ? dataSource : "web");
            redirectAttributes.addFlashAttribute("success", "Principal '" + name + "' created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create principal: " + e.getMessage());
        }
        return "redirect:/principals";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            authorizationService.removePrincipal(id);
            redirectAttributes.addFlashAttribute("success", "Principal '" + id + "' deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete principal: " + e.getMessage());
        }
        return "redirect:/principals";
    }

    @GetMapping("/{id}/members")
    public String members(@PathVariable String id, Model model) {
        try {
            // Find the group principal
            List<Principal> allPrincipals = authorizationService.getPrincipalList();
            Principal group = allPrincipals.stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            
            if (group == null) {
                model.addAttribute("error", "Group not found");
                return "redirect:/principals";
            }
            
            List<Principal> members = authorizationService.getMembersList(group);
            model.addAttribute("group", group);
            model.addAttribute("members", members);
            model.addAttribute("allPrincipals", allPrincipals);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load members: " + e.getMessage());
        }
        return "group-members";
    }

    @PostMapping("/{groupId}/members")
    public String addMember(
            @PathVariable String groupId,
            @RequestParam String memberId,
            RedirectAttributes redirectAttributes) {
        try {
            List<Principal> allPrincipals = authorizationService.getPrincipalList();
            Principal group = allPrincipals.stream()
                    .filter(p -> p.getId().equals(groupId))
                    .findFirst()
                    .orElse(null);
            Principal member = allPrincipals.stream()
                    .filter(p -> p.getId().equals(memberId))
                    .findFirst()
                    .orElse(null);
            
            if (group != null && member != null) {
                authorizationService.addGroupMember(group, member);
                redirectAttributes.addFlashAttribute("success", "Member added successfully.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Group or member not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add member: " + e.getMessage());
        }
        return "redirect:/principals/" + groupId + "/members";
    }

    @PostMapping("/{groupId}/members/{memberId}/remove")
    public String removeMember(
            @PathVariable String groupId,
            @PathVariable String memberId,
            RedirectAttributes redirectAttributes) {
        try {
            List<Principal> allPrincipals = authorizationService.getPrincipalList();
            Principal group = allPrincipals.stream()
                    .filter(p -> p.getId().equals(groupId))
                    .findFirst()
                    .orElse(null);
            Principal member = allPrincipals.stream()
                    .filter(p -> p.getId().equals(memberId))
                    .findFirst()
                    .orElse(null);
            
            if (group != null && member != null) {
                authorizationService.removeGroupMember(group, member);
                redirectAttributes.addFlashAttribute("success", "Member removed successfully.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to remove member: " + e.getMessage());
        }
        return "redirect:/principals/" + groupId + "/members";
    }
}
