package com.example.poll.model.buisness;

import com.example.poll.model.audit.UserDateAudit;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "polls")
public class Poll extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 140)
    private String question;

    @OneToMany(
            mappedBy = "poll", // parent property in child class
            cascade = CascadeType.ALL, // owner property
            fetch = FetchType.EAGER,
            orphanRemoval = true // removed child when it's no longer referenced from the parent entity
    )
//    @Size(min = 2, max = 6)
    @Fetch(FetchMode.SELECT) // way of sql query loading records
    @BatchSize(size = 30) // optimization of sql query amounts
    private List<Choice> choices = new ArrayList<>();

    @NotNull
    public Instant expirationDateTime;

    public void addChoice(Choice choice) { // needed for bidirectional communication and synchronization
        choices.add(choice);
        choice.setPoll(this);
    }

    public void removeChoice(Choice choice) {
        choices.remove(choice);
        choice.setPoll(null);
    }
}
