package com.example.Controllers;

import com.example.Models.User;
import com.example.Repositories.CaseRepo;
import com.example.Repositories.PostsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * Created by baraa on 4/15/2017.
 */
@ControllerAdvice
public class Advice {

    CaseRepo caseRepo;
    PostsRepo postsRepo;


    @ModelAttribute
    public void hi(Model model, @SessionAttribute(required = false)User user) {
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("photo", "/users/" + user.getUserName() + ".png");
            model.addAttribute("profile", "/users/" + user.getUserName());
            model.addAttribute("logged", "display:inline");
            model.addAttribute("notLogged", "display:none");
            model.addAttribute("cases", caseRepo.findBycreatedBy(user.getId()));
            model.addAttribute("posts", postsRepo.findBycreatedBy(user.getId()));
            model.addAttribute("newCaseButton","/users/"+user.getUserName()+"/createCase");
            model.addAttribute("newPostButton","/users/"+user.getUserName()+"/createPost");
            model.addAttribute("newCase","/users/"+user.getUserName()+"/createCase");
            model.addAttribute("newPost","/users/"+user.getUserName()+"/createPost");
        }
        else
            model.addAttribute("user",new User());
    }

    @Autowired
    public void setCaseRepo(CaseRepo caseRepo) {
        this.caseRepo = caseRepo;
    }

    @Autowired
    public void setPostsRepo(PostsRepo postsRepo) {
        this.postsRepo = postsRepo;
    }
}
