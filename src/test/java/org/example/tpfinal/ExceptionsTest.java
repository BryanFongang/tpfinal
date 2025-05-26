package org.example.tpfinal;


import org.example.tpfinal.model.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
public class ExceptionsTest {
    @Test
    void testCapaciteMaxAtteinteException() {
        Concert miniConcert = new Concert("c2", "Mini Concert", LocalDateTime.now(), "Lyon", 1, "minks", "rap");
        Participant p1 = new Participant("p1", "bry", "bry@mail.com");
        Participant p2 = new Participant("p2", "bryan", "bryan@mail.com");

        assertDoesNotThrow(() -> miniConcert.ajouterParticipant(p1));
        Exception exception = assertThrows(CapaciteMaxAtteinteException.class, () -> miniConcert.ajouterParticipant(p2));
        assertEquals("Capacité maximale atteinte pour l'événement: Mini Concert", exception.getMessage());
    }



}
