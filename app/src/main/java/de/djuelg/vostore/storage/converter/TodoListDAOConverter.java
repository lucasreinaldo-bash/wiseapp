package de.djuelg.vostore.storage.converter;


import com.fernandocejas.arrow.functions.Function;

import org.jetbrains.annotations.Nullable;

import de.djuelg.vostore.domain.model.preview.TodoList;
import de.djuelg.vostore.storage.model.TodoListDAO;

/**
 * Created by djuelg on 04.08.17.
 */

public class TodoListDAOConverter implements Function<TodoListDAO, TodoList> {

    @Nullable
    @Override
    public TodoList apply(TodoListDAO input) {
        return RealmConverter.convert(input);
    }
}
