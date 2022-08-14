package com.starrysea.crowd.mvc.config;

import com.google.gson.Gson;
import com.starrysea.crowd.exception.AccessForbiddenException;
import com.starrysea.crowd.exception.LoginAcctAlreadyInUseException;
import com.starrysea.crowd.exception.LoginFailedException;
import com.starrysea.crowd.util.CrowdConstant;
import com.starrysea.crowd.util.CrowdUtil;
import com.starrysea.crowd.util.ResultEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@ControllerAdvice表示当前类是一个基于注解的异常处理器类
@ControllerAdvice
public class CrowdExceptionResolver {

    // @ExceptionHandler将一个具体的异常类型和一个方法关联起来
    @ExceptionHandler(value=NullPointerException.class)
    public ModelAndView resolveNullPointerException(NullPointerException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return commonResolve("system-error",exception,request,response);
    }

    // 登录异常处理器
    @ExceptionHandler(value=LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(LoginFailedException exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {
        return commonResolve("admin-login",exception,request,response);
    }

    // 新增时账号重复异常处理
    @ExceptionHandler(value= LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(LoginAcctAlreadyInUseException exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {
        String message = exception.getMessage();
        if(message.contains("更新")){
            return commonResolve("admin-update",exception,request,response);
        }
        return commonResolve("admin-add",exception,request,response);
    }

    // // 禁止访问异常处理器--测试用
    // /**
    //  * /admin/to/main/page.html的请求已经使用了view-controller进行配置，这里这个controller没有作用，仅仅是用来
    //  * 测试使用view-controller配置的映射是否能够被基于注解的异常映射处理捕获。
    //  * 实测结果是：view-controller配置的映射是不能被基于注解的异常映射处理捕获的，只能被基于xml的异常映射处理捕获
    //  */
    // @ExceptionHandler(value=AccessForbiddenException.class)
    // public ModelAndView resolveAccessForbiddenException(AccessForbiddenException exception,
    //                                                 HttpServletRequest request,
    //                                                 HttpServletResponse response) throws IOException {
    //     return commonResolve("system-error",exception,request,response);
    // }

    //公共代码，抽出来封装成一个方法，其它异常处理器可以直接调用
    private ModelAndView commonResolve(String viewName, Exception exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.判断当前请求类型
        boolean judgeResult = CrowdUtil.judgeRequestType(request);

        //2.如果是Ajax请求，就不返回页面
        if(judgeResult){
            ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());

            //2.1 创建Gson对象，将resultEntity转换成json
            Gson gson = new Gson();
            String json = gson.toJson(resultEntity);

            //2.2 将Json字符串作为响应体返回给浏览器
            response.getWriter().write(json);

            //2.3 由于上面已经通过原生的response对象返回了响应，所以这个if不提供ModelAndView对象
            return null;
        }

        //3.如果是普通请求，则创建ModelAndView对象，返回对应错误页面
        ModelAndView modelAndView = new ModelAndView();
        //4.将Exception对象存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
        //5.设置对应的视图名称
        modelAndView.setViewName(viewName);
        //返回ModelAndView对象
        return modelAndView;
    }
}
