package com.uptalent.account.service.visitor;

import com.uptalent.account.exception.InvalidAgeException;
import com.uptalent.account.exception.TalentNotFoundException;
import com.uptalent.account.mapper.TalentMapper;
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
    private final TalentAgeRange talentAgeRange;

    @Override
    public AccountProfile updateProfile(Long id, TalentUpdate updatedTalent) {
        Talent talentToUpdate  = getTalentById(id);
        talentToUpdate.setLastname(updatedTalent.getLastname());
        talentToUpdate.setFirstname(updatedTalent.getFirstname());
        LocalDate birthday = updatedTalent.getBirthday();

        if(birthday != null) {
            if (birthday.isBefore(LocalDate.now().minusYears(talentAgeRange.getMaxAge())) ||
                    birthday.isAfter(LocalDate.now().minusYears(talentAgeRange.getMinAge()))) {
                throw new InvalidAgeException();
            }
            talentToUpdate.setBirthday(updatedTalent.getBirthday());
        }
        if(updatedTalent.getLocation() != null) {
            talentToUpdate.setLocation(updatedTalent.getLocation());
        }
        if(updatedTalent.getAboutMe() != null) {
            talentToUpdate.setAboutMe(updatedTalent.getAboutMe());
        }
        Talent savedTalent = talentRepository.save(talentToUpdate);

        return talentMapper.toTalentFullProfile(savedTalent);
    }

    @Override
    public AccountProfile updateProfile(Long id, SponsorUpdate sponsorUpdate) {
        //TODO implement
        return null;
    }

    private Talent getTalentById(Long id){
        return talentRepository.findById(id).orElseThrow(TalentNotFoundException::new);
    }
}
