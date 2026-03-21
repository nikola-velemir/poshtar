package org.nikola.velemir.poshtar.spring.adapter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "test_entities")
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String data;

    public TestEntity(){}

    public TestEntity(String data){
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TestEntity{" +
                "id=" + id +
                ", data='" + data + '\'' +
                '}';
    }
}
