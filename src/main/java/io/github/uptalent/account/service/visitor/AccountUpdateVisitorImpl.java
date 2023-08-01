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
import io.github.uptalent.account.service.SponsorService;
import io.github.uptalent.account.service.TalentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AccountUpdateVisitorImpl implements AccountUpdateVisitor{
    private final TalentRepository talentRepository;
    private final SponsorRepository sponsorRepository;
    private final TalentService talentService;
    private final SponsorService sponsorService;
    private final TalentMapper talentMapper;
    private final SponsorMapper sponsorMapper;
    private final TalentAgeRange talentAgeRange;
    private final ContentClient contentClient;

    @Override
    public AccountProfile updateProfile(Long id, TalentUpdate talentUpdate) {
        Talent talentToUpdate  = talentService.getTalentById(id);
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
            talentToUpdate.setAboutMe(talentUpdate.getAboutMe());
        }
        Talent updatedTalent = talentRepository.save(talentToUpdate);

        contentClient.updateProofsByAuthor(talentMapper.toAuthorUpdate(updatedTalent));

        return talentMapper.toTalentFullProfile(updatedTalent);
    }

    @Override
    public AccountProfile updateProfile(Long id, SponsorUpdate sponsorUpdate) {
        Sponsor sponsorToUpdate  = sponsorService.getSponsorById(id);
        sponsorToUpdate.setFullname(sponsorUpdate.getFullname());

        Sponsor updatedSponsor = sponsorRepository.save(sponsorToUpdate);

        return sponsorMapper.toSponsorProfile(updatedSponsor);
    }
}
