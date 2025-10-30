package com.ticketoffice.backend.infra.adapters.out.db.mapper;

import static com.ticketoffice.backend.infra.adapters.out.db.mapper.UserDynamoDBMapper.DynamoKeys.EMAIL;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.UserDynamoDBMapper.DynamoKeys.ID;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.UserDynamoDBMapper.DynamoKeys.ORGANIZER_ID;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.UserDynamoDBMapper.DynamoKeys.ORGANIZER_LOGO_ALT;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.UserDynamoDBMapper.DynamoKeys.ORGANIZER_LOGO_ID;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.UserDynamoDBMapper.DynamoKeys.ORGANIZER_LOGO_URL;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.UserDynamoDBMapper.DynamoKeys.ORGANIZER_NAME;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.UserDynamoDBMapper.DynamoKeys.ORGANIZER_URL;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.UserDynamoDBMapper.DynamoKeys.PASSWORD;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.UserDynamoDBMapper.DynamoKeys.ROLES;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.UserDynamoDBMapper.DynamoKeys.USERNAME;
import com.ticketoffice.backend.domain.enums.UserRole;
import com.ticketoffice.backend.domain.models.Image;
import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.models.User;
import org.jetbrains.annotations.NotNull;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

public class UserDynamoDBMapper {

    public static class DynamoKeys {
        public static final String ID = "id";
        public static final String USERNAME = "username";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String ROLES = "roles";
        public static final String ORGANIZER_ID = "organizerId";
        public static final String ORGANIZER_NAME = "organizerName";
        public static final String ORGANIZER_URL = "organizerUrl";
        public static final String ORGANIZER_LOGO_ID = "organizerLogoId";
        public static final String ORGANIZER_LOGO_URL = "organizerLogoUrl";
        public static final String ORGANIZER_LOGO_ALT = "organizerLogoAlt";
    }

    private UserDynamoDBMapper() {}

    public static @NotNull Map<String, AttributeValue> toMap(@NotNull User user) {
        Map<String, AttributeValue> stringAttributeValueMap = new HashMap<>(Map.of(
                ID, AttributeValue.builder().s(user.getId()).build(),
                USERNAME, AttributeValue.builder().s(user.getUsername()).build(),
                EMAIL, AttributeValue.builder().s(user.getEmail()).build(),
                PASSWORD, AttributeValue.builder().s(user.getPassword()).build(),
                ROLES, AttributeValue.builder().ss(user.getRole().stream().map(UserRole::name).toList()).build()
        ));

        if (user.getOrganizer().isPresent()) {
            Organizer organizer = user.getOrganizer().get();
            stringAttributeValueMap.putAll(Map.of(
                    ORGANIZER_ID, AttributeValue.builder().s(organizer.id()).build(),
                    ORGANIZER_NAME, AttributeValue.builder().s(organizer.name()).build(),
                    ORGANIZER_URL, AttributeValue.builder().s(organizer.url()).build()
            ));

            if (organizer.logo() != null) {
                stringAttributeValueMap.putAll(Map.of(
                        ORGANIZER_LOGO_ID, AttributeValue.builder().s(organizer.logo().id()).build(),
                        ORGANIZER_LOGO_URL, AttributeValue.builder().s(organizer.logo().url()).build(),
                        ORGANIZER_LOGO_ALT, AttributeValue.builder().s(organizer.logo().alt()).build()
                ));
            }
        }
        return stringAttributeValueMap;
    }

    public static @NotNull User fromMap(@NotNull Map<String, AttributeValue> map) {
        Organizer organizer = map.containsKey(ORGANIZER_ID)
                ? new Organizer(
                    map.get(ORGANIZER_ID).s(),
                    map.get(ORGANIZER_NAME).s(),
                    map.get(ORGANIZER_URL).s(),
                    new Image(
                            map.get(ORGANIZER_LOGO_ID).s(),
                            map.get(ORGANIZER_LOGO_URL).s(),
                            map.get(ORGANIZER_LOGO_ALT).s()
                    ))
                : null;
        return new User(
                map.get(ID).s(),
                map.get(USERNAME).s(),
                map.get(EMAIL).s(),
                map.get(PASSWORD).s(),
                map.get(ROLES).ss().stream().map(UserRole::valueOf).toList(),
                organizer
        );
    }
}
