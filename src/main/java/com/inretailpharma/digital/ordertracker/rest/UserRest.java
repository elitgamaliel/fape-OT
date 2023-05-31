package com.inretailpharma.digital.ordertracker.rest;

import javax.validation.Valid;

import com.inretailpharma.digital.ordertracker.exception.InvalidRequestException;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.inretailpharma.digital.ordertracker.config.aditional.SecurityUtils;
import com.inretailpharma.digital.ordertracker.dto.LoginDto;
import com.inretailpharma.digital.ordertracker.dto.ResetPasswordDto;
import com.inretailpharma.digital.ordertracker.dto.ResponseDTO;
import com.inretailpharma.digital.ordertracker.dto.UserDto;
import com.inretailpharma.digital.ordertracker.dto.in.CodeOut;
import com.inretailpharma.digital.ordertracker.dto.in.SendSmsIn;
import com.inretailpharma.digital.ordertracker.facade.UserFacade;
import com.inretailpharma.digital.ordertracker.utils.Util;

@RestController
public class UserRest {

    @Autowired
    private UserFacade userFacade;

    @PostMapping("/v2/user/register")
    public ResponseDTO<String> motorizedLogin(@RequestBody LoginDto loginDto) {
        return userFacade.register(loginDto, SecurityUtils.getUID());
    }

    @PutMapping("/user/unregister")
    public ResponseDTO<String> motorizedLogout() {
        return userFacade.unRegister(SecurityUtils.getUID());
    }

    @PostMapping("/user/generatecodesms")
    public CodeOut sendSmsRest(@RequestBody SendSmsIn sendSmsIn) {
        return new CodeOut(userFacade.generateCodeSms(sendSmsIn));
    }
    
    @PostMapping("/nvr/user")
    public void create(@RequestBody @Valid UserDto userDto) {
        userFacade.create(userDto);        
    }
    
    @PutMapping("/nvr/user/{id}")
    public void update(@PathVariable(name = "id") String id, @RequestBody @Valid UserDto userDto) {
    	userDto.setId(id);
        userFacade.update(userDto);        
    }    

    @GetMapping("/user/test")
    public String find() {
    	return Util.generateTrackingCode();
    }
    
    @PutMapping("/user/password")
    public void resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {    	
    	userFacade.resertPassword(resetPasswordDto);    	
    }

    //old version
    @PostMapping("/user/register")
    public void motorizedLogin() {
        throw new InvalidRequestException(Constant.Error.VERSION_ERROR);
    }

}