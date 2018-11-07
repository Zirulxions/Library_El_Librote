package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import utility.DataBase;
import utility.Encrypt;
import utility.PropsManager;

@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Encrypt encPassword;
	private DataBase conn = new DataBase();
	
    public Register() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		try {
			execRegister(conn.getConnection(), request, response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void execRegister(Connection myConnection, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException, JSONException {
		PropsManager prop = PropsManager.getInstance();
		JSONObject reqBody = new JSONObject(req.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
		JSONObject json = new JSONObject();
		PrintWriter out = res.getWriter();
		try { 
	        int id = Integer.parseInt(reqBody.getString("cedula"));
	        String name = reqBody.getString("nombre");
	        String email = reqBody.getString("email");
	        String direction = reqBody.getString("direccion");
	        String password = reqBody.getString("contrasenia");
	        PreparedStatement stat = null;
	        String signupQuery = prop.getValue("query_new");
        	encPassword = new Encrypt(password); //encrypt the password
            stat = myConnection.prepareStatement(signupQuery);
            stat.setInt(1, id);
            stat.setString(2, name);
            stat.setString(3, email);
            stat.setString(4, direction);
            stat.setString(5, encPassword.returnEncrypt());
            stat.executeUpdate();//use if no data will be returned... else use, executeQuery();
            System.out.println("Added to Database.");
            json.put("status", 200).put("message", "Done").put("redirect", "Login.html");
        } catch (SQLException | JSONException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.print(json.toString());
        out.print(json.toString());
	}

}
