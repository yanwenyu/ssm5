package com.duyi.hrb.controller;

import com.duyi.hrb.domain.User;

import com.duyi.hrb.servcie.MessageService;
import com.duyi.hrb.servcie.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController extends BaseCotroller {
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @RequestMapping("/test/freemarker1")
    public Map<String,String> freemarker1(){
        Map<String,String> map = new HashMap<>();
        map.put("name","freemarker1");
        return map;
    }

    @RequestMapping("/test/freemarker2")
    public ModelMap freemarker2(){
        ModelMap map = new ModelMap();
        map.put("name","freemarker2");
        return map;
    }
    /**
     * 返回类型是ModelAndView
     * @return
     */
    @RequestMapping("/test/freemarker3")
    public ModelAndView freemarker3(){
        ModelAndView modelAndView = new ModelAndView("/test/freemarker3");
        modelAndView.addObject("name", "freemarker3");
        return modelAndView;
    }

    /**
     * 返回类型是ModelAndView
     * 对于ModelAndView构造函数可以指定返回页面的名称，也可以通过setViewName方法来设置所需要跳转的页面；
     * @return
     */
    @RequestMapping(value = "/test/freemarker4")
    public ModelAndView freemarker4() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name", "freemarker4");
        modelAndView.setViewName("/test/freemarker4");
        return modelAndView;
    }

    /**
     * 返回类型是ModelAndView,方法参数是model和map
     * @return
     */
    @RequestMapping(value = "/test/freemarker5")
    public ModelAndView freemarker5(Model model, Map<String, Object> map) {
        map.put("name1", "balalal1");
        map.put("name2", "balalal2");
        map.put("name3", "balalal3");
        map.put("name4", "balalal4");
        model.addAllAttributes(map);
        return new ModelAndView("/test/freemarker5");
    }


    /**
     * 返回String类型
     *
     * @return
     */
    @RequestMapping(value = "/test/freemarker6")
    public String freemarker6(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        request.setAttribute("name", "freemarker6");
        return "/test/freemarker6";
    }

    /**
     * 方法的参数是Model，返回值是String类型
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/test/freemarker7")
    public String freemarker7(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        model.addAttribute("name", "freemarker7");
        return "/test/freemarker7";
    }

    /**
     * 返回重定向redirect后的逻辑视图名
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/test/freemarker8")
    public String freemarker8(){

        return "redirect:/test/freemarker6";
    }

    @RequestMapping(value = "/test/freemarker9")
    public String freemarker9(){

        return "forward:/test/freemarker7";
    }



    @RequestMapping(value = "/findByName",method = RequestMethod.GET)
    public void findByName(@RequestParam(name = "username") String name, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        if (null == name || "".equals(name)){
            wirteResult(response,RespStatusEnum.FAIL.getStatus(),"username值为空无法查找",null);
            return;
        }
        User user = userService.findByName(name);
        if (null != user) {
            wirteResult(response,RespStatusEnum.SUCCESS.getStatus(),"查询成功",user);
        } else {
            wirteResult(response,RespStatusEnum.SUCCESS.getStatus(),"未找到结果",null);
        }
    }

    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public void addUser(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam("tel") String tel,
                        HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        if ( null == username | "".equals(username)) {
            wirteResult(response,RespStatusEnum.FAIL.getStatus(),"用户名不能为空",null);
            return;
        }
        if ( null == password | "".equals(password)) {
            wirteResult(response,RespStatusEnum.FAIL.getStatus(),"密码不能为空",null);
            return;
        }
        if ( null == tel | "".equals(tel)) {
            wirteResult(response,RespStatusEnum.FAIL.getStatus(),"tel不能为空",null);
            return;
        }

        UserService.InsertStatus result = userService.addUser(username, password, tel);
        wirteResult(response,result.getRespStatus().getStatus(),result.getMsg(),null);
    }


    @RequestMapping(value = "/deleteUser",method = RequestMethod.POST)
    public void delete(@RequestParam("id") int id,
                        HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        if (id < 0) {
            wirteResult(response,RespStatusEnum.FAIL.getStatus(),"请输入有效Id",null);
            return;
        }

        if (userService.del(id)) {
            wirteResult(response,RespStatusEnum.SUCCESS.getStatus(),"删除成功",null);
        } else {
            wirteResult(response,RespStatusEnum.FAIL.getStatus(),"删除失败",null);
        }

    }

    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    public void updateUser(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam("tel") String tel,
                        @RequestParam("id") Integer id,
                        HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");

        if (id < 0) {
            wirteResult(response,RespStatusEnum.FAIL.getStatus(),"请输入有效Id",null);
            return;
        }
        if ( null == username | "".equals(username)) {
            wirteResult(response,RespStatusEnum.FAIL.getStatus(),"用户名不能为空",null);
            return;
        }
        if ( null == password | "".equals(password)) {
            wirteResult(response,RespStatusEnum.FAIL.getStatus(),"密码不能为空",null);
            return;
        }
        if ( null == tel | "".equals(tel)) {
            wirteResult(response,RespStatusEnum.FAIL.getStatus(),"tel不能为空",null);
            return;
        }
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setTel(tel);

        UserService.UpdateStatus result = userService.updateUser(user);
        wirteResult(response,result.getRespStatus().getStatus(),result.getMsg(),null);
    }

    @RequestMapping("/insertMsg")
    public void insertMsg(@RequestParam("message") String message,
                          HttpServletResponse response) throws IOException {
        messageService.insert(message);
        response.setContentType("text/html;charset=utf-8");

        wirteResult(response,RespStatusEnum.SUCCESS.getStatus(),"lalalal",null);
    }
}
