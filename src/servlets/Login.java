package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import utility.DataBase;
import utility.Encrypt;
import utility.PropsManager;

/**
 * Servlet implementation class Login
 */
@WebServlet("/LoginBiblioteca")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Encrypt encPassword;
	private DataBase conn = new DataBase();
    
    public Login() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		try {
			validateLogin(conn.getConnection(), request, response);
		} catch (JSONException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private void validateLogin(Connection connection, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, JSONException, NoSuchAlgorithmException {
		JSONObject reqBody = new JSONObject(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
		PropsManager prop = PropsManager.getInstance();
		PrintWriter out = response.getWriter();
		JSONObject jsonRet = new JSONObject();
		HttpSession session;
		PreparedStatement stat = null;
		String loginQuery = prop.getValue("query_logIn");
		try {
			Integer username = Integer.parseInt(reqBody.getString("ci"));
			String password = reqBody.getString("password");
			encPassword = new Encrypt(password);
			stat = connection.prepareStatement(loginQuery);
			stat.setInt(1, username);
			stat.setString(2, encPassword.returnEncrypt());
			ResultSet res = stat.executeQuery(); //executeQuery retorna el valor, o no si no lo posee.
			if(res.next()) {
				Integer type_id = res.getInt("id_tipo_persona");
				if(checkUserType(type_id)) {
					System.out.println("@@@ ADMIN @@@");
					session = request.getSession();
					jsonRet.put("status", 200).put("message", "Login successful").put("redirect", "UserLogged.html");
				} else {
					System.out.println("XXX Not Admin XXX");
					session = request.getSession();
					jsonRet.put("status",404).put("message", "Login successful").put("redirect", "UserLogged.html");
				}
				session.setAttribute("usr", username);
			} else {
				jsonRet.put("status",500).put("message", "Invalid username or password");
			}
			out.print(jsonRet.toString());
			
		} catch (SQLException | JSONException ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}
	
	public boolean checkUserType(int type_id) {
        boolean isAdmin = false;
        isAdmin = ((type_id == 1) ? true : false);
        return isAdmin;
    }
}
