package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author zhoumo
 * @datetime 2018/8/2 15:58
 * @desc 用户中心接口
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 数据校验
     *
     * @param data 要校验的数据
     * @param type 要校验的数据类型：1，用户名；2，手机；3，邮箱
     * @return - true：可用 - false：不可用
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> verify(@PathVariable String data,
                                          @PathVariable(required = false) Integer type) {
        Boolean b = userService.verify(data, type);
        if (b == null) {
            //参数有误
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            //返回校验结果
            return ResponseEntity.ok(b);
        }
    }

    /**
     * 根据用户输入的手机号,发送验证码到用户手机。
     *
     * @param phone 用户的手机号码
     * @return 204:请求已接收,400：参数有误, 500：服务器内部异常
     */
    @PostMapping("code")
    public ResponseEntity<Void> sendCode(@RequestParam String phone) {
        if (StringUtils.isBlank(phone) || !phone.matches("^1[34568]\\d{9}$")) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Boolean b = userService.sendCode(phone);
        if (b == null || !b) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    /**
     * 注册用户
     * @param user
     * @param code
     * @return 201,400,500
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code){
      Boolean b =  userService.register(user,code);
      if(b==null ||!b){
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 根据用户名和密码查询用户
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<User> query(@RequestParam("username") String username,@RequestParam("password") String password){
        if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        User u  = userService.query(user);
        if(u==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(u);
    }

}
