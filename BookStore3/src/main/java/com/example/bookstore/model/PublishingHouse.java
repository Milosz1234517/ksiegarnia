package com.example.bookstore.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Publishing_House", schema = "public", catalog = "BookStore")
public class PublishingHouse {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "publishing_house_id", nullable = false)
    private int publishingHouseId;

    @Basic
    @Column(name = "name", nullable = false, length = 15)
    private String name;

    @Basic
    @Column(name = "icon", length = 15)
    private String icon;

}
