package de.djuelg.vostore.storage.converter;


import com.fernandocejas.arrow.functions.Function;

import org.jetbrains.annotations.Nullable;

import de.djuelg.vostore.domain.model.todolist.TodoListHeader;
import de.djuelg.vostore.storage.model.TodoListHeaderDAO;

/**
 * Created by djuelg on 04.08.17.
 */

public class TodoListHeaderDAOConverter implements Function<TodoListHeaderDAO, TodoListHeader> {

    @Nullable
    @Override
    public TodoListHeader apply(TodoListHeaderDAO input) {
        return RealmConverter.convert(input);
    }
}
