package com.example.Models;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;

public class Main {

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

  public static void main(String[] args) throws IOException {
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
      byte[] b =  baos.toByteArray();

      FileOutputStream fos = new FileOutputStream("C:\\Users\\mrbar\\Desktop\\new.jpg");
      fos.write(b);
      fos.close();
  }
}
