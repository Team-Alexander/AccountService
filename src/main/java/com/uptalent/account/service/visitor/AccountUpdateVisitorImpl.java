package com.uptalent.account.service.visitor;

import com.uptalent.account.exception.InvalidAgeException;
import com.uptalent.account.exception.SponsorNotFoundException;
import com.uptalent.account.exception.TalentNotFoundException;
import com.uptalent.account.mapper.SponsorMapper;
import com.uptalent.account.mapper.TalentMapper;
import com.uptalent.account.model.entity.Sponsor;
import com.uptalent.account.model.entity.Talent;
import com.uptalent.account.model.property.TalentAgeRange;
import com.uptalent.account.model.request.SponsorUpdate;
import com.uptalent.account.model.request.TalentUpdate;
import com.uptalent.account.model.response.AccountProfile;
import com.uptalent.account.repository.SponsorRepository;
import com.uptalent.account.repository.TalentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AccountUpdateVisitorImpl implements AccountUpdateVisitor{
    private final TalentRepository talentRepository;
    private final SponsorRepository sponsorRepository;
    private final TalentMapper talentMapper;
    private final SponsorMapper sponsorMapper;
    private final TalentAgeRange talentAgeRange;

    @Override
    public AccountProfile updateProfile(Long id, TalentUpdate talentUpdate) {
        Talent talentToUpdate  = getTalentById(id);
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

        return talentMapper.toTalentFullProfile(updatedTalent);
    }

    @Override
    public AccountProfile updateProfile(Long id, SponsorUpdate sponsorUpdate) {
        Sponsor sponsorToUpdate  = getSponsorById(id);
        sponsorToUpdate.setFullname(sponsorUpdate.getFullname());

        Sponsor updatedSponsor = sponsorRepository.save(sponsorToUpdate);

        return sponsorMapper.toSponsorProfile(updatedSponsor);
    }

    private Talent getTalentById(Long id) {
        return talentRepository.findById(id).orElseThrow(TalentNotFoundException::new);
    }

    private Sponsor getSponsorById(Long id) {
        return sponsorRepository.findById(id).orElseThrow(SponsorNotFoundException::new);
    }
}
