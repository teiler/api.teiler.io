package io.teiler.server.persistence.repositories;

import java.util.List;

import io.teiler.server.persistence.entities.DummyEntity;


public interface DummyRepository {

    /**
     * Get all Dummy-Entities.
     * 
     * @return List of {@link DummyEntity}
     */
    List<DummyEntity> getAll();

}
