package com.example.demo.annotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.Annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AdminAnnotationTest {

    @Test
    @DisplayName("Admin annotation deve existir")
    void deveExistir() {
        // ARRANGE & ACT & ASSERT
        assertNotNull(Admin.class, "Admin annotation deve existir");
    }

    @Test
    @DisplayName("Admin annotation deve ter RetentionPolicy RUNTIME")
    void deveTemRetentionPolicyRuntime() {
        // ARRANGE & ACT
        java.lang.annotation.Retention retention = Admin.class.getAnnotation(java.lang.annotation.Retention.class);

        // ASSERT
        assertNotNull(retention);
        assertTrue(retention.value() == java.lang.annotation.RetentionPolicy.RUNTIME);
    }

    @Test
    @DisplayName("Admin annotation deve ser aplicável a METHOD")
    void deveSerAplicavelAMethod() {
        // ARRANGE & ACT
        java.lang.annotation.Target target = Admin.class.getAnnotation(java.lang.annotation.Target.class);

        // ASSERT
        assertNotNull(target);
        boolean temMethod = false;
        for (java.lang.annotation.ElementType type : target.value()) {
            if (type == java.lang.annotation.ElementType.METHOD) {
                temMethod = true;
            }
        }
        assertTrue(temMethod);
    }

    @Test
    @DisplayName("Admin annotation deve ter PreAuthorize")
    void deveTemPreAuthorize() {
        // ARRANGE & ACT
        org.springframework.security.access.prepost.PreAuthorize preAuth = 
            Admin.class.getAnnotation(org.springframework.security.access.prepost.PreAuthorize.class);

        // ASSERT
        assertNotNull(preAuth);
        assertTrue(preAuth.value().contains("ADMIN"));
    }
}
