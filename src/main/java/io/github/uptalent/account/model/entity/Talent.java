package io.github.uptalent.account.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Talent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "talent_account",
            joinColumns = @JoinColumn(name = "talent_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    @EqualsAndHashCode.Exclude
    private Account account;

    @Column(length = 15, nullable = false, name = "lastname")
    private String lastname;

    @Column(length = 15, nullable = false, name = "firstname")
    private String firstname;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "banner")
    private String banner;

    @Column(name = "location")
    private String location;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Column(length = 2250, name = "about_me")
    private String aboutMe;

    @ManyToMany(cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @JoinTable(name = "talent_skills",
            joinColumns = @JoinColumn(name = "talent_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    @EqualsAndHashCode.Exclude
    private Set<Skill> skills = new LinkedHashSet<>();
}
