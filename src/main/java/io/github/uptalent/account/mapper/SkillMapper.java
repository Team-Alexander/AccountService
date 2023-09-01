package io.github.uptalent.account.mapper;

import io.github.uptalent.account.model.entity.Skill;
import io.github.uptalent.account.model.response.SkillResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SkillMapper {
    List<SkillResponse> toSkillResponses(List<Skill> skills);
}
