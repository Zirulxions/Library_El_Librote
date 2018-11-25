package servlets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONObject;

import utility.DataBase;
import utility.PropsManager;

@WebServlet("/UploadNewFile")
public class UploadNewFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataBase conn = new DataBase();
       
    public UploadNewFile() {
        super();
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		Part file = request.getPart("file");
		InputStream filecontent = file.getInputStream();
		OutputStream output = null;
		PropsManager prop = new PropsManager();
		try {
			String baseDir = prop.getValue("baseDir");
			output = new FileOutputStream(baseDir + this.getFileName(file));
			int read = 0;
			byte[] bytes = new byte[4096];
			while ((read = filecontent.read(bytes)) != -1) {
				output.write(bytes, 0, read);
			}
			json.put("status", 200).put("message", "Hecho.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (filecontent != null) {
				filecontent.close();
			}
			if (output != null) {
				output.close();
			}
		}
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
