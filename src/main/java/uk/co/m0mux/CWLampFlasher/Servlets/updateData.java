package uk.co.m0mux.CWLampFlasher.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.m0mux.CWLampFlasher.App;
import uk.co.m0mux.CWLampFlasher.PinThreadController;
import uk.co.m0mux.CWLampFlasher.Morse.CWUtils;

/**
 * Servlet implementation class updateData
 */
public class updateData extends HttpServlet {
	private static final long serialVersionUID = 1L;
      private PinThreadController ptc; 
      
      private static final Logger Logger = LogManager.getLogger(updateData.class.getName());
    /**
     * @see HttpServlet#HttpServlet()
     */
    public updateData() {
        super();
        ptc = App.getPtc();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		response.getWriter().append(ptc.pinthreads.toString());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try {
	        	   int pin = Integer.parseInt(request.getParameter("pin"));
	        	   
	        	   int speed = Integer.parseInt(request.getParameter("speed"));
	        	   String cw = request.getParameter("cw");

	        	   ptc.updateOutputThread(pin, cw, speed);
	        	   response.sendRedirect("index.jsp?success=true");
	          
			} catch (Exception e)
			{
					response.sendRedirect("index.jsp?success=false");
			}
		
	       
	}

}
