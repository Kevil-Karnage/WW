package com.program.WhoWins.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", updatable = false, nullable = false, unique = true)
    private String name;
    @Column(name = "nickname", updatable = false, nullable = false, unique = true)
    private String nickname;
    @Column(name = "surname", updatable = false, nullable = false, unique = true)
    private String surname;

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
