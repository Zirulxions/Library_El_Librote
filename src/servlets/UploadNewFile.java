package servlets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.json.JSONException;
import org.json.JSONObject;

import utility.DataBase;
import utility.PropsManager;

@WebServlet("/UploadNewFile")
@MultipartConfig
public class UploadNewFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataBase conn = new DataBase();
	PropsManager prop = new PropsManager();
       
    public UploadNewFile() {
        super();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		try {
			execGetBooks(conn.getConnection(), request, response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		execUploadFile(conn.getConnection(), request, response);
	}
	
	private void execGetBooks(Connection connection, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
		JSONObject jsonRet = new JSONObject();
		PrintWriter out = response.getWriter();
		PreparedStatement stmt = null;
		try {
			String queryBooks = prop.getValue("query_getBookList");
			stmt = connection.prepareStatement(queryBooks);
			ResultSet result = stmt.executeQuery();
			Integer list = null;
			if(result.next()) {
				list = result.getInt("count");
			}
			System.out.println("Cantidad de Libros: " + list);
			
			//Funciones Random.
			Integer L1 = (int) ((Math.random() * ((list - 1) + 1)) + 1);
			Integer L2 = (int) ((Math.random() * ((list - 1) + 1)) + 1);
			Integer L3 = (int) ((Math.random() * ((list - 1) + 1)) + 1);
			while((L1 == L2) || (L1 == L3) || (L2 == L3) || (L1 == 0) || (L2 == 0) || (L3 == 0)) {
				if(L1 == L2) { L2 = (int) ((Math.random() * ((list - 1) + 1)) + 1); }
				if(L2 == L3) { L3 = (int) ((Math.random() * ((list - 1) + 1)) + 1); }
				if(L1 == L3) { L3 = (int) ((Math.random() * ((list - 1) + 1)) + 1); }
				if(L1 == 0) { L1 = 1; }
				if(L2 == 0) { L2 = 1; }
				if(L3 == 0) { L3 = 1; }
				System.out.println("Valores Actuales L1 = " + L1 + " L2 = " + L2 + " L3 = " + L3);
			}
			String msjL1L2L3 = "Valores Finales: L1 = " + L1 + " L2 = " + L2 + " L3 = " + L3;
			System.out.println(msjL1L2L3);
			//Fin funciones Random.
			
			jsonRet.put("status", 200).put("message", msjL1L2L3);
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		out.print(jsonRet.toString());
	}

	private void execUploadFile(Connection connection, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		JSONObject json = new JSONObject();
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		Integer usuario = (Integer) session.getAttribute("usr");
		String nombre_libro = request.getParameter("nombre_libro");
		String autor_libro = request.getParameter("autor_libro");
		Integer genero_libro = Integer.parseInt(request.getParameter("genero_libro"));
		Integer anio_libro = Integer.parseInt(request.getParameter("anio_libro"));
		String editorial_libro = request.getParameter("editorial_libro");
		Part file = request.getPart("file");
		InputStream filecontent = file.getInputStream();
		OutputStream output = null;
		System.out.println("Usuario: " + usuario);
		if((usuario != null) && (nombre_libro != null) && (autor_libro != null) && (genero_libro != null) && (anio_libro != null) && (editorial_libro != null)) {
			try {
				System.out.println("Preparando todo...");
				String baseDirWeb = prop.getValue("baseDirWeb") + this.getFileName(file);
				PreparedStatement stat = null;
				stat = connection.prepareStatement(prop.getValue("query_newBook"));
				stat.setString(1, nombre_libro);
				stat.setString(2, autor_libro);
				stat.setInt(3, anio_libro);
				stat.setString(4, baseDirWeb);
				stat.setInt(5, genero_libro);
				stat.setString(6, editorial_libro);
				System.out.println("Añadiendo a la base de datos...");
				stat.executeUpdate();
				System.out.println("Libro creado en base de datos... Creando archivo...");
				String baseDir = prop.getValue("baseDir");
				output = new FileOutputStream(baseDir + this.getFileName(file));
				int read = 0;
				byte[] bytes = new byte[10240];
				while ((read = filecontent.read(bytes)) != -1) {
					output.write(bytes, 0, read);
				}
				System.out.println("Todo Listo...! Enviando respuesta...");
				json.put("status", 200).put("message", "Libro creado y Añadido a la base de datos!");
			} catch (Exception e) {
				e.printStackTrace();
				try {
					json.put("status", 500).put("message", "Algo Fue Mal..." + e.getMessage());
				} catch (JSONException errjson) {
					errjson.printStackTrace();
				}
			} finally {
				if (filecontent != null) {
					filecontent.close();
				}
				if (output != null) {
					output.close();
				}
			}
		} else {
			try {
				json.put("status", 500).put("message", "Faltan campos por rellenar o no estas logueado..!");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		out.print(json.toString());
	}

	private String getFileName(Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
}
