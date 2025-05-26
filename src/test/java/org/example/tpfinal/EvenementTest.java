package org.example.tpfinal;

import org.example.tpfinal.model.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EvenementTest {

    private Participant participant;
    private Concert concert;

    @BeforeEach
    void objet() {
        participant = new Participant("p1", "bryan", "bryan@gmail.com");
        concert = new Concert("c1", "Concert Test", LocalDateTime.now(), "Yaoundé", 100, "Petit pays", "bikutsi");
    }

    @Test
    void testAjouterParticipant() throws Exception {
        concert.ajouterParticipant(participant);
        assertTrue(concert.getParticipants().contains(participant));
    }

    @Test
    void testRetirerParticipant() throws Exception {
        concert.ajouterParticipant(participant);
        concert.retirerParticipant(participant);
        assertFalse(concert.getParticipants().contains(participant));
    }
}
