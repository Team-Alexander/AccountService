package io.github.uptalent.account;

import io.github.uptalent.account.exception.SkillNotFoundException;
import io.github.uptalent.account.exception.TalentNotFoundException;
import io.github.uptalent.account.mapper.SkillMapper;
import io.github.uptalent.account.model.entity.Skill;
import io.github.uptalent.account.model.entity.Talent;
import io.github.uptalent.account.model.request.SkillRequest;
import io.github.uptalent.account.model.response.SkillResponse;
import io.github.uptalent.account.repository.SkillRepository;
import io.github.uptalent.account.service.SkillService;
import io.github.uptalent.account.service.TalentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static io.github.uptalent.account.utils.MockModelsUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SkillServiceTest {
    @Mock
    private SkillRepository skillRepository;
    @Mock
    private SkillMapper skillMapper;
    @Mock
    private TalentService talentService;
    @InjectMocks
    private SkillService skillService;

    @Test
    @DisplayName("Get all skills successfully")
    public void getAllSkillsSuccessfully() {
        List<Skill> skillList = generateSkills();
        List<SkillResponse> responseList = generateSkillResponses();

        when(skillRepository.findAll()).thenReturn(skillList);
        when(skillMapper.toSkillResponses(skillList)).thenReturn(responseList);

        List<SkillResponse> result = skillService.getAllSkills();

        assertEquals(responseList.size(), result.size());
        verify(skillRepository, times(1)).findAll();
        verify(skillMapper, times(1)).toSkillResponses(skillList);
    }

    @Test
    @DisplayName("Update Talent skills successfully")
    public void updateTalentSkillsSuccessfully() {
        Talent talent = generateTalent();
        SkillRequest skillRequest  = generateSkillRequest();
        Set<Long> skillIds = skillRequest.getSkillIds();
        List<Skill> skills = generateSkills();
        Long talentId = talent.getId();

        when(talentService.getTalentById(talentId)).thenReturn(talent);
        when(skillRepository.findAllById(skillIds)).thenReturn(skills);

        skillService.updateTalentSkills(talentId, skillRequest);

        verify(skillRepository, times(1)).findAllById(skillIds);
        verify(talentService, times(1)).getTalentById(talentId);
        verify(talentService, times(1)).save(talent);
    }

    @Test
    @DisplayName("Update Talent skills when talent is not found")
    public void updateTalentSkillsWhenTalentNotFound() {
        SkillRequest skillRequest  = generateSkillRequest();
        Long talentId = 1L;

        when(talentService.getTalentById(talentId)).thenThrow(TalentNotFoundException.class);

        assertThrows(TalentNotFoundException.class,
                () -> skillService.updateTalentSkills(talentId, skillRequest));
    }

    @Test
    @DisplayName("Update Talent skills when skill is not found")
    public void updateTalentSkillsWhenSkillNotFound() {
        Talent talent = generateTalent();
        SkillRequest skillRequest  = generateSkillRequest();
        Set<Long> skillIds = skillRequest.getSkillIds();
        List<Skill> skillList = Collections.emptyList();
        Long talentId = talent.getId();

        when(talentService.getTalentById(talentId)).thenReturn(talent);
        when(skillRepository.findAllById(skillIds)).thenReturn(skillList);

        assertThrows(SkillNotFoundException.class,
                () -> skillService.updateTalentSkills(talentId, skillRequest));
    }
}
