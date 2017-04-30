package com.example.Controllers;

import com.example.Models.File;
import com.example.Models.User;
import com.example.Repositories.FileRepo;
import com.example.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by baraa on 2/21/2017.
 */

@Controller
@RequestMapping("/users")
public class Users {


    private UserRepo userRepo;
    private FileRepo fileRepo;

    @RequestMapping("/user")
    public String user(Model model)
    {
        return "user";
    }

    @RequestMapping("/{user}.json")
    @ResponseBody
    public User user(@SessionAttribute(value = "user",required = false) User user1, @PathVariable("user")String userName)
    {
        if (user1==null || !user1.getUserName().equals(userName))
            return null;
        User user = userRepo.findOne(user1.getId());
        user.setPassword("*********");
        return user;
    }

    @RequestMapping("/{userName}")
    public String user1(Model model,@SessionAttribute(value = "user",required = false) User user,@PathVariable(value = "userName")String userName)
    {
        if(user!=null&&user.getUserName().equals(userName))
        {
            model.addAttribute("user",user);
            model.addAttribute("photo","/users/"+user.getUserName()+".png");
            model.addAttribute("profile","/users/"+user.getUserName());
            model.addAttribute("editProfile","/users/"+user.getUserName()+"/editProfile");
            model.addAttribute("logged","display:inline");
            model.addAttribute("notLogged","display:none");
            return "user";
        }
        else
        {
            model.addAttribute("userName","please Login First");
            return "login";
        }
    }

    @RequestMapping("")
    public String userProfile (Model model, @SessionAttribute(value = "user" ,required = false) User user)
    {
        if (user==null)
            return "login";
    else
        return "redirect:/users/"+user.getUserName();
    }


    @RequestMapping(value = "/{userName}/editProfile")
    public String editProfile(Model model,@SessionAttribute User user)
    {
        if(user==null)
            return "login";
        else
        {
            model.addAttribute("photo","/users/"+user.getUserName()+".png");
            return "editProfile";
        }
    }

    @RequestMapping(value = "/{userName}/editProfile",method = RequestMethod.POST)
    public String editProfile(@SessionAttribute User user, @ModelAttribute User user1, @RequestParam MultipartFile file) throws IOException {
        if(user==null)
            return "login";
        else
        {
            user1.setPassword(user.getPassword());
            userRepo.save(user1);
            File file1 = fileRepo.findByUserId(user1.getId());
            if (file1==null)
                file1=new File(user1.getUserName(),"png",null,user1.getId(),null,file.getBytes());
            else
            file1.setFile(file.getBytes());
            fileRepo.save(file1);
            return "user";
        }
    }

    @RequestMapping(value = "/{userName}.png")
    @ResponseBody
    public ResponseEntity getProfileImage(@SessionAttribute User user,@PathVariable String userName)
    {
        if(user==null)
            return null;
        else
        {
            User user1 = userRepo.findByUserName(userName);
            if(fileRepo.findByUserId(user1.getId())!=null) {
                byte[] content = fileRepo.findByUserId(user1.getId()).getFile();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);
                return new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
            }
            else
                return null;
            }
    }

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Autowired
    public void setFileRepo(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }
}
