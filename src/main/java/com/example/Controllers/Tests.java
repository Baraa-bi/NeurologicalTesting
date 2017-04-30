package com.example.Controllers;

import com.example.Models.Test;
import com.example.Models.User;
import com.example.Repositories.TestRepo;
import com.example.Repositories.UserRepo;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataSource;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Date;

/**
 * Created by baraa on 4/2/2017.
 */
@Controller
@RequestMapping("/users/{userName}/cases/{caseId}")
public class Tests {

    private JavaMailSender mailSender;

    @Autowired
    public Tests(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RequestMapping(value = "/tests/{testId}/sendTo")
    @ResponseBody
    public String send(@PathVariable Long testId,@RequestParam("email")String emailAddress) throws MessagingException {
            Test test = testRepo.findOne(testId);
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
            messageHelper.setTo(emailAddress);
            messageHelper.setSubject(test.getTestName()+" Report");
            messageHelper.setText("<h1 style='font-style:forte'>Hello....<br>This is "+test.getTestName()+"Test.<br>" +
                    "kindly check <br> Best Regards<br>ASIA Neurological Testing</h1>",true);

            String path = "C:\\Users\\mrbar\\Desktop\\1.jpg";

            BufferedImage im = ImageIO.read(new File(path));
            Graphics2D g2 = im.createGraphics();

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 25));

            for(int i = 0; i < pos1.length; i++){
                for(int j = 0; j < arr[i]; j++){
                    g2.drawString("55", pos1[i][0], pos1[i][1] + rectHieght * j);
                }
            }

            for(int i = 0; i < pos2.length; i++){
                g2.drawString("50", pos2[i][0], pos2[i][1]);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(im, "jpg", baos);
            byte[] bytes =  baos.toByteArray();



           messageHelper.addAttachment("omar.jpg",
                   new ByteArrayResource(IOUtils.toByteArray(new ByteArrayInputStream(bytes))));
        };
        mailSender.send(messagePreparator);
        return "{\"messeage\":true}";
    }

    private TestRepo testRepo;
    private UserRepo userRepo;

    @RequestMapping(value = "/createTest")
    public String createTest(@SessionAttribute("user")User user,@PathVariable("caseId")long caseId)
    {
        if(user!=null)
        {
            return "test";
        }
        else
            return null;
    }
    @RequestMapping(value = "/createTest",method = RequestMethod.POST)
    public void createTest(HttpServletResponse response,@SessionAttribute("user")User user, @PathVariable("caseId")long caseId, @ModelAttribute("test")Test test) throws IOException {
        if(user!=null)
        {
            test.setCreatedDate(new Date());
            test.setCaseId(caseId);
            testRepo.save(test);
            response.sendRedirect("/users/"+user.getUserName()+"/cases/"+caseId);
        }
    }

    @RequestMapping(value = "/tests/editTest",method = RequestMethod.POST)
    public String editTest(@SessionAttribute("user")User user,@PathVariable("caseId")long caseId,@RequestParam Long id,@ModelAttribute("test")Test test)
    {
        if(user!=null)
        {
            test.setCreatedDate(new Date());
            test.setCaseId(caseId);
            test.setId(id);
            testRepo.save(test);
            return "redirect:/users/"+user.getUserName()+"/cases/"+caseId;
        }
        else
            return "redirect:/users";
    }

    @RequestMapping("/tests.json")
    @ResponseBody
    public Iterable getAllTests(@SessionAttribute("user")User user,@PathVariable("caseId")long caseId)
    {
        if(user!=null)
            return testRepo.findBycaseId(caseId);
        else
            return null;
    }
    @RequestMapping("/tests/{testId}.json")
    @ResponseBody
    public Test getTest(@SessionAttribute("user") User user, @PathVariable("testId") Long id)
    {

        if(user!=null)
            return testRepo.findOne(id);
        else
            return null;
    }

    @RequestMapping(value = "/tests/delete",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteTest(@SessionAttribute("user") User user,@PathVariable("userName") String user1,@RequestParam("id") long id) {
        if (user == null || !user1.equals(user.getUserName()))
            return "{\"success\":0}";
        else {
            Test test = testRepo.findOne(id);
            testRepo.delete(id);
            return test.toString();
        }
    }


    static int pos1[][] = {
            {570, 416},
            {570, 1028},
            {685, 308},
            {810, 308},
            {1415, 308},
            {1540, 308},
            {1655, 416},
            {1655, 1028},
    };

    static int pos2[][] = {
            {565, 1340},
            {680, 1340},
            {798, 1340},
            {1415, 1340},
            {1532, 1340},
            {1647, 1340},
            {140, 1445},
            {285, 1445},
            {525, 1445},
            {675, 1445},
            {825, 1445},
            {1070, 1445},
            {1245, 1445},
            {1390, 1445},
            {1590, 1445},
            {1750, 1445},
            {1900, 1445},
            {2100, 1445},
            {480, 1550},
            {555, 1550},
            {480, 1586},
            {555, 1586},
            {925, 1556},
            {1475, 1535},
            {1475, 1585},
            {2005, 1550},
            {2080, 1550},
            {2005, 1585},
            {2080, 1585},
    };

    static int arr[] = {5, 5, 28, 28, 28, 28, 5, 5};
    static int rectHieght = 36;


    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Autowired
    public void setTestRepo(TestRepo testRepo) {
        this.testRepo = testRepo;
    }
}

