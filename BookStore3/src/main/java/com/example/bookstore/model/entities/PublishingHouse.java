package com.example.bookstore.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Publishing_House", schema = "public", catalog = "BookStore")
public class PublishingHouse {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publishing_house_id", nullable = false)
    private Integer publishingHouseId;

    @Basic
    @Column(name = "name", length = 15, unique = true)
    private String name;

}
