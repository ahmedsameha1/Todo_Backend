package com.ahmedsameha1.todo.domain_model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TodoTest {
    private Todo todo;

    @BeforeEach
    public void before() {
        todo = new Todo();
    }

    @Test
    public void testDefaultValueOfIsDone() {
        Assertions.assertThat(todo.isDone()).isFalse();
    }
}
