package io.teiler.server.persistence.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;

import io.teiler.server.persistence.entities.DummyEntity;
import io.teiler.server.persistence.entities.QDummyEntity;

@Repository
public class DummyRepositoryImpl implements DummyRepository {

    @Autowired
    private EntityManager entityManager;
    
    @Override
    public List<DummyEntity> getAll() {
        return new JPAQuery<DummyEntity>(entityManager).from(QDummyEntity.dummyEntity).fetch();
    }
    
}
