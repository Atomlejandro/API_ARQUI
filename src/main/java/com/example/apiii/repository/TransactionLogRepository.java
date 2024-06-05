package com.example.apiii.repository;

import com.example.apiii.Entity.TransactionLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TransactionLogRepository {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    public void save(TransactionLog log) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(log.getId()).build());
        item.put("cardNumber", AttributeValue.builder().s(log.getCardNumber()).build());
        item.put("totalAmount", AttributeValue.builder().n(String.valueOf(log.getTotalAmount())).build());
        item.put("bookIds", AttributeValue.builder().ss(log.getBookIds().toString()).build());
        item.put("transactionDate", AttributeValue.builder().s(log.getTransactionDate().toString()).build());
        item.put("ipAddress", AttributeValue.builder().s(log.getIpAddress()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName("Transaction")
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
    }
}
