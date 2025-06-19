package com.nexora.nexora_crypto_api.service;

public class TransactionInvoker {
    private TransactionCommand command;

    public void setCommand(TransactionCommand command) {
        this.command = command;
    }

    public void process() {
        command.execute();
    }
}
