/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletInputStream;
import java.lang.Object;
import javax.imageio.*;

/**
 *
 * @author SuperUser
 */
@WebServlet(urlPatterns = {"/Useless"})
public class Useless extends HttpServlet {
Connection Conn=null;
 Statement stmt=null;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException,SQLException {
        response.setContentType("text/html;charset=UTF-8");
                    /* TODO output your page here. You may use following sample code. */
               
     String userName = "root";
     String password = "123456";
     
     try{
      Class.forName("com.mysql.jdbc.Driver");
     }
     catch(ClassNotFoundException E)
     {
         E.printStackTrace();
     }
      Conn=DriverManager.getConnection("jdbc:mysql://localhost/distributedauctionsystem",userName,password);
      stmt=Conn.createStatement();
      String ProductID=request.getParameter("ProductID");
      String ProductName=request.getParameter("ProductName");
      String Query="Select ProductPicture from distributedauctionsystem.Product where ProductID=" + ProductID + "and ProductName=" + ProductName + ";";
       
          
      
        
         ResultSet RS=null;
          Blob ProductPicture=null;
           RS=stmt.executeQuery(Query);
           while(RS.next())
      {
            try{
               //ContentType = "application/pdf";
          ProductPicture=RS.getBlob("ProductPicture");
              byte[] blobData=ProductPicture.getBytes(1, (int)ProductPicture.length());     
               int size = blobData.length;  // bytes to be sent
               response.setContentLength(size);
               response.setContentType("Content-Type"); 
               InputStream in= ProductPicture.getBinaryStream();
               ServletOutputStream stream =response.getOutputStream();
                
               stream.write(blobData);
               stream.flush();        
               // PrintWriter out =response.getWriter();
               
              // out.println(in);
             
                   
            }
            catch(Exception E)
            {
            //  out.println(E.getMessage()); 
            }
       
        
      
      }
        }
    


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
        processRequest(request, response);
        }
        catch(SQLException E)
        {
            E.printStackTrace();
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
        processRequest(request, response);
        }
        catch(SQLException E)
        {
            E.printStackTrace();
        }
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
