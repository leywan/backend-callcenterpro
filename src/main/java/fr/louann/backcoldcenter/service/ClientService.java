package fr.louann.backcoldcenter.service;

import fr.louann.backcoldcenter.models.Client;
import fr.louann.backcoldcenter.models.Company;
import fr.louann.backcoldcenter.models.RemoteAccess;
import fr.louann.backcoldcenter.repository.ClientRepository;
import fr.louann.backcoldcenter.repository.CompanyRepository;
import fr.louann.backcoldcenter.repository.RemoteAccessRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;
    private final RemoteAccessRepository remoteAccessRepository;
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    public ClientService(ClientRepository clientRepository, CompanyRepository companyRepository, RemoteAccessRepository remoteAccessRepository) {
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
        this.remoteAccessRepository = remoteAccessRepository;
    }

    // Charger les clients sans remoteAccess pour les listes
    public Page<Client> getClientsWithoutRemoteAccess(Long companyId, Pageable pageable) {
        return clientRepository.findAllWithoutRemoteAccess(companyId, pageable);
    }

    // Charger un client avec remoteAccess pour la fiche détaillée
    public Optional<Client> getClientWithRemoteAccess(Long id) {
        return clientRepository.findByIdWithRemoteAccess(id);
    }

    public Optional<Client> findClientById(Long id) {
        return clientRepository.findById(id);
    }

    public List<Client> searchClientsReactive(String query) {
        return clientRepository.searchReactive(query);
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    @Transactional
    public Client updateClient(Long id, Client updatedClient) {
        return clientRepository.findById(id).map(client -> {
            client.setName(updatedClient.getName());
            client.setAddress(updatedClient.getAddress());
            client.setPhone(updatedClient.getPhone());
            client.setEmail(updatedClient.getEmail());
            client.setEquipments(updatedClient.getEquipments());
            client.setCompany(updatedClient.getCompany());
            return clientRepository.save(client);
        }).orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID : " + id));
    }

    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client non trouvé avec l'ID : " + id);
        }
        clientRepository.deleteById(id);
    }

    // Méthode d'export CSV pour les clients avec leurs remoteAccess (pour l'export)
    public byte[] exportClientsToCSV(Long companyId) {
        List<Client> clients = clientRepository.findClientsWithRemoteAccessOptimized(companyId);
        if (clients.isEmpty()) {
            logger.warn("⚠️ Aucun client trouvé pour l'entreprise ID={}", companyId);
            return new byte[0];
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL
                     .withDelimiter(';')
                     .withHeader("Id", "Permanence", "Nom", "Adresse", "Téléphone", "Email", "Statut", "Remote Type", "Remote URL", "Remote Username", "Remote Password"))) {
            for (Client client : clients) {
                List<RemoteAccess> remoteAccesses = client.getRemoteAccess();
                if (remoteAccesses == null || remoteAccesses.isEmpty()) {
                    csvPrinter.printRecord(
                            client.getId(),
                            client.getCompany() != null ? client.getCompany().getId() : "N/A",
                            client.getName(),
                            client.getAddress(),
                            client.getPhone(),
                            client.getEmail(),
                            client.getStatus(),
                            "N/A", "N/A", "N/A", "N/A"
                    );
                } else {
                    for (RemoteAccess access : remoteAccesses) {
                        csvPrinter.printRecord(
                                client.getId(),
                                client.getCompany() != null ? client.getCompany().getId() : "N/A",
                                client.getName(),
                                client.getAddress(),
                                client.getPhone(),
                                client.getEmail(),
                                client.getStatus(),
                                access.getType(),
                                access.getUrl(),
                                access.getUsername(),
                                access.getPassword()
                        );
                    }
                }
            }
            csvPrinter.flush();
            logger.info("✅ {} clients exportés pour la société ID={}", clients.size(), companyId);
            return outputStream.toByteArray();
        } catch (IOException e) {
            logger.error("❌ Erreur lors de l'export CSV : {}", e.getMessage(), e);
            throw new RuntimeException("Erreur d'export CSV", e);
        }
    }

    // Méthode d'import CSV pour les clients et leurs remoteAccess
    @Transactional
    public void importClientsFromCSV(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader().withTrim());
            Map<String, Client> clientMap = new HashMap<>();
            List<RemoteAccess> remoteAccessList = new ArrayList<>();
            for (var record : csvParser) {
                try {
                    String companyIdStr = record.get("Permanence");
                    String clientName = record.get("Nom");
                    if (clientName.isEmpty()) {
                        logger.warn("⚠️ Client ignoré : nom manquant !");
                        continue;
                    }
                    Optional<Company> companyOpt = companyRepository.findById(Long.parseLong(companyIdStr));
                    if (companyOpt.isEmpty()) {
                        logger.warn("⚠️ Société inconnue pour la permanence '{}'. Client ignoré.", companyIdStr);
                        continue;
                    }
                    Company company = companyOpt.get();
                    Client client = clientRepository.findByName(clientName).orElse(new Client());
                    client.setName(clientName);
                    client.setAddress(record.get("Adresse"));
                    client.setPhone(record.get("Téléphone"));
                    client.setEmail(record.get("Email"));
                    client.setStatus(record.get("Statut"));
                    client.setCompany(company);
                    clientRepository.save(client);
                    clientMap.put(clientName, client);
                    String remoteType = record.get("Remote Type");
                    if (!"N/A".equalsIgnoreCase(remoteType)) {
                        RemoteAccess remoteAccess = new RemoteAccess();
                        remoteAccess.setType(remoteType);
                        remoteAccess.setUrl(record.get("Remote URL"));
                        remoteAccess.setUsername(record.get("Remote Username"));
                        remoteAccess.setPassword(record.get("Remote Password"));
                        remoteAccess.setClient(client);
                        remoteAccessList.add(remoteAccess);
                    }
                } catch (Exception e) {
                    logger.error("❌ Erreur lors de l'import d'un client : {}", e.getMessage(), e);
                }
            }
            if (!remoteAccessList.isEmpty()) {
                remoteAccessRepository.saveAll(remoteAccessList);
                logger.info("✅ {} accès distants importés.", remoteAccessList.size());
            }
            logger.info("✅ {} clients importés avec succès.", clientMap.size());
        } catch (IOException e) {
            logger.error("❌ Erreur lors de la lecture du fichier CSV : {}", e.getMessage(), e);
            throw new RuntimeException("Erreur d'import CSV", e);
        }
    }
}
