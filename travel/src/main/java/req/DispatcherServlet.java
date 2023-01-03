package req;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import handler.Handler;

/**
 * Servlet implementation class DispatcherServlet
 */
@WebServlet("*.do")
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// 명령어와 실행할 handler객체를 쌍으로 저장할 맵생성
	private Map<String, Handler> map = new HashMap<>();
	//맵: 키,값 저장 
	//명령어와 실행할 핸들러 객체를 쌍으로 저장할 맵 생성 
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DispatcherServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	// 서블릿 초기화 메서드
	@Override
	public void init() throws ServletException {
		//init: 서블릿 페이지가 맨 처음 실행될때 초기화하는 메서드 
		// TODO Auto-generated method stub
		// Properties. Map과 비슷하게 키와 값 저장하는 클래스.
		
		Properties prop = new Properties();
		//properties : 맵과 비슷하게 키와 값 저장하는 메서드 
		
		String path = "/WEB-INF/command.properties";
		// properties 파일 경로
		
		String path2 = getServletContext().getRealPath(path);
		//웹에서 접근할수있는 주소로 변환 
		
		try {
			
			// 파일의 내용을 읽어서 properties에 복구
			prop.load(new FileReader(path2));
			//fileReader문자단위로 읽을 수 있는 스트림 
			
			Iterator iter = prop.keySet().iterator();
			//keyset 맵에 있는 키 묶음 (순서가 없는 중복이 안되는 집합) iterator 반복자 
			//properties의 키값 묶음에 반복자 붙여서 

			// 읽을 키가 있을 동안 반복
			while (iter.hasNext()) {
				String url = (String) iter.next();// 키를 하나씩 읽어서
				// 키에 묶인 값을 읽는다(클래스명)
				
				String className = prop.getProperty(url);

				try {
					
					Class<?> handlerClass = Class.forName(className);
					//Class : 클래스에 대한 정보파일 
					//클래스 이름으로 클래스 정보를 읽어서 반환 
					
					Constructor<?> cons = handlerClass.getConstructor(null);
					Handler handler = (Handler) cons.newInstance();
				
					map.put(url, handler);
					// 명령어와 핸들러객체를 쌍으로 등록
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//String url = request.getParameter("url");
		// 요청 url 보냄, 명령어로 등록된 핸들러 객체를 꺼내서
		
		String url = request.getServletPath();
		//요청 url을 반환받는 메서드 
		
		
		Handler handler = map.get(url);
		//그 url이 등록되어있는 process()를 호출 
		
		String view = handler.process(request, response);
		
	
		if (view == null) {
			return;
		}
		if (view.startsWith("redirect:")) {
			String path = view.split(":")[1];
			// : 기준으로 쪼개서 sendRedirect 
			
			response.sendRedirect(request.getContextPath() + path);
		} else if (view.startsWith("responsebody/")) {
			String body = view.split("/")[1];
			//앞쪽은 뗴고 뒤쪽에있는 url만 가지고 간다 
			
			response.getWriter().append(body);
			
		} else {		//redirct로 시작하지 않으면 forward로 보낸다.
			RequestDispatcher dis = request.getRequestDispatcher(view);
			dis.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
