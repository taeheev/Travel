package handler.member;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import handler.Handler;
import member.MemberService;
import member.MemberVo;

public class MemberLogin implements Handler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
	if(request.getMethod().toLowerCase().equals("get")) {
		return "/member/loginForm.jsp";
		
	} else {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		
		MemberService service = new MemberService();
		MemberVo vo = service.getMember(id);
		
		//로그인 처리
		if(vo != null && pwd.equals(vo.getPwd())) {//로그인 성공
			//현재 사용중인 세션 반환
			HttpSession session = request.getSession();
			session.setAttribute("loginId", id);//로그인 아이디
		}
//		return "redirect:/index.jsp";
		return "/index.jsp";
	}
	
	}
}
