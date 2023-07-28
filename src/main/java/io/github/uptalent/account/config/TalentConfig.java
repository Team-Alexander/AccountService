package io.github.uptalent.account.config;

import io.github.uptalent.account.model.property.TalentAgeRange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TalentConfig {
    @Value("${talent.min-age}")
    private int TALENT_MIN_AGE;

    @Value("${talent.max-age}")
    private int TALENT_MAX_AGE;

    @Bean
    public TalentAgeRange talentAgeRange() {
        return new TalentAgeRange(TALENT_MIN_AGE, TALENT_MAX_AGE);
    }
}