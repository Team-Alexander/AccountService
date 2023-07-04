package com.uptalent.account.model.entity;

import com.uptalent.account.model.entity.Account;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Talent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinTable(
            name = "talent_account",
            joinColumns = @JoinColumn(name = "talent_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private Account account;

    @Column(length = 15, nullable = false, name = "lastname")
    private String lastname;

    @Column(length = 15, nullable = false, name = "firstname")
    private String firstname;
}
