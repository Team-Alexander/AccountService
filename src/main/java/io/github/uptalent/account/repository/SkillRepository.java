package io.github.uptalent.account.repository;

import io.github.uptalent.account.model.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}
