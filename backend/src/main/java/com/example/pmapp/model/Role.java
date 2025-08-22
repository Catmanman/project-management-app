package com.example.pmapp.model;

/**
 * Enum representing user roles within the system.  Roles can be
 * leveraged by Spring Security to restrict access to certain endpoints.
 */
public enum Role {
    USER,
    ADMIN
}