package com.example.realitycheck;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.realitycheck.adapter.PostAdapter;
import com.example.realitycheck.bean.PostBean;
import com.example.realitycheck.databinding.ActivityPostBinding;
import com.example.realitycheck.util.LinearLayoutDivider;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zackratos.ultimatebarx.ultimatebarx.java.Operator;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

import java.util.Date;

public class PostActivity extends Fragment {

    public static ActivityPostBinding binding;
    public static PostAdapter postAdapter;

    public FirebaseFirestore fStorage;
    public PostAdapter getPostAdapter(){
        return this.postAdapter;
    }



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding  =  ActivityPostBinding.inflate(inflater, container, false);


        MainActivity.toolbar.hide();
        FloatingActionButton myFab = binding.getRoot().findViewById(R.id.fab);
        myFab.show();
        fStorage = FirebaseFirestore.getInstance();
        initData();

        BottomNavigationView bottomNavigationView = binding.getRoot().findViewById(R.id.bnav_post_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.post_search:
                        NavHostFragment.findNavController(PostActivity.this)
                                .navigate(R.id.action_PostActivity_to_SearchActivity);
                        break;
                    case R.id.post_home:
                        //
                        break;
                    case R.id.post_notification:
                        //
                        break;
                    case R.id.post_message:
                        //
                        break;
                }
                return true;
            }
        });

        ImageView imageView = binding.getRoot().findViewById(R.id.shapeableImageView);
        Glide.with(this.getContext())
                .load(LoginPage.storageProfilePictureReference)
                .into(imageView);

        binding.getRoot().findViewById(R.id.shapeableImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(PostActivity.this)
                        .navigate(R.id.action_PostActivity_to_ProfileActivity);
            }
        });
        return binding.getRoot();
    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Operator operator = UltimateBarX.statusBar(this);
        operator.fitWindow(true);
        operator.light(false);
        operator.color(Color.BLACK);
        operator.apply();



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    private void initData() {
        postAdapter = new PostAdapter(this.getContext());
        StringBuilder title = new StringBuilder();
        StringBuilder content = new StringBuilder();
        binding.getRoot().findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                Post p = new TextPost(FirebaseAuth.getInstance().getCurrentUser().getUid(),currentDateTimeString);
                binding.rlPostBox.getLayoutManager().scrollToPosition(0);
                p.createPost();

            }
        });
        //layout post adapter
        binding.rlPostBox.setAdapter(postAdapter);
        binding.rlPostBox.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.rlPostBox.setHasFixedSize(true);
        binding.rlPostBox.addItemDecoration(new LinearLayoutDivider(this.getContext(), LinearLayoutManager.VERTICAL));


    }




    private void addTempData(String title, String content) {
        PostBean postBean = new PostBean();
        postBean.setTitle(title);
        postBean.setContent(content);
        postBean.setDescription(title + "re-post");
        postAdapter.addData(postBean);
    }


}