package io.teiler.api.service;

import static spark.Spark.awaitInitialization;

import com.google.gson.Gson;
import io.teiler.server.dto.Group;
import io.teiler.server.persistence.repositories.GroupRepository;
import io.teiler.server.util.AuthorizationChecker;
import io.teiler.server.util.exceptions.NotAuthorizedException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spark.Request;

@Service
public class GroupService {
    // Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);
    // Instance objects, potentially expensive ones you only need once
    private SecureRandom random = new SecureRandom();
    private Gson gson = new Gson();
    // Spark Components (Services/Controller)
    @Autowired
    private AuthorizationChecker authorizationChecker;
    @Autowired
    private GroupRepository groupRepository;

    // Constants
    private static final String GROUP_ID_HEADER = "X-Teiler-GroupID";
    private static final int NUMBER_OF_ID_CHARACTERS = 8;
    // Mathematical fact, don't change it
    private static final int ENTROPY_BITS_IN_ONE_CHARACTER = 5;

    public String handleGet(Request req) throws NotAuthorizedException {
        awaitInitialization();

        String uuid = req.headers(GROUP_ID_HEADER);
        authorizationChecker.checkAuthorization(uuid);
        LOGGER.debug("Request with group ID: " + uuid);

        Group responseGroup = new Group(groupRepository.get(uuid));

        return gson.toJson(responseGroup);
    }

    public String handlePost(Request req) {
        awaitInitialization();

        Group requestGroup = gson.fromJson(req.body(), Group.class);

        requestGroup.setUuid(createNewUuid());
        LOGGER.debug("New Group: " + requestGroup.getName() + ", " + requestGroup.getUuid());

        Group responseGroup = new Group(
            groupRepository.create(requestGroup.getUuid(), requestGroup.getName()));

        return gson.toJson(responseGroup);
    }

    // this needs to go somewhere else
    private String createNewUuid() {
        String uuid;
        List<String> allIds = groupRepository.getAllIds();
        // reasoning: http://stackoverflow.com/a/41156
        do {
            uuid = new BigInteger(NUMBER_OF_ID_CHARACTERS * ENTROPY_BITS_IN_ONE_CHARACTER, random).toString(32);
        } while (allIds.contains(uuid));
        return uuid;
    }
}
