package org.nikola.velemir.poshtar.spring.adapter.repository;

import org.nikola.velemir.poshtar.spring.adapter.model.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {

    TestEntity findByData(String data);
}
