package org.lskk.lumen.speech.synthesis.jpa;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ceefour on 10/8/15.
 */
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uk_wordwithsampa_word", columnNames = "word")
})
public class KnownWord implements Serializable {

    @Id
    private Integer id;
    @Column(nullable = false)
    private String word;
    @Column(nullable = false)
    private String syllables;
    @Column(nullable = false)
    private String sampa;
    @Column(nullable = false)
    private String syllablesSampa;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSyllables() {
        return syllables;
    }

    public void setSyllables(String syllables) {
        this.syllables = syllables;
    }

    public String getSampa() {
        return sampa;
    }

    public void setSampa(String sampa) {
        this.sampa = sampa;
    }

    public String getSyllablesSampa() {
        return syllablesSampa;
    }

    public void setSyllablesSampa(String syllablesSampa) {
        this.syllablesSampa = syllablesSampa;
    }
}
