package io.github.uptalent.account.repository;

import io.github.uptalent.account.model.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
