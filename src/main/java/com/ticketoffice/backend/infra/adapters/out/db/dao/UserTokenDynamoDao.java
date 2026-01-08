package com.ticketoffice.backend.infra.adapters.out.db.dao;

import com.ticketoffice.backend.infra.adapters.out.db.mapper.UserTokenDynamoDBMapper;
import jakarta.inject.Inject;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class UserTokenDynamoDao extends AbstractDynamoDao {

    private static final String TOKEN_HASH_INDEX = "tokenHash";
    private static final String PASSWORD_RESET_TOKEN_TABLE = "PasswordResetTokenTable";

    @Inject
    public UserTokenDynamoDao() {
        super(
            DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build(),
            PASSWORD_RESET_TOKEN_TABLE
        );
    }

    public Optional<Map<String, AttributeValue>> findByHashToken(String hashToken) {
        return queryByIndex(TOKEN_HASH_INDEX, UserTokenDynamoDBMapper.DynamoKeys.TOKEN_HASH, hashToken)
            .stream()
            .filter(x -> Objects.nonNull(x) && !x.isEmpty())
            .findFirst();
    }
}
