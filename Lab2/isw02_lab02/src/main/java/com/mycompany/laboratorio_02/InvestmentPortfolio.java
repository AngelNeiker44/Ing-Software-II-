/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.laboratorio_02;

/**
 *
 * @author jacks
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InvestmentPortfolio {
    private String userId;
    private List<Transaction> transactions;
    private Map<String, Double> fundBalances;

    public InvestmentPortfolio(String userId) {
        this.userId = userId;
        this.transactions = new ArrayList<>();
        this.fundBalances = new HashMap<>();
    }

    public void processBuyTransaction(String fundCode, double amount) {
        processTransaction("BUY", fundCode, amount);
    }

    public void processSellTransaction(String fundCode, double amount) {
        processTransaction("SELL", fundCode, amount);
    }

    private void processTransaction(String type, String fundCode, double amount) {
        // Validaciones comunes
        if (fundCode == null || fundCode.isEmpty()) {
            throw new IllegalArgumentException("El código del fondo no puede estar vacío");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        if (!isFundValid(fundCode)) {
            throw new IllegalArgumentException("El fondo no existe: " + fundCode);
        }

        double currentBalance = fundBalances.getOrDefault(fundCode, 0.0);
        if (type.equals("SELL") && currentBalance < amount) {
            throw new IllegalArgumentException("Saldo insuficiente. Balance actual: " + currentBalance);
        }

        // Crear transacción
        String transactionId = UUID.randomUUID().toString();
        Date transactionDate = new Date();
        Transaction transaction = new Transaction(transactionId, userId, fundCode, type, amount, transactionDate);

        // Agregar transacción y actualizar balance
        transactions.add(transaction);
        double updatedBalance = type.equals("BUY") ? currentBalance + amount : currentBalance - amount;
        fundBalances.put(fundCode, updatedBalance);

        // Registro simulado y notificación
        saveTransactionToDatabase(transaction);
        String action = type.equals("BUY") ? "compra" : "rescate";
        sendNotificationToUser("Se ha realizado una " + action + " por $" + amount + " en el fondo " + fundCode);

        System.out.println("Transacción de " + action + " procesada exitosamente. ID: " + transactionId);
    }

    private boolean isFundValid(String fundCode) {
        return fundCode.startsWith("FUND");
    }

    private void saveTransactionToDatabase(Transaction transaction) {
        System.out.println("Guardando transacción en la base de datos: " + transaction.getId());
    }

    private void sendNotificationToUser(String message) {
        System.out.println("Notificación para usuario " + userId + ": " + message);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Map<String, Double> getFundBalances() {
        return fundBalances;
    }
}
