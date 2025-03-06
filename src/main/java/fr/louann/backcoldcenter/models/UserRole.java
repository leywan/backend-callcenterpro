package fr.louann.backcoldcenter.models;

public enum UserRole {
    GOD,     // Peut tout faire
    ADMIN,   // Gère les utilisateurs et les tickets
    AGENT,   // Gère uniquement les tickets
    CLIENT,  // Peut voir ses tickets
    COMPANY, // Peut voir les tickets de ses clients
    SUPERVISOR; // Peut voir les stats et assigner les tickets
}

