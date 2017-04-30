package com.example.Controllers;

import com.example.Models.Case;
import com.example.Models.File;
import com.example.Models.User;
import com.example.Repositories.CaseRepo;
import com.example.Repositories.FileRepo;
import com.example.Repositories.TestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by baraa on 2/25/2017.
 */

@Controller
@RequestMapping("/users/{user}")
public class Cases {


    private CaseRepo caseRepo;
    private TestRepo testRepo;
    private FileRepo fileRepo;



    @RequestMapping("/createCase")
    public String createCase(@SessionAttribute(value = "user",required = false)User user)
    {
        if(user==null)
            return "redirect:/login";
        return "caseForm";
    }





    @RequestMapping(value = "/createCase",method = RequestMethod.POST)
    public String createCaseJson(@SessionAttribute("user")User user, @ModelAttribute Case case1, @RequestParam MultipartFile file) throws IOException {
        if(user ==null)
            return "/login";
        else
        {
            case1.setCreatedDate(new Date());
            case1.setCreatedBy(user.getId());
            caseRepo.save(case1);
            fileRepo.save(new File(case1.getCaseName(),"png",case1.getCaseId(),null,null,file.getBytes()));
            return "redirect:/users/"+user.getUserName();
        }
    }
    @RequestMapping(value = "cases/{case}/editCase",method = RequestMethod.POST)
    public String editCaseJson(@SessionAttribute("user")User user, @ModelAttribute Case case1, @PathVariable("case")Long caseId,@RequestParam MultipartFile file) throws IOException {
        if(user ==null || caseRepo.findOne(caseId)==null)
            return "/login";
        else
        {
            Case dbCase = caseRepo.findOne(caseId);
            case1.setCaseId(caseId);
            case1.setCreatedDate(dbCase.getCreatedDate());
            case1.setSex(dbCase.isSex());
            if(fileRepo.findByCaseId(caseId)!=null) {
                if (file != null) {
                    File file1 = fileRepo.findByCaseId(caseId);
                    file1.setFile(file.getBytes());
                    fileRepo.save(file1);
                }
            }
                    caseRepo.save(case1);
            }
            return "redirect:/users/"+user.getUserName()+"/cases/"+case1.getCaseId();

    }

    @RequestMapping(value = "/cases/{case}/editCase")
    public String editCase(@SessionAttribute("user")User user, @ModelAttribute Case case1, @PathVariable("case")Long caseId)
    {
        if(user ==null || caseRepo.findOne(caseId)==null)
            return "/login";
        else
        {
            return "editCase";
        }
    }

    @RequestMapping("/cases/{case}.json")
    @ResponseBody
    public Case getCaseJson(@SessionAttribute("user") User user , @PathVariable("user") String userName,@PathVariable("case")Long case1)
    {
     if(user==null||!user.getUserName().equals(userName))
         return null;
     else
     {
       return caseRepo.findOne(case1);
     }
    }
    @RequestMapping(value = "/cases/{case}.png")
    @ResponseBody
    public ResponseEntity getProfileImage(@SessionAttribute User user, @PathVariable("user") String userName,@PathVariable("case") Long case1)
    {
        if(user==null||!user.getUserName().equals(userName))
            return null;
        else
        {
            if(fileRepo.findByCaseId(case1)!=null) {
                byte[] content = fileRepo.findByCaseId(case1).getFile();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);
                return new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
            }
            else
                return null;
        }
    }

    @RequestMapping("/cases/{case}")
    public String getCase(Model model,@SessionAttribute("user") User user , @PathVariable("user") String userName,@PathVariable("case")Long case1)
    {
        if(user==null||!user.getUserName().equals(userName))
            return "/users/login";
        else
        {
            model.addAttribute("case",caseRepo.findOne(case1));
            model.addAttribute("casePhoto","/users/"+user.getUserName()+"/cases/"+case1+".png");
            model.addAttribute("newTestButton","/users/"+user.getUserName()+"/cases/"+case1+"/createTest");
            model.addAttribute("testFormSave","/users/"+user.getUserName()+"/cases/"+case1+"/createTest");
            model.addAttribute("testFormEdit","/users/"+user.getUserName()+"/cases/"+case1+"/tests/editTest");
            model.addAttribute("tests",testRepo.findBycaseId(case1));
            model.addAttribute("deleteTest","/users/"+user.getUserName()+"/cases/"+case1+"/tests/delete?id=");
            model.addAttribute("testo","/users/"+user.getUserName()+"/cases/"+case1+"/tests");
            return "case";
        }
    }

    @RequestMapping("/cases.json")
    @ResponseBody
    public Iterable<Case> getAllCases(@SessionAttribute("user") User user ,
                                  @PathVariable("user") String userName)
    {
        if(user==null||!user.getUserName().equals(userName))
            return null;
        else
        {
            return caseRepo.findBycreatedBy(user.getId());
        }
    }
    @RequestMapping(value = "/cases/delete",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteCase(@SessionAttribute(required = false) User user ,
                             @PathVariable("user") String user1 ,
                             @RequestParam long id)
    {
        if(user==null||!user1.equals(user.getUserName()))
            return "/login";
        else
        {
            caseRepo.delete(id);
            return "{\"Success\":1}";
        }
    }


    @Autowired
    public void setCaseRepo(CaseRepo caseRepo) {
        this.caseRepo = caseRepo;
    }

    @Autowired
    public void setTestRepo(TestRepo testRepo) {this.testRepo = testRepo;}
    @Autowired
    public void setFileRepo(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }
}


