package com.ahmedsameha1.todo.domain_model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class TodoTest {
    private Todo todo;

    @BeforeEach
    public void before() {
        todo = new Todo();
    }

    @Test
    public void testDefaultValueOfIsDone() {
        assertThat(todo.isDone()).isFalse();
    }
}
