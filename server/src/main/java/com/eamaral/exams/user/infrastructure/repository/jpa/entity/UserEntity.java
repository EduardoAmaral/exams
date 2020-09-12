package com.eamaral.exams.user.infrastructure.repository.jpa.entity;

import com.eamaral.exams.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "TB_USER")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserEntity {

    @Id
    private String id;

    @Column(unique = true)
    @NotEmpty(message = "{user.email.required}")
    @Email(message = "{user.email.invalid}")
    private String email;

    @Column
    @NotEmpty(message = "{user.name.required}")
    private String name;

    @Column
    @NotEmpty(message = "{user.surname.required}")
    private String surname;

    @Column
    private String picture;

    public static UserEntity from(User user) {
        return builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .surname(user.getSurname())
                .picture(user.getPicture())
                .build();
    }

    public User toUser() {
        return User.builder()
                .id(getId())
                .email(getEmail())
                .name(getName())
                .surname(getSurname())
                .picture(getPicture())
                .build();
    }
}
