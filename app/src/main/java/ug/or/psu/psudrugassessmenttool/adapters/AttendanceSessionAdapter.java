package ug.or.psu.psudrugassessmenttool.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.models.AttendanceSession;

public class AttendanceSessionAdapter extends RecyclerView.Adapter<AttendanceSessionAdapter.MyViewHolder> {
    private List<AttendanceSession> attendanceList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView session_date, session_time_in, session_time_out, session_duration;

        MyViewHolder(View view) {
            super(view);
            session_date = view.findViewById(R.id.session_date);
            session_time_in = view.findViewById(R.id.session_time_in);
            session_time_out = view.findViewById(R.id.session_time_out);
            session_duration = view.findViewById(R.id.session_duration);
        }
    }

    public AttendanceSessionAdapter(List<AttendanceSession> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_session_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final AttendanceSession attendanceSession = attendanceList.get(position);

        //get words from date
        String startDateString = attendanceSession.getDate();
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        // Convert from String to Date
        Date date = null;

        try {
            date = dateFormat.parse(startDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Converting to String again, using an alternative format
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat1 = new SimpleDateFormat("E, MMM dd yyyy");

        holder.session_date.setText(dateFormat1.format(date));

        //get time from the timestamps and post
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String time_in = formatter.format(new Date(Long.parseLong(attendanceSession.getTime_in())));
        String time_out = formatter.format(new Date(Long.parseLong(attendanceSession.getTime_out())));

        holder.session_time_in.setText(time_in);
        holder.session_time_out.setText(time_out);

        //get the duration
        Long milliseconds = Long.parseLong(attendanceSession.getDuration());
        //int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);

        String duration = String.valueOf(hours) + " hours and " + String.valueOf(minutes) + " minutes";
        holder.session_duration.setText(duration);
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }
}
