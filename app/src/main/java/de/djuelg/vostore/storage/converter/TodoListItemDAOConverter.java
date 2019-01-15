package de.djuelg.vostore.storage.converter;


import com.fernandocejas.arrow.functions.Function;

import org.jetbrains.annotations.Nullable;

import de.djuelg.vostore.domain.model.todolist.TodoListItem;
import de.djuelg.vostore.storage.model.TodoListItemDAO;

/**
 * Created by djuelg on 04.08.17.
 */

public class TodoListItemDAOConverter implements Function<TodoListItemDAO, TodoListItem> {

    @Nullable
    @Override
    public TodoListItem apply(TodoListItemDAO input) {
        return RealmConverter.convert(input);
    }
}
