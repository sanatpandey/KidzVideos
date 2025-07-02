package com.kidztube.userservice.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "app_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    @Column
    val name: String,

    @Column(unique = true)
    val email: String,

    @Column
    var password: String,

    @Column
    var video_ids: String = "",

    @Column
    val ageGroup: String,

    @Column
    val role: String
)
