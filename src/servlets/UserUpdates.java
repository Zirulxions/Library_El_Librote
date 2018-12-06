package servlets;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/UserUpdates")
public class UserUpdates extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataBase conn = new DataBase();
	PropsManager prop = PropsManager.getInstance();
	private Encrypt encPassword;
       
    public UserUpdates() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		try {
			execUserUpdate(conn.getConnection(), request, response);
		} catch (JSONException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void execUserUpdate(Connection connection, HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException {
		response.setContentType("application/json");
		JSONObject reqBody = new JSONObject(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
		PrintWriter out = response.getWriter();
		JSONObject jsonRet = new JSONObject();
		HttpSession session = request.getSession();
		PreparedStatement stat = null;
		Integer userSession = (Integer) session.getAttribute("usr");
		System.out.println("Usuario Actual: " + userSession);
		Integer aux = reqBody.getInt("aux");
		if(userSession != null) {
			if(aux == 1) {
				String oldPass = reqBody.getString("contAntigua");
				String newPass = reqBody.getString("contNueva");
				String passQuery = prop.getValue("query_updatePassword");
				String getOldPass = prop.getValue("query_logIn");
				try {
					System.out.println("Comprobando contraseñas...");
					encPassword = new Encrypt(oldPass);
					stat = connection.prepareStatement(getOldPass);
					stat.setInt(1, userSession);
					stat.setString(2, encPassword.returnEncrypt());
					ResultSet result = stat.executeQuery();
					if(result.next()) {
						System.out.println("Contraseña valida... Cambiando...");
						stat = null;
						result = null;
						encPassword = new Encrypt(newPass);
						stat = connection.prepareStatement(passQuery);
						stat.setString(1, encPassword.returnEncrypt());
						stat.setInt(2, userSession);
						System.out.println("Ejecutando...");
						stat.executeUpdate();
						jsonRet.put("status", 200).put("message", "Contraseña cambiada satisfactoriamente...!");
						stat = null;
						result = null;
						System.out.println("Finalizado. Contraseña cambiada satisfactoriamente...!");
					} else {
						jsonRet.put("status",500).put("message", "Contraseña Invalida...!");
						
					}
				} catch (SQLException e) {
					//System.out.println("Error: " + e.getMessage());
					e.printStackTrace();
					jsonRet.put("status",500).put("message", "Algo no fue bien... No se ha cambiado la contraseña");
					if(stat != null) {
						stat = null;
					}
				}
			}
			if(aux == 2) {
				String oldEmail = reqBody.getString("emailAntiguo");
				String newEmail = reqBody.getString("emailNuevo");
				String newEmailQuery = prop.getValue("query_updateEmail");
				String getOldEmail = prop.getValue("query_getEmail");
				try {
					System.out.println("Validando Email...");
					stat = connection.prepareStatement(getOldEmail);
					stat.setInt(1, userSession);
					stat.setString(2, oldEmail);
					ResultSet result = stat.executeQuery();
					if(result.next()) {
						System.out.println("Email Valido... Cambiando...");
						stat = null;
						result = null;
						stat = connection.prepareStatement(newEmailQuery);
						stat.setString(1, newEmail);
						stat.setInt(2, userSession);
						System.out.println("Actualizando...");
						stat.executeUpdate();
						jsonRet.put("status", 200).put("message", "Email Cambiado satisfactoriamente...!");
						stat = null;
						result = null;
						System.out.println("Email cambiado satisfactoriamente...!");
					} else {
						jsonRet.put("status",500).put("message", "Email Invalido...!");
					}
				} catch (SQLException e) {
					System.out.println("Error: " + e.getMessage());
					jsonRet.put("status",500).put("message", "Algo no fue bien... No se ha cambiado el Email...!");
					if(stat != null) {
						stat = null;
					}
				}
			}
			if(aux == 3) {
				String upName = reqBody.getString("nuNombre");
				String nameQuery = prop.getValue("query_updateName");
				try {
					System.out.println("Comprobando usuario...");
					stat = connection.prepareStatement(nameQuery);
					stat.setString(1, upName);
					stat.setInt(2, userSession);
					System.out.println("Ejecutando Cambio de Nombre...");
					stat.executeUpdate();
					jsonRet.put("status",200).put("message", "Nombre cambiado satisfactoriamente: " + upName);
					stat = null;
					System.out.println("Finalizado el cambio de nombre...!");
				} catch (SQLException sq) {
					System.out.println("Error: " + sq.getMessage());
					jsonRet.put("status",500).put("message", "Algo fue mal... No ha sido posible ejecutar el cambio de nombre");
				}
			}
			if((aux < 1) && (aux > 3)) {
				System.out.println("Un usuario ha intentado piratear el Back-End...");
				jsonRet.put("status",500).put("message", "Algo fue mal... Por favor deja de intentar piratear el Back-End!");
			}
		} else {
			jsonRet.put("status",500).put("message", "No estas Logeado...!");
		}
		out.print(jsonRet.toString());
	}
}
