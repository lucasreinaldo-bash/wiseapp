package de.djuelg.vostore.presentation.ui.flexibleadapter;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.djuelg.vostore.R;
import de.djuelg.vostore.domain.comparator.PreviewCompareable;
import de.djuelg.vostore.domain.model.preview.Note;
import de.djuelg.vostore.domain.model.preview.Preview;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

import static android.view.View.GONE;

/**
 * Created by djuelg on 20.07.17.
 */

public class PreviewViewModel extends AbstractFlexibleItem<PreviewViewModel.ViewHolder> implements PreviewCompareable {

    private final Preview preview;

    public PreviewViewModel(Preview preview) {
        this.preview = Objects.requireNonNull(preview);
    }

    public Preview getPreview() {
        return preview;
    }

    public String getUuid() {
        return preview.getBaseItem().getUuid();
    }

    public String getTitle() {
        return preview.getBaseItem().getTitle();
    }

    public Date getChangedAt() {
        return  preview.getBaseItem().getChangedAt();
    }

    public Date getCreatedAt() {
        return preview.getBaseItem().getCreatedAt();
    }

    public long getImportance() {
        return  preview.calculateImportance();
    }

    @Override
    public boolean isSwipeable() {
        return true;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.todo_list_preview;
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List payloads) {
        DateFormat date = SimpleDateFormat.getDateInstance();
        DateFormat time = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);

        String title = holder.getFrontView().getResources().getString(R.string.preview_title, getTitle(), getImportance());
        //holder.title.setText(getTitle());
        holder.title.setText(title);
        holder.lastChange.setText(DateUtils.isToday(getChangedAt().getTime())
                ? time.format(getChangedAt())
                : date.format(getChangedAt()));
        holder.header.setText(preview.getSubtitle());
        holder.items.setText(preview.getDetails());

        if (preview.getBaseItem() instanceof Note) {
            holder.header.setVisibility(GONE);
            holder.typeIcon.setImageResource(R.drawable.ic_note_gray_22dp);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PreviewViewModel that = (PreviewViewModel) o;
        return Objects.equals(preview, that.preview);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preview);
    }

    @Override
    public String toString() {
        return getTitle();
    }

    /**
     * Needed ViewHolder
     */
    class ViewHolder extends FlexibleViewHolder {

        @BindView(R.id.front_view) View frontView;
        @BindView(R.id.rear_left_view) View rearLeftView;
        @BindView(R.id.rear_right_view) View rearRightView;
        @BindView(R.id.title_preview) TextView title;
        @BindView(R.id.last_change_preview) TextView lastChange;
        @BindView(R.id.header_preview) TextView header;
        @BindView(R.id.items_preview) TextView items;
        @BindView(R.id.type_icon) ImageView typeIcon;

        ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ViewHolder)) return false;
            ViewHolder that = (ViewHolder) o;
            return Objects.equals(frontView, that.frontView) &&
                    Objects.equals(rearLeftView, that.rearLeftView) &&
                    Objects.equals(rearRightView, that.rearRightView) &&
                    Objects.equals(title, that.title) &&
                    Objects.equals(lastChange, that.lastChange) &&
                    Objects.equals(header, that.header) &&
                    Objects.equals(items, that.items) &&
                    Objects.equals(typeIcon, that.typeIcon);
        }

        @Override
        public int hashCode() {
            return Objects.hash(frontView, rearLeftView, rearRightView, title, lastChange, header, items, typeIcon);
        }
    }
}
