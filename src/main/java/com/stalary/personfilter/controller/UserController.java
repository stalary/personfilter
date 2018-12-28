package com.stalary.personfilter.controller;

import com.stalary.personfilter.annotation.LoginRequired;
import com.stalary.personfilter.data.dto.Applicant;
import com.stalary.personfilter.data.dto.HR;
import com.stalary.personfilter.data.dto.User;
import com.stalary.personfilter.data.entity.mysql.UserInfo;
import com.stalary.personfilter.data.vo.LoginVo;
import com.stalary.personfilter.data.vo.ResponseMessage;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.service.ClientService;
import com.stalary.personfilter.service.mysql.MessageService;
import com.stalary.personfilter.service.mysql.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

import static com.stalary.personfilter.utils.Constant.*;

/**
 * UserController
 *
 * @description 用户操作接口
 * @author lirongqian
 * @since 2018/04/13
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private ClientService clientService;

    @Resource
    private UserService userService;

    @Resource
    private MessageService messageService;

    /**
     * @method register 求职者注册
     * @param applicant 求职者对象
     * @return 0 token
     **/
    @PostMapping
    public ResponseMessage register(
            @RequestBody Applicant applicant) {
        // todo: 前端应该改为注册完就跳转
        return clientService.postUser(applicant, REGISTER);
    }

    /**
     * @method login 传入登陆对象，仅需要用户名和密码
     * @param user 用户对象
     * @return LoginVo 登陆对象
     **/
    @PostMapping("/login")
    public ResponseMessage login(
            @RequestBody User user) {
        ResponseMessage responseMessage = clientService.postUser(user, LOGIN);
        if (!responseMessage.isSuccess()) {
            return responseMessage;
        }
        String token = responseMessage.getData().toString();
        User getUser = clientService.getUser(token);
        LoginVo loginVo = new LoginVo(token, getUser.getRole(), getUser.getId(), getUser.getFirstId());
        messageService.getCount();
        return ResponseMessage.successMessage(loginVo);
    }

    /**
     * @method hrRegister hr注册
     * @param hr hr对象
     * @return 0 token
     **/
    @PostMapping("/hr")
    public ResponseMessage hrRegister(
            @RequestBody HR hr) {
        return clientService.postUser(hr, REGISTER);
    }

    /**
     * @method logout 退出登录
     * @return 0 退出成功
     **/
    @GetMapping("/logout")
    @LoginRequired
    public ResponseMessage logout() {
        // 在拦截器中进行删除操作
        return ResponseMessage.successMessage("退出成功");
    }

    /**
     * @method getInfo header中带入token获取用户信息
     * @return UserInfo 用户信息
     **/
    @GetMapping
    @LoginRequired
    public ResponseMessage getInfo() {
        return ResponseMessage.successMessage(userService.getInfo());
    }

    /**
     * @method updateInfo 修改用户信息
     * @param userInfo 用户信息对象
     * @return UserInfo 用户信息
     **/
    @PutMapping("/info")
    @LoginRequired
    public ResponseMessage updateInfo(
            @RequestBody UserInfo userInfo) {
        User user = UserHolder.get();
        userInfo.setUserId(user.getId());
        userInfo.setUsername(user.getUsername());
        return ResponseMessage.successMessage(userService.save(userInfo));
    }

    /**
     * @method updatePhone 修改手机号
     * @param params 手机号
     */
    @PutMapping("/phone")
    @LoginRequired
    public ResponseMessage updatePhone(
            @RequestBody Map<String, String> params) {
        User user = UserHolder.get();
        user.setPhone(params.get("phone"));
        return clientService.postUser(user, UPDATE);
    }

    /**
     * @method updatePhone 修改密码，通过新密码进行修改
     * @param params 新密码
     */
    @PutMapping("/password")
    @LoginRequired
    public ResponseMessage updatePassword(
            @RequestBody Map<String, String> params) {
        User user = UserHolder.get();
        user.setPassword(params.get("password"));
        return clientService.postUser(user, UPDATE_PASSWORD);
    }

    /**
     * @method forgetPassword 忘记密码，通过用户名，手机号，新密码进行修改
     * @param params 参数
     **/
    @PostMapping("/password")
    public ResponseMessage forgetPassword(
            @RequestBody Map<String, String> params) {
        User user = new User();
        user.setPassword(params.get("password"));
        user.setUsername(params.get("username"));
        user.setPhone(params.get("phone"));
        return clientService.postUser(user, UPDATE_PASSWORD);
    }

    /**
     * @method updateEmail 修改邮箱
     * @param params 邮箱
     */
    @PutMapping("/email")
    @LoginRequired
    public ResponseMessage updateEmail(
            @RequestBody Map<String, String> params) {
        User user = UserHolder.get();
        user.setEmail(params.get("email"));
        return clientService.postUser(user, UPDATE);
    }

    /**
     * @method token 使用token获取用户信息
     * @param token token
     * @return User 用户信息
     **/
    @GetMapping("/token")
    public ResponseMessage token(
            @RequestParam String token) {
        return ResponseMessage.successMessage(clientService.getUser(token));
    }

    /**
     * @method upload 上传用户头像
     * @param avatar 头像
     **/
    @PostMapping("/avatar")
    @LoginRequired
    public ResponseMessage upload(
            @RequestParam("avatar") MultipartFile avatar) {
        userService.uploadAvatar(avatar);
        return ResponseMessage.successMessage("头像上传成功");
    }
}