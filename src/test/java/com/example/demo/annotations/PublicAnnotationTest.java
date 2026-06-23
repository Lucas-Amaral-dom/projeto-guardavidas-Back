package com.example.demo.annotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PublicAnnotationTest {

    @Test
    @DisplayName("Public annotation deve existir")
    void deveExistir() {
        // ARRANGE & ACT & ASSERT
        assertNotNull(Public.class, "Public annotation deve existir");
    }

    @Test
    @DisplayName("Public annotation deve ter RetentionPolicy RUNTIME")
    void deveTemRetentionPolicyRuntime() {
        // ARRANGE & ACT
        java.lang.annotation.Retention retention = Public.class.getAnnotation(java.lang.annotation.Retention.class);

        // ASSERT
        assertNotNull(retention);
        assertTrue(retention.value() == java.lang.annotation.RetentionPolicy.RUNTIME);
    }

    @Test
    @DisplayName("Public annotation deve ser aplicável a METHOD e TYPE")
    void deveSerAplicavelAMethodEType() {
        // ARRANGE & ACT
        java.lang.annotation.Target target = Public.class.getAnnotation(java.lang.annotation.Target.class);

        // ASSERT
        assertNotNull(target);
        boolean temMethod = false;
        boolean temType = false;
        for (java.lang.annotation.ElementType type : target.value()) {
            if (type == java.lang.annotation.ElementType.METHOD) temMethod = true;
            if (type == java.lang.annotation.ElementType.TYPE) temType = true;
        }
        assertTrue(temMethod && temType);
    }

    @Test
    @DisplayName("Public annotation deve ter PreAuthorize permitAll")
    void deveTemPreAuthorizePermitAll() {
        // ARRANGE & ACT
        org.springframework.security.access.prepost.PreAuthorize preAuth = 
            Public.class.getAnnotation(org.springframework.security.access.prepost.PreAuthorize.class);

        // ASSERT
        assertNotNull(preAuth);
        assertTrue(preAuth.value().contains("permitAll"));
    }
}
