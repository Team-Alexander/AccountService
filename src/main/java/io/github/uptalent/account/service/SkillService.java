package io.github.uptalent.account.service;

import io.github.uptalent.account.exception.SkillNotFoundException;
import io.github.uptalent.account.mapper.SkillMapper;
import io.github.uptalent.account.model.entity.Skill;
import io.github.uptalent.account.model.entity.Talent;
import io.github.uptalent.account.model.request.SkillRequest;
import io.github.uptalent.account.model.response.SkillResponse;
import io.github.uptalent.account.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;
    private final TalentService talentService;

    @Transactional(readOnly = true)
    public List<SkillResponse> getAllSkills() {
        return skillMapper.toSkillResponses(skillRepository.findAll());
    }

    public void updateTalentSkills(Long id, SkillRequest skillRequest) {
        Talent talent = talentService.getTalentById(id);

        Set<Long> skillIds = skillRequest.getSkillIds();
        List<Skill> skills = skillRepository.findAllById(skillIds);
        if(skills.size() != skillIds.size()) {
            throw new SkillNotFoundException();
        }

        talent.setSkills(new LinkedHashSet<>(skills));
        talentService.save(talent);
    }
}
