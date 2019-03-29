package in.SingAvi.complaintapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ComplainAdapter extends RecyclerView.Adapter<ComplainAdapter.MyViewHolder> {

    private List<String> name,title,description,status,department,date;
    Activity activity;



    public ComplainAdapter(List<String> myName,List<String> myTitle, List<String> myDescription, List<String> myStatus,List<String> myDepartment,List<String> myDete) {
        name = myName;
        title = myTitle;
        description = myDescription;
        status = myStatus;
        department = myDepartment;
        date = myDete;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(
                viewGroup.getContext());
        View view= inflater.inflate(R.layout.complain_layout,viewGroup,false);
        MyViewHolder holder =new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        holder.txtName.setText( " "+name.get(i));
        holder.txtTitle.setText( title.get(i));
        holder.txtDescription.setText( description.get(i));
        holder.txtStatus.setText(status.get(i));
        holder.txtDepartment.setText(department.get(i));
        holder.txtDate.setText(date.get(i));


    }

    public Context getActivity()
    {
        return activity;
    }

    @Override
    public int getItemCount() {

        return  name.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {


        public TextView txtName,txtTitle,txtDescription,txtStatus,txtDepartment,txtDate;
        public View layout;


        public MyViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.byName);
            txtTitle = itemView.findViewById(R.id.title);
            txtDescription = itemView.findViewById(R.id.description);
            txtStatus = itemView.findViewById(R.id.status);
            txtDepartment = itemView.findViewById(R.id.department);
            txtDate = itemView.findViewById(R.id.date);

            itemView.setClickable(false);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
