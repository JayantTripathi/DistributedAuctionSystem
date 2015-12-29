

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.Object;
import java.sql.*;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.simple.*;



@WebServlet(urlPatterns = {"/DistributedAuctionSystem"})
public class DistributedAuctionSystem extends HttpServlet {
Connection Conn=null;
Statement stmt=null;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
       
    }    

   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
         String user ="root";
      String password="123456";
 
      try {
      Class.forName("com.mysql.jdbc.Driver");
      Conn=DriverManager.getConnection("jdbc:mysql://localhost/distributedauctionsystem",user,password);
      stmt=Conn.createStatement();
      String Query="Select * from product where ProductID=2;";
      ResultSet RS=stmt.executeQuery(Query);
      JSONArray obj_array_Json=new JSONArray();
       JSONObject obj_json_data= null;
        while (RS.next()) {              
              String Product_NameProduct_Name=RS.getString("ProductName");
              String Seller_price=RS.getString("SellerPrice");
              String Ends_in_Days=RS.getString("Timelimit");
              String ItemCondition=RS.getString("ItemCondition");
              String DateSubmitted=RS.getString("DateSubmitted");
              Blob ProduuctPicture=RS.getBlob("ProductPicture");
              InputStream in= ProduuctPicture.getBinaryStream();
              //byte[] blob_obj_bytes=ProduuctPicture.getBytes(1, (int)ProduuctPicture.length());
              String ProduuctPicture_string =in.toString();
              obj_json_data=new JSONObject();
              obj_json_data.put("ProductName", Product_NameProduct_Name);
              obj_json_data.put("SellerPrice",Seller_price);
              obj_json_data.put("Timelimit",Ends_in_Days);
              obj_json_data.put("ItemCondition",ItemCondition);
              obj_json_data.put("DateSubmitted", DateSubmitted);
              obj_json_data.put("ProductPicture", ProduuctPicture_string);
              
              
          }
      
       
       response.setContentType("application/json");
        PrintWriter printwriterobj=response.getWriter();
        printwriterobj.println(obj_json_data.toString());
     
      }
      catch(Exception e){
          e.printStackTrace();
    }
       
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

  
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
