package com.ssafy.happyhouse.domain.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.ssafy.happyhouse.domain.enums.Authority;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "AUTHORITY")
@DynamicInsert @DynamicUpdate
@Getter @Setter @NoArgsConstructor
public class Role extends BaseTime {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ROLE", unique = true)
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @ManyToMany(mappedBy = "roles")
    private List<Member> members = new ArrayList<>();

    @Builder
    public Role(Authority authority) {
        this.authority = authority;
    }
}
