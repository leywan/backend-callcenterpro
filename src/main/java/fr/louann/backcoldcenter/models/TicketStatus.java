package fr.louann.backcoldcenter.models;

public enum TicketStatus {
    OUVERT,      // Ticket nouvellement créé
    EN_COURS,    // Ticket en cours de traitement
    RESOLU,      // Ticket résolu
    TRANSMIS_TECH, // Ticket transmis au technicien
    CONNEXION_DIST, // Connexion à distance
    INTERVENTION,   // Intervention sur site
    MER,            // Mise en relation
    FERME;       // Ticket fermé
}