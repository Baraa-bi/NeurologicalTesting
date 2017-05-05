package com.example.Controllers;

import com.example.Models.User;
import com.example.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created by baraa on 2/24/2017.
 */
@Controller
public class Login {

    private UserRepo userRepo;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(Model model,@SessionAttribute(required = false) User user)
    {
        if(user!=null)
            return "redirect:/users/"+user.getUserName();
        model.addAttribute("user",new User());
        return "login";
    }


    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(Model model,@Valid @ModelAttribute User user, BindingResult result, HttpSession session)
    {
        if(result.hasErrors())
        {
            for(FieldError error :result.getFieldErrors())
                model.addAttribute(error.getField(),error.getDefaultMessage());


             model.addAttribute("user",user);
            return "login";
        }
        else {
            User user1 = userRepo.findByUserName(user.getUserName());
            if (user1 == null || !user.getPassword().equals(user1.getPassword())) {
                model.addAttribute("userName", "incorrect userName or Password");
                return "login";
            }
            else
            {
                session.setAttribute("user",user1);
                return "redirect:/users/";
            }
        }
    }

    @RequestMapping(value = "/logout",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String logout(Model model,@SessionAttribute("user")User user, HttpSession session){
        session.removeAttribute("user");
        model.addAttribute("user",new User());
        model.addAttribute("logged","display:none");
        model.addAttribute("notLogged","display:inline");
        return "redirect:/home";
    }

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
}
