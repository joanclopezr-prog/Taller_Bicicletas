package com.example.taller1;

import java.util.ArrayList;
import java.util.List;

public class ClientRepository {
    private static ClientRepository instance;
    private List<Client> clients;

    private ClientRepository() {
        clients = new ArrayList<>();

        clients.add(new Client( "1090272139", "Alejandro Campo", "3224229356", "alejo@gmail.com", 20));
        clients.add(new Client("108425322", "Danilo Ladino", "3224229358", "danilo@gmail.com", 20));
    }

    public static ClientRepository getInstance() {
        if (instance == null) {
            instance = new ClientRepository();
        }
        return instance;
    }

    public List<Client> getClients() {
        return new ArrayList<>(clients);
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public void updateClient(Client clientActualizado) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getDocument().equals(clientActualizado.getDocument())) {
                clients.set(i, clientActualizado);
                break;
            }
        }
    }

    public void deleteClient(String document) {
        clients.removeIf(client -> client.getDocument().equals(document));
    }

    public Client searchClientPerDocument(String document) {
        return clients.stream()
                .filter(client -> client.getDocument().equals(document))
                .findFirst()
                .orElse(null);
    }
}