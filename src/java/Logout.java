package java;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Logout() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		if(session.isNew()) {
			session.invalidate();
			System.out.println("Noob.");
			try {
				json.put("status", 304).put("message", "No Session Online.").put("redirect", "/MangaReader2/Login.html");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			out.print(json.toString());
			
		}else {
			session.invalidate();
			System.out.println("Logged Out.");
			try {
				json.put("status", 200).put("message", "Loggout Successful.").put("redirect", "/MangaReader2/Login.html");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			out.print(json.toString());
			
		}
	}
}
