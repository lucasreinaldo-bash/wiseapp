package de.djuelg.vostore.domain.interactors.todolist;


import java.util.List;

import de.djuelg.vostore.domain.interactors.base.Interactor;
import de.djuelg.vostore.domain.model.todolist.TodoListSection;


public interface DisplayTodoListInteractor extends Interactor {

    interface Callback {
        void onTodoListRetrieved(List<TodoListSection> sections);

        void onInvalidTodoListUuid();
    }
}
