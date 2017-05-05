package com.example.Controllers;

import com.example.Models.User;
import com.example.Repositories.PostsRepo;
import com.example.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by baraa on 2/20/2017.
 */
@Controller
public class Home {

    PostsRepo postsRepo;

    @RequestMapping(value = "/test")
    public String test()
    {
        return "hello";
    }

    @RequestMapping({"home","/"})
    public String home(Model model,@SessionAttribute(required = false) User user)
    {
        if(user!=null) {
            model.addAttribute("user", user);
            model.addAttribute("photo","/users/"+user.getUserName()+".png");
            model.addAttribute("profile","/users/"+user.getUserName());
            model.addAttribute("logged","display:inline");
            model.addAttribute("notLogged","display:none");
        }
        else {
            model.addAttribute("logged","display:none");
        }
        return "home";
    }

    @RequestMapping(value = "/About")
    public String about()
    {
        return "about";
    }


    @RequestMapping("/blog")
    public String blog (Model model){
        model.addAttribute("posts",postsRepo.findAll());
        return "blog";
    }


    @Autowired
    public void setPostsRepo(PostsRepo postsRepo) {
        this.postsRepo = postsRepo;
    }
}
