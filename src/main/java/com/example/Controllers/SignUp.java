package com.example.Controllers;


import com.example.Models.File;
import com.example.Models.User;
import com.example.Repositories.FileRepo;
import com.example.Repositories.UserRepo;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class SignUp {


    private UserRepo userRepo;
    private FileRepo fileRepo;


    @RequestMapping(value = "/signUp" , method = RequestMethod.GET)
    public String signUp(HttpSession session)
    {
        if (session.getAttribute("user")!=null)
            session.removeAttribute("user");
        return "signUp";
    }


    @RequestMapping(value = "/signUp" , method = RequestMethod.POST)
    public String signUp(Model model, @Valid @ModelAttribute User user, BindingResult result, HttpServletResponse response, HttpSession session, @RequestParam MultipartFile file) throws IOException {

        byte hasError=0;
        if(result.hasErrors())
        {
                for(FieldError error :result.getFieldErrors())
                    model.addAttribute(error.getField(),error.getDefaultMessage());


                model.addAttribute("user",user);
            hasError=1;
        }

        if(userRepo.findByUserName(user.getUserName())!=null)
        {
            model.addAttribute("userName","User Already Exists..please try another one");
            hasError=1;
        }
        if(!isValidEmailAddress(user.getEmail()))
        {
            model.addAttribute("email","invalid Email Address");
            hasError=1;
        }
        if(userRepo.findByEmail(user.getEmail())!=null)
        {
            model.addAttribute("email","email Address Already Exists..please try another one");
            hasError=1;
        }
        if (hasError==1)
            return "signUp";
        user.setUserName(user.getUserName().toLowerCase());
        session.setAttribute("user",user);
        session.setMaxInactiveInterval(20*60);
        userRepo.save(user);
        fileRepo.save(new File(user.getUserName(),"png",null,user.getId(),null,file.getBytes()));
        return "redirect:/users/"+user.getUserName();
    }

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    @Autowired
    public void setFileRepo(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }


    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}
