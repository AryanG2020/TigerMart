package com.example.tigermartdepauw.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.tigermartdepauw.R;
import com.example.tigermartdepauw.activities.ShowAllActivity;
import com.example.tigermartdepauw.adapters.CategoryAdapter;
import com.example.tigermartdepauw.adapters.NewProductsAdapter;
import com.example.tigermartdepauw.adapters.PopularProductAdapter;
import com.example.tigermartdepauw.models.CategoryModel;
import com.example.tigermartdepauw.models.NewProductModel;
import com.example.tigermartdepauw.models.PopularProductsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    RecyclerView catRecyclerView, newProductRecyclerView, popularRecyclerView;
    //Category recyclerview
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    TextView catShowAll, popularShowAll, newProductShowAll;


    //New Product Recycler View
    NewProductsAdapter newProductsAdapter;
    List<NewProductModel> newProductModelList;



    //popular products
    PopularProductAdapter popularProductAdapter;
    List<PopularProductsModel> popularProductsModelList;

    //Firestore
    FirebaseFirestore db;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseFirestore.getInstance();
        catRecyclerView=root.findViewById(R.id.rec_category);
        newProductRecyclerView=root.findViewById(R.id.new_product_rec);
        popularRecyclerView=root.findViewById(R.id.popular_rec);

        catShowAll=root.findViewById(R.id.category_see_all);
        popularShowAll=root.findViewById(R.id.popular_see_all);
        newProductShowAll=root.findViewById(R.id.newProducts_see_all);

        catShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });

        popularShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });

        newProductShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });




    //image slider
        ImageSlider imageSlider= root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels= new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.depauwbanner,"Welcome To TigerMart", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner,"Black Friday Deal", ScaleTypes.CENTER_CROP));
       // slideModels.add(new SlideModel(R.drawable.banner3,"70% Off", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels);


                //Category
        catRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
       categoryModelList= new ArrayList<>();
       categoryAdapter= new CategoryAdapter(getContext(), categoryModelList);
       catRecyclerView.setAdapter(categoryAdapter);

        db.collection(  "Category").get()
                .addOnCompleteListener (new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CategoryModel categoryModel = document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                                categoryAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(),""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //New  Products

        newProductRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        newProductModelList=new ArrayList<>();
        newProductsAdapter = new NewProductsAdapter(getContext () , newProductModelList);
        newProductRecyclerView.setAdapter(newProductsAdapter);


        db.collection(  "NewProducts").get()
                .addOnCompleteListener (new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NewProductModel newProductModel= document.toObject(NewProductModel.class);
                                newProductModelList.add(newProductModel);
                                newProductsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(),""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //popular products
        popularRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        popularProductsModelList=new ArrayList<>();
        popularProductAdapter = new PopularProductAdapter(getContext () , popularProductsModelList);
        popularRecyclerView.setAdapter(popularProductAdapter);

        db.collection(  "AllProducts").get()
                .addOnCompleteListener (new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PopularProductsModel popularProductsModel= document.toObject(PopularProductsModel.class);
                                popularProductsModelList.add(popularProductsModel);
                                popularProductAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(),""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return root;
    }
}

































