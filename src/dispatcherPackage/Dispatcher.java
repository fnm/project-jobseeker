package dispatcherPackage;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class dispatch
 */
@WebServlet("/dispatch")
public class Dispatcher extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Dispatcher() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  request.setCharacterEncoding("UTF-8"); //TODO remember to delete
		
		  AjaxParser cr = AjaxParser.ClientRequestFactory(); 
		  ClientRequest msg = cr.ParseClientAjax(request);
		  
		  HandleRequest HR=new HandleRequest();
	  	  HandleRequestStatus HRS = HR.HandleMessage(msg);
	  	 
		  AjaxResponseGenerator ARG=new AjaxResponseGenerator();
		  JSONObject jsonObj=ARG.HandleResponse(HRS);
		  response.setCharacterEncoding("UTF-8");
		  response.setContentType("application/json");
		  PrintWriter out = response.getWriter();
		  System.out.println(jsonObj);//write the json object to console
		  out.println(jsonObj);
		  
	}
}
