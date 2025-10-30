package com.ticketoffice.backend.infra.adapters.out.db.dao;

import com.google.inject.Inject;
import com.ticketoffice.backend.infra.adapters.out.db.mapper.UserDynamoDBMapper;
import java.util.Map;
import java.util.Optional;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class UserDynamoDao extends AbstractDynamoDao {

    private static final String USERNAME_INDEX = "username-index";
    private static final String EMAIL_INDEX = "email-index";
    private static final String USER_TABLE = "UserTable";

    @Inject
    public UserDynamoDao() {
        super(
                DynamoDbClient.builder()
                        .region(Region.US_EAST_1)
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build(),
                USER_TABLE
        );
    }

    public Optional<Map<String, AttributeValue>> findOneByEmail(String email) {
        return queryByIndex(EMAIL_INDEX, UserDynamoDBMapper.DynamoKeys.EMAIL, email)
                .stream().findAny();
    }

    public Optional<Map<String, AttributeValue>> findOneByUsername(String username) {
        return queryByIndex(USERNAME_INDEX, UserDynamoDBMapper.DynamoKeys.USERNAME, username)
                .stream().findAny();
    }
}
