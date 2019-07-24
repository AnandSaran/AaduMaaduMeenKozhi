package com.aadu_maadu_kozhi.gregantech.view.iview;

import com.aadu_maadu_kozhi.gregantech.adapter.CreatePostImageAdapter;
import com.aadu_maadu_kozhi.gregantech.model.pojo.UserPost;

/**
 * Created by Anand on 8/27/2017.
 */

public interface ICreatePostActivityView extends IView {


    void setAdapter(CreatePostImageAdapter createPostImageAdapter);

    void updateProgress(double v);

    String getPostText();

    void setUploadSucess(UserPost userPost);

    void setUserPostTitile();

    void setPostContent(String post_content);
}
