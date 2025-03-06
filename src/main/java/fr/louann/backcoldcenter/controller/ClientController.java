package fr.louann.backcoldcenter.controller;

import fr.louann.backcoldcenter.models.Client;
import fr.louann.backcoldcenter.models.ClientDetailDTO;
import fr.louann.backcoldcenter.models.ClientDTO;
import fr.louann.backcoldcenter.models.RemoteAccess;
import fr.louann.backcoldcenter.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*")
public class ClientController {

    @Autowired
    private ClientService clientService;

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    /**
     * 📌 Charger la liste des clients sans remote_access pour éviter le chargement excessif.
     */
    @GetMapping
    public ResponseEntity<Page<ClientDTO>> getClients(
            @RequestParam(required = false) Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<ClientDTO> clients = clientService.getClientsWithoutRemoteAccess(companyId, pageable)
                .map(ClientDTO::fromEntity);

        if (clients.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(clients);
    }

    /**
     * 📌 Recherche des clients par nom ou email.
     * 🔄 Renvoie une liste de `ClientDTO` pour ne pas charger `remoteAccess`.
     */
    @GetMapping("/search")
    public ResponseEntity<List<ClientDTO>> searchClientsReactive(@RequestParam String query) {
        List<ClientDTO> clients = clientService.searchClientsReactive(query)
                .stream()
                .map(ClientDTO::fromEntity)
                .collect(Collectors.toList());

        if (clients.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(clients);
    }

    /**
     * 📌 Création d'un client.
     */
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client savedClient = clientService.createClient(client);
        return ResponseEntity.ok(savedClient);
    }

    /**
     * 📌 Mise à jour d'un client avec gestion correcte des remoteAccess.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client clientDetails) {
        Optional<Client> clientOpt = clientService.findClientById(id);
        if (clientOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Client client = clientOpt.get();
        client.setName(clientDetails.getName());
        client.setAddress(clientDetails.getAddress());
        client.setEmail(clientDetails.getEmail());
        client.setPhone(clientDetails.getPhone());
        client.setStatus(clientDetails.getStatus());

        // Gérer remoteAccess correctement pour éviter les doublons
        if (clientDetails.getRemoteAccess() != null) {
            client.getRemoteAccess().clear();
            for (RemoteAccess newAccess : clientDetails.getRemoteAccess()) {
                newAccess.setClient(client);
                client.getRemoteAccess().add(newAccess);
            }
        }

        Client updatedClient = clientService.updateClient(id, client);
        return ResponseEntity.ok(updatedClient);
    }

    /**
     * 📌 Suppression d'un client.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 📌 Export CSV des clients d'une entreprise donnée.
     */
    @GetMapping("/export/{companyId}")
    public ResponseEntity<byte[]> exportClientsToCSV(@PathVariable Long companyId) {
        byte[] csvData = clientService.exportClientsToCSV(companyId);

        if (csvData.length == 0) {
            return ResponseEntity.noContent().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clients_" + companyId + ".csv");
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentLength(csvData.length);

        return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
    }

    /**
     * 📌 Importation de clients via CSV.
     */
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importClients(@RequestParam("file") MultipartFile file) {
        try {
            clientService.importClientsFromCSV(file);
            return ResponseEntity.ok("Import terminé avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de l'import : " + e.getMessage());
        }
    }

    /**
     * 📌 Charger les détails d'un client avec ses remote_access.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientDetailDTO> getClientDetails(@PathVariable Long id) {
        return clientService.getClientWithRemoteAccess(id)
                .map(ClientDetailDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
