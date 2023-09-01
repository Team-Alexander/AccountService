package io.github.uptalent.account.controller;

import io.github.uptalent.account.model.response.SkillResponse;
import io.github.uptalent.account.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account/skills")
public class SkillController {
    private final SkillService skillService;

    @GetMapping
    public List<SkillResponse> getAllSkills() {
        return skillService.getAllSkills();
    }
}
