package com.example.Controllers;

import com.example.Models.File;
import com.example.Models.Post;
import com.example.Models.User;
import com.example.Repositories.FileRepo;
import com.example.Repositories.PostsRepo;
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
import java.util.Date;

/**
 * Created by baraa on 4/19/2017.
 */


@Controller
@RequestMapping("/users/{user}")
public class Posts {


    PostsRepo postsRepo;

    FileRepo fileRepo;


    @RequestMapping("/createPost")
    public String createPost(Model model , @SessionAttribute(value = "user",required = false)User user)
    {
        if(user==null )
        {
            model.addAttribute("login","please Login First");
            return "login";
        }
        model.addAttribute("post",new Post());
        return "postForm";
    }

    @RequestMapping(value = "/createPost",method = RequestMethod.POST)
    public String createPost(Model model
                            , @SessionAttribute(value = "user",required = false)User user,
                             @ModelAttribute Post post, @RequestParam("file")MultipartFile file) throws IOException {
        if(user==null)
        {
            model.addAttribute("login","please Login First");
            return "login";
        }
        post.setCreatedBy(user.getId());
        post.setCreatedDate(new Date());
        postsRepo.save(post);
        fileRepo.save(new File(post.getPostTitle(),"png",null,null,post.getPostId(),file.getBytes()));
        return "redirect:/users/"+user.getUserName();
    }


    @RequestMapping(value = "/{post}/addComment",method = RequestMethod.POST)
    public String addComment(Model model, @PathVariable Long user
            , @SessionAttribute(value = "user",required = false)User user1,
                             @ModelAttribute Post post, @RequestParam("file")MultipartFile file) throws IOException {
        if(user==null||user!=user1.getId())
        {
            model.addAttribute("login","please Login First");
            return "login";
        }
        postsRepo.save(post);
        fileRepo.save(new File(post.getPostTitle(),"png",null,null,post.getPostId(),file.getBytes()));
        return "redirect:/users/"+user1.getUserName();
    }
    @RequestMapping(value = "/posts/{post}.png")
    @ResponseBody
    public ResponseEntity getProfileImage(@SessionAttribute User user, @PathVariable("user") String userName, @PathVariable("post") Long post)
    {
        if(user==null||!user.getUserName().equals(userName))
            return null;
        else
        {
            if(fileRepo.findByPostId(post)!=null) {
                byte[] content = fileRepo.findByPostId(post).getFile();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);
                return new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
            }
            else
                return null;
        }
    }

    @Autowired
    public void setPostsRepo(PostsRepo postsRepo) {
        this.postsRepo = postsRepo;
    }

    @Autowired
    public void setFileRepo(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }


}
