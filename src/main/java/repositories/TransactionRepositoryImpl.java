package repositories;

import daos.TransactionDAO;
import daos.entities.TransactionEntity;
import model.Transaction;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionDAO transactionDAO;

    public TransactionRepositoryImpl(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactionDAO.getTransactions().stream()
                .map(this::convertToTransaction)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Transaction> getTransactions(LocalDateTime from) {

        ZonedDateTime zonedDateTime = from.atZone(ZoneId.of("Europe/Athens"));
        Instant fromInstant = zonedDateTime.toInstant();

        return transactionDAO.getTransactions()
                .stream()
                .filter(transactionEntity -> transactionEntity.getDate().isAfter(fromInstant))
                .map(this::convertToTransaction)
                .collect(Collectors.toList());
    }

    private Transaction convertToTransaction(TransactionEntity transactionEntity) {
        Transaction transaction = new Transaction();

        transaction.setEmailAddress(transactionEntity.getEmail());
        transaction.setAmount(transactionEntity.getAmount());

        Instant timestamp = transactionEntity.getDate();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(timestamp, ZoneId.of("Europe/Athens"));

        transaction.setDate(localDateTime);

        return transaction;
    }

}
