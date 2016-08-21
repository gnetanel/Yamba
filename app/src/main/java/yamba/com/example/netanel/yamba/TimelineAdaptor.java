package yamba.com.example.netanel.yamba;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class TimelineAdaptor extends SimpleCursorAdapter{
    static String[] FROM = {StatusData.C_USER, StatusData.C_CREATED_AT, StatusData.C_TEXT};
    static int[] TO = {R.id.textUser, R.id.textCreatedAt, R.id.textText};

    public TimelineAdaptor(Context context, Cursor c) {
        super (context, R.layout.row, c, FROM, TO);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        TextView textViewCreateAt = (TextView)view.findViewById(R.id.textCreatedAt);
        long createdAt = cursor.getLong(cursor.getColumnIndex(StatusData.C_CREATED_AT));
        textViewCreateAt.setText(DateUtils.getRelativeTimeSpanString(createdAt));
    }
}
