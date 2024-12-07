package org.example.savegame;

import org.example.service.QueueService;
import org.example.model.Customer;
import org.example.model.Dish;
import org.example.model.Player;
import org.example.model.Worker;
import org.example.view.console.GameSecondView;
import org.example.view.console.GameView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class SaveController {
    Player player;
    QueueService queueController;
    public SaveController(Player player, QueueService queueController) {
        this.player = player;
        this.queueController = queueController;
    }
    public void saveGame() {
        try {
            FileWriter savegame = new FileWriter("savegame.txt");
            List<Worker> workers = player.get_workers();
            for (Worker worker : workers) {
                savegame.write("Worker:,"
                        + worker.getName() + ","
                        + worker.getLevel() + ","
                        + worker.getIncome() + ","
                        + worker.getUpgradeCost() + ","
                        + worker.getId() + "\n");
            }
            savegame.write("Player:,"
                    + player.getBalance() + ","
                    + player.getCurrentProfit() + ","
                    + player.getPreciseWorkersCount()[0] + ","
                    + player.getPreciseWorkersCount()[1] + ","
                    + player.getPreciseWorkersCount()[2] + ","
                    + player.getPreciseWorkersCount()[3] + ","
                    + player.getPrefixNumber()[0] + ","
                    + player.getPrefixNumber()[1] + ","
                    + player.getPrefixNumber()[2] + ","
                    + player.getDisplayedProfit() + "\n");
            Queue<Dish> queue = queueController.getQueue();
            if (!queue.isEmpty()) {
                savegame.write("Queue:,");
                for (Dish dish : queue) {
                    savegame.write(dish.getWorth() + ",");
                }
                savegame.write("\n");
            }
            savegame.write("ClientCount:," + queueController.getClientCount() + "\n");
            Queue<Customer> customerQueue = queueController.getCustomerQueue();
            if (!customerQueue.isEmpty()) {
                savegame.write("CustomerQueue:,");
                for (Customer customer : customerQueue) {
                    savegame.write(customer.getDay() + ",");
                }
                savegame.write("\n");
            }


            savegame.close();

            System.out.println("Zapisano stan gry");
        }
        catch (IOException e) {
            System.out.println("Błąd zapisu stanu gry");
        }
    }
    public void loadGame() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("savegame.txt"));
            List<Worker> workers = new ArrayList<>();
            Queue<Dish> dishQueue = queueController.getQueue();
            Queue<Customer> customerQueue = queueController.getCustomerQueue();
            dishQueue.clear(); // Oczyszczamy istniejące dane kolejki
            customerQueue.clear(); // Oczyszczamy istniejącą kolejkę klientów

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Worker:")) {
                    // Parsowanie linii z danymi pracownika
                    String[] parts = line.split(",");
                    String name = parts[1];
                    int level = Integer.parseInt(parts[2]);
                    double income = Double.parseDouble(parts[3]);
                    double upgradeCost = Double.parseDouble(parts[4]);
                    int id = Integer.parseInt(parts[5]);

                    // Tworzenie obiektu Worker na podstawie danych z pliku i dodanie go do listy
                    Worker worker = new Worker(income, upgradeCost, name, id, level);
                    workers.add(worker);
                } else if (line.startsWith("Player:")) {
                    // Parsowanie linii z danymi gracza
                    String[] parts = line.split(",");
                    double balance = Double.parseDouble(parts[1]);
                    double currentProfit = Double.parseDouble(parts[2]);
                    int[] workersCount = new int[]{
                            Integer.parseInt(parts[3]),
                            Integer.parseInt(parts[4]),
                            Integer.parseInt(parts[5]),
                            Integer.parseInt(parts[6])
                    };
                    int[] prefixNumber = new int[]{
                            Integer.parseInt(parts[7]),
                            Integer.parseInt(parts[8]),
                            Integer.parseInt(parts[9])
                    };
                    player.setDisplayedProfit(Double.parseDouble(parts[10]));

                    // Ustawienie stanu gracza
                    player.setBalance(balance);
                    player.setCurrentProfit(currentProfit);
                    player.setWorkers(workers);
                    player.setWorkersCount(workersCount);
                    player.setPrefixNumber(prefixNumber);
                } else if (line.startsWith("Queue:")) {
                    // Parsowanie linii z danymi kolejki Dish
                    String[] parts = line.split(",");
                    for (int i = 1; i < parts.length; i++) { // Pomijamy pierwszy element ("Queue:")
                        double worth = Double.parseDouble(parts[i]);
                        Dish dish = new Dish(worth); // Zakładamy, że Dish ma konstruktor z jednym parametrem `worth`
                        dishQueue.add(dish);
                    }
                } else if (line.startsWith("ClientCount:")) {
                    // Parsowanie liczby klientów
                    String[] parts = line.split(",");
                    int clientCount = Integer.parseInt(parts[1]);
                    queueController.setClientCount(clientCount); // Zakładamy istnienie metody setClientCount
                } else if (line.startsWith("CustomerQueue:")) {
                    // Parsowanie kolejki Customer
                    String[] parts = line.split(",");
                    for (int i = 1; i < parts.length; i++) { // Pomijamy pierwszy element ("CustomerQueue:")
                        int day = Integer.parseInt(parts[i]);
                        Customer customer = new Customer(day);
                        customerQueue.add(customer);
                    }
                }
            }

            reader.close();

            System.out.println("Wczytano stan gry");
        } catch (IOException e) {
            System.out.println("Błąd odczytu stanu gry");
        } catch (NumberFormatException e) {
            System.out.println("Błąd parsowania danych: " + e.getMessage());
        }
    }

}
