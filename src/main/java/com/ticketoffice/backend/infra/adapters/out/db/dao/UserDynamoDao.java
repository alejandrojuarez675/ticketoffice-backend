package com.ticketoffice.backend.infra.adapters.out.db.dao;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class UserDynamoDao extends AbstractDynamoDao {
    public UserDynamoDao() {
        super(
                DynamoDbClient.builder()
                        .region(Region.US_EAST_1)
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build(),
                "UserTable"
        );
    }
}
