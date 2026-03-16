package com.example.unitprojectspring.Controllers;
import org.springframework.ui.Model;
import com.example.unitprojectspring.DTO.SectionDTO;
import com.example.unitprojectspring.Entities.Section;
import com.example.unitprojectspring.Service.SectionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/project/{projectId}/add")
    public String addSection(@ModelAttribute Section section, @PathVariable Long projectId, HttpServletRequest request) {

        sectionService.createSection(section, projectId);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/api/dashboard");
    }

    @PostMapping("/{id}/update")
    public String updateSection(@PathVariable Long id, @ModelAttribute Section section, HttpServletRequest request) {

        sectionService.updateSection(id, section);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/api/dashboard");
    }

    @PostMapping("/{id}/delete")
    public String deleteSection(@PathVariable Long id, HttpServletRequest request) {

        sectionService.deleteSection(id);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/api/dashboard");
    }

    @GetMapping("/{id}")
    public String viewSectionDetails(@PathVariable Long id, Model model) {

        SectionDTO section = sectionService.getSectionById(id);

        model.addAttribute("section", section);

        return "section-details"; // Loads our new template
    }
}