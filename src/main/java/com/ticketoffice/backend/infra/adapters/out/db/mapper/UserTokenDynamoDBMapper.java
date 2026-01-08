package com.ticketoffice.backend.infra.adapters.out.db.mapper;

import com.ticketoffice.backend.domain.models.UserToken;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

public class UserTokenDynamoDBMapper {

    public static class DynamoKeys {
        public static final String ID = "id";
        public static final String USERNAME = "username";
        public static final String EMAIL = "email";
        public static final String TOKEN_HASH = "tokenHash";
        public static final String EXPIRES_AT = "expiresAt";
        public static final String USED = "used";
    }

    private UserTokenDynamoDBMapper() {}

    public static Map<String, AttributeValue> toMap(UserToken userToken) {
        Map<String, AttributeValue> attributeValueMap = new HashMap<>();
        attributeValueMap.put(DynamoKeys.ID, AttributeValue.builder().s(userToken.id()).build());
        attributeValueMap.put(DynamoKeys.USERNAME, AttributeValue.builder().s(userToken.username()).build());
        attributeValueMap.put(DynamoKeys.EMAIL, AttributeValue.builder().s(userToken.email()).build());
        attributeValueMap.put(DynamoKeys.TOKEN_HASH, AttributeValue.builder().s(userToken.tokenHash()).build());
        attributeValueMap.put(DynamoKeys.EXPIRES_AT, AttributeValue.builder().n(String.valueOf(userToken.expiresAt())).build());
        attributeValueMap.put(DynamoKeys.USED, AttributeValue.builder().bool(userToken.used()).build());
        return attributeValueMap;
    }

    public static UserToken fromMap(Map<String, AttributeValue> item) {
        if (item == null || item.isEmpty()) {
            return null;
        }
        
        return new UserToken(
            item.get(DynamoKeys.ID).s(),
            item.get(DynamoKeys.USERNAME).s(),
            item.get(DynamoKeys.EMAIL).s(),
            item.get(DynamoKeys.TOKEN_HASH).s(),
            Long.parseLong(item.get(DynamoKeys.EXPIRES_AT).n()),
            item.get(DynamoKeys.USED).bool()
        );
    }
}
