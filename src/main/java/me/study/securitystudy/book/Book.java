package me.study.securitystudy.book;

import lombok.Getter;
import lombok.Setter;
import me.study.securitystudy.account.Account;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
public class Book {

    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    @ManyToOne
    private Account author;
}
