package io.github.uptalent.account.service.visitor;

import io.github.uptalent.account.client.ContentClient;
import io.github.uptalent.account.exception.InvalidAgeException;
import io.github.uptalent.account.mapper.SponsorMapper;
import io.github.uptalent.account.mapper.TalentMapper;
import io.github.uptalent.account.model.entity.Sponsor;
import io.github.uptalent.account.model.entity.Talent;
import io.github.uptalent.account.model.property.TalentAgeRange;
import io.github.uptalent.account.model.request.SponsorUpdate;
import io.github.uptalent.account.model.request.TalentUpdate;
import io.github.uptalent.account.model.response.AccountProfile;
import io.github.uptalent.account.repository.SponsorRepository;
import io.github.uptalent.account.repository.TalentRepository;
import io.github.uptalent.account.service.ReportService;
import io.github.uptalent.account.service.SponsorService;
import io.github.uptalent.account.service.TalentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static io.github.uptalent.starter.security.Role.SPONSOR;
import static io.github.uptalent.starter.security.Role.TALENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountUpdateVisitorImpl implements AccountUpdateVisitor {
    private final TalentRepository talentRepository;
    private final SponsorRepository sponsorRepository;
    private final TalentService talentService;
    private final SponsorService sponsorService;
    private final TalentMapper talentMapper;
    private final SponsorMapper sponsorMapper;
    private final TalentAgeRange talentAgeRange;
    private final ContentClient contentClient;
    private final ReportService reportService;

    @Override
    public AccountProfile updateProfile(Long id, TalentUpdate talentUpdate) {
        Talent talentToUpdate  = talentService.getTalentById(id);
        StringBuilder sb = new StringBuilder();
        sb.append(talentUpdate.getLastname()).append(" ").append(talentToUpdate.getFirstname());

        talentToUpdate.setLastname(talentUpdate.getLastname());
        talentToUpdate.setFirstname(talentUpdate.getFirstname());
        LocalDate birthday = talentUpdate.getBirthday();

        if(birthday != null) {
            if (birthday.isBefore(LocalDate.now().minusYears(talentAgeRange.getMaxAge())) ||
                    birthday.isAfter(LocalDate.now().minusYears(talentAgeRange.getMinAge()))) {
                throw new InvalidAgeException();
            }
            talentToUpdate.setBirthday(talentUpdate.getBirthday());
        }
        if(talentUpdate.getLocation() != null) {
            talentToUpdate.setLocation(talentUpdate.getLocation());
        }
        if(talentUpdate.getAboutMe() != null) {
            String aboutMe = talentUpdate.getAboutMe();
            sb.append(" ").append(aboutMe);
            talentToUpdate.setAboutMe(aboutMe);
        }
        Talent updatedTalent = talentRepository.save(talentToUpdate);

        contentClient.updateProofsByAuthor(talentMapper.toAuthorUpdate(updatedTalent));

        reportService.checkToxicity(id, sb.toString(), TALENT);

        return talentMapper.toTalentFullProfile(updatedTalent);
    }

    @Override
    public AccountProfile updateProfile(Long id, SponsorUpdate sponsorUpdate) {
        Sponsor sponsorToUpdate  = sponsorService.getSponsorById(id);
        String name = sponsorUpdate.getFullname();
        reportService.checkToxicity(id, name, SPONSOR);

        sponsorToUpdate.setFullname(name);
        Sponsor updatedSponsor = sponsorRepository.save(sponsorToUpdate);
        return sponsorMapper.toSponsorProfile(updatedSponsor);
    }
}
