package de.djuelg.vostore.presentation.ui.flexibleadapter;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.djuelg.vostore.R;
import de.djuelg.vostore.domain.model.todolist.TodoListItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractSectionableItem;
import eu.davidea.viewholders.FlexibleViewHolder;

import static de.djuelg.vostore.presentation.ui.dialog.BaseDialogs.showHtmlDialog;

/**
 * Created by djuelg on 20.07.17.
 */

public class TodoListItemViewModel extends AbstractSectionableItem<TodoListItemViewModel.ViewHolder, TodoListHeaderViewModel> {

    private final TodoListItem item;

    public TodoListItemViewModel(TodoListHeaderViewModel headerVM, TodoListItem item) {
        super(headerVM);
        this.item = Objects.requireNonNull(item);
    }

    public TodoListItem getItem() {
        return item;
    }

    @Override
    public boolean isSwipeable() {
        return true;
    }

    @Override
    public boolean isDraggable() {
        return true;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.todo_list_item;
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List payloads) {
        holder.title.setText(item.getTitle());
        displayIfItemIsImportant(holder);
        displayIfItemHasDetails(holder);
        displayIfItemIsDone(holder);
    }

    private void displayIfItemIsDone(ViewHolder holder) {
        if (item.isDone()) {
            holder.title.setTextColor(holder.getFrontView().getResources().getColor(R.color.light_gray));
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.details.setImageResource(R.drawable.ic_lightbulb_outline_light_gray_24dp);
        } else {
            holder.title.setPaintFlags(holder.title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    private void displayIfItemIsImportant(ViewHolder holder) {
        if (item.isImportant()) {
            holder.title.setTextColor(holder.getFrontView().getResources().getColor(R.color.colorPrimary));
            holder.title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            holder.details.setImageResource(R.drawable.ic_lightbulb_outline_primary_24dp);
        } else {
            holder.title.setTextColor(holder.getFrontView().getResources().getColor(R.color.dark_gray));
            holder.title.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            holder.details.setImageResource(R.drawable.ic_lightbulb_outline_gray_24dp);
        }
    }

    private void displayIfItemHasDetails(ViewHolder holder) {
        if (item.getDetails().isEmpty()) {
            holder.details.setVisibility(View.GONE);
        } else {
            holder.details.setVisibility(View.VISIBLE);
            holder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showHtmlDialog(view.getContext(), item.getTitle(), item.getDetails());
                }
            });
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoListItemViewModel)) return false;
        TodoListItemViewModel that = (TodoListItemViewModel) o;
        return Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item);
    }

    @Override
    public String toString() {
        return item.getTitle();
    }

    /**
     * Needed ViewHolder
     */
    // TODO Update view on drag
    public class ViewHolder extends FlexibleViewHolder {

        @BindView(R.id.front_view) View frontView;
        @BindView(R.id.rear_left_view) View rearLeftView;
        @BindView(R.id.rear_right_view) View rearRightView;
        @BindView(R.id.todo_list_item_title) TextView title;
        @BindView(R.id.todo_list_item_details) ImageView details;

        ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        @Override
        public boolean onLongClick(View view) {
            boolean isValid = super.onLongClick(view);
            title.setTextColor(getFrontView().getResources().getColor(android.R.color.black));
            frontView.setBackgroundColor(getFrontView().getResources().getColor(R.color.light_gray));
            return isValid;
        }

        @Override
        public void onItemReleased(int position) {
            super.onItemReleased(position);
            title.setTextColor(getFrontView().getResources().getColor(R.color.dark_gray));
            frontView.setBackgroundColor(getFrontView().getResources().getColor(android.R.color.white));
        }

        @Override
        protected boolean shouldActivateViewWhileSwiping() {
            return true;
        }

        @Override
        public float getActivationElevation() {
            return 8f;
        }

        @Override
        public View getFrontView() {
            return frontView;
        }

        @Override
        public View getRearLeftView() {
            return rearLeftView;
        }

        @Override
        public View getRearRightView() {
            return rearRightView;
        }
    }
}
