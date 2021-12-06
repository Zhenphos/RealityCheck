package com.example.realitycheck.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.realitycheck.Comment;
import com.example.realitycheck.Group;
import com.example.realitycheck.LoginPage;
import com.example.realitycheck.R;
import com.example.realitycheck.SettingsPage;
import com.example.realitycheck.User;
import com.example.realitycheck.databinding.ItemCommentBinding;
import com.example.realitycheck.databinding.ItemGroupBinding;
import com.example.realitycheck.otherUserProfileActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private Context context;

    public Uri postAuthorProfilePhoto;

    public ArrayList<Group> groupList;
    public static Group group;
    public String author;
    public String content;
    public static String thisPage;
    public String date;
    public FirebaseFirestore fStore;

    public static User userToNavTo;


    public GroupAdapter(Context context, ArrayList<Group> group) {
        this.context = context;
        this.groupList = group;
    }

    @NonNull
    @Override
    public GroupAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGroupBinding binding = ItemGroupBinding.inflate(LayoutInflater.from(context), parent, false);
        GroupAdapter.GroupViewHolder groupViewHolder = new GroupAdapter.GroupViewHolder(binding);
        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupViewHolder holder, int position) {

        //sorts list of post based on post date
        //need to convert String postDate to dateTime in order to compare accurately
        Collections.sort(groupList, new Comparator<Group>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public int compare(Group group, Group t1) {
                return(group.groupName.compareTo(t1.groupName));

            }
        });
        //sets in order of most recent
        Collections.reverse(groupList);



        //gets current post from the recycler view list based its position
        group = groupList.get(position);
        holder.binding.tvTitle.setText(group.groupName);
        holder.binding.tvDescription.setText(group.bio);
        holder.binding.ivMore.setText(group.size + " members");
        if(group.members.contains(LoginPage.currUser.username)){
            holder.binding.buttonJoin.setText("Leave");
        }
        if(!group.members.contains(LoginPage.currUser.username)){
            holder.binding.buttonJoin.setText("Join");


        }
        if(group.members.contains(LoginPage.currUser.username)){
            holder.binding.buttonJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    group = groupList.get(position);
                    new AlertDialog.Builder(context)
                            .setTitle("Leave Group")
                            .setMessage("Are you sure you want to leave the group: "+ group.groupName+"?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    group.members.remove(LoginPage.currUser.username);
                                    group.size = group.members.size();
                                    holder.binding.ivMore.setText(group.size + " members");
                                    holder.binding.buttonJoin.setText("Join");
                                    Toast.makeText(GroupAdapter.this.context, ("Left " + group.groupName), Toast.LENGTH_SHORT).show();
                                    FirebaseFirestore.getInstance().collection("Groups").document(group.groupName).update("members", group.members);
                                    FirebaseFirestore.getInstance().collection("Groups").document(group.groupName).update("size", group.size);

                                    if(thisPage == "profile"){
                                        groupList.remove(group);
                                        notifyDataSetChanged();
                                    }
                                }
                            }).setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        }
        else if(!group.members.contains(LoginPage.currUser.username)){
            holder.binding.buttonJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    group = groupList.get(position);
                    group.members.add(LoginPage.currUser.username);
                    group.size = group.members.size();
                    holder.binding.ivMore.setText(group.size + " members");
                    holder.binding.buttonJoin.setText("Leave");
                    Toast.makeText(GroupAdapter.this.context, ("Joined" + group.groupName), Toast.LENGTH_SHORT).show();
                    FirebaseFirestore.getInstance().collection("Groups").document(group.groupName).update("members", group.members);
                    FirebaseFirestore.getInstance().collection("Groups").document(group.groupName).update("size", group.size);
                }
            });

        }




    }



    //adds new comment
    public void addData(Group newItem) {
        groupList.add(0,newItem);
        notifyItemInserted(0);
    }







    @Override
    public int getItemCount() {
        return groupList.size();
    }


    class GroupViewHolder extends RecyclerView.ViewHolder {
        ItemGroupBinding binding;



        public GroupViewHolder (ItemGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
    }
}