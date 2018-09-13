package com.android.zakaria.classmateinfo.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zakaria.classmateinfo.activities.DetailsActivity;
import com.android.zakaria.classmateinfo.models.StudentInfo;
import com.android.zakaria.classmateinfo.R;

import java.util.List;

public class StudentAdapterGridView extends RecyclerView.Adapter<StudentAdapterGridView.StudentViewHolder> {

    private Context context;
    private List<StudentInfo> studentInfoList;

    public StudentAdapterGridView(Context context, List<StudentInfo> studentInfoList) {
        this.context = context;
        this.studentInfoList = studentInfoList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_view_info, null);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        final StudentInfo studentInfo = studentInfoList.get(position);
        holder.name.setText(studentInfo.getName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(studentInfo.getImage(), null));
        } else {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(studentInfo.getImage()));
        }

        /*onclick listener*/
        holder.relativeLayoutGridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("name_key", studentInfo.getName());
                intent.putExtra("id_key", studentInfo.getId());
                intent.putExtra("email_key", studentInfo.getEmail());
                intent.putExtra("phone_key", studentInfo.getPhone());
                intent.putExtra("image_key", studentInfo.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentInfoList.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView name, id;
        private RelativeLayout relativeLayoutGridView;

        public StudentViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.texViewName);

            relativeLayoutGridView = itemView.findViewById(R.id.gridViewItemRL);
        }
    }
}
