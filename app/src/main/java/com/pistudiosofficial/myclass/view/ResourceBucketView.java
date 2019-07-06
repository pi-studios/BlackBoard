package com.pistudiosofficial.myclass.view;

import com.pistudiosofficial.myclass.objects.ResourceBucketObject;

import java.util.ArrayList;

public interface ResourceBucketView {


    void uploadSuccess();
    void loadSuccess(ArrayList<ResourceBucketObject> bucketObjects);

}
