package org.example.tpfinal.utils;

import org.example.tpfinal.model.Concert;
import org.example.tpfinal.model.Conference;
import org.example.tpfinal.model.Evenement;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

// Wrapper pour la sérialisation XML
@XmlRootElement(name = "evenements")
@XmlAccessorType(XmlAccessType.FIELD)
public class EvenementsContainer {
    @XmlElementWrapper(name = "liste")
    @XmlElements({
            @XmlElement(name = "conference", type = Conference.class),
            @XmlElement(name = "concert", type = Concert.class)
    })
    private List<Evenement> evenements;

    public EvenementsContainer() {}

    public EvenementsContainer(List<Evenement> evenements) {
        this.evenements = evenements;
    }

    public List<Evenement> getEvenements() { return evenements; }
    public void setEvenements(List<Evenement> evenements) { this.evenements = evenements; }
}