package com.saifu.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 共通エラー処理
 * ハンドリング微妙。どうやって作るかわすれた。。
 * @author kxn4t
 */
@Component
public class ExceptionHandler implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {

		String exception = ex.getClass() + " : " + ex.getMessage();

        System.out.println(exception);

        ModelAndView mv = new ModelAndView();
        mv.addObject("exception", exception);
        mv.setViewName("error");
		return mv;
	}


}
