package com.example.demo.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BaseEntityTest {

    // Subclasse concreta apenas para testar a classe abstrata BaseEntity
    static class BaseEntityImpl extends BaseEntity {}

    @Test
    @DisplayName("BaseEntity deve ter campo ativo como true por padrão")
    void deveAtivoSerTruePorPadrao() {
        BaseEntityImpl entity = new BaseEntityImpl();
        assertTrue(entity.isAtivo(), "O campo ativo deve ser true por padrão");
    }

    @Test
    @DisplayName("BaseEntity deve ter ID nulo antes de persistir")
    void deveIdSerNuloAntesDePersirir() {
        BaseEntityImpl entity = new BaseEntityImpl();
        assertNull(entity.getId(), "O ID deve ser nulo antes da persistência");
    }
}
