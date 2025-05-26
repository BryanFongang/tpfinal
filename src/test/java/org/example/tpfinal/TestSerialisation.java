package org.example.tpfinal;

import org.example.tpfinal.model.*;
import org.example.tpfinal.utils.PersistenceService;
import org.junit.jupiter.api.*;
import org.example.tpfinal.model.Concert;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestSerialisation {
    Participant participant;
    Concert concert;
    Conference conference;
    PersistenceService persistenceService;
    @BeforeEach
    void setup() throws Exception {
        participant = new Participant("p1", "Jean Dupont", "jean@example.com");
        concert = new Concert("c1", "Concert Test", LocalDateTime.now(), "bafoussam", 100, "Artiste Test", "Pop");
        conference = new Conference("conf1", "Conférence", LocalDateTime.now(), "Douala", 50, "ancienneté", List.of("Dr.FONGANG", "Prof. BRYAN"));
        persistenceService = new PersistenceService();
    }
    @Test
    void testSauvegardeEtChargementJSON() throws Exception {
        PersistenceService service = new PersistenceService();
        List<Evenement> evenements = List.of(
                new Concert("c1", "Test", LocalDateTime.now(), "Yaounde", 100, "bryan", "mbolé")
        );

        File file = new File("data/test_evenements.json");
        service.sauvegarderJSON(evenements, file.getPath());

        List<Evenement> event = service.chargerJSON(file.getPath());

        assertEquals(1, event.size());
        assertEquals("Test", event.get(0).getNom());

        file.delete();
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
