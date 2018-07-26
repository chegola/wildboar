/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.wildboar.social.linecorp.controller;

import java.net.URISyntaxException;
import java.util.Arrays;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.support.URIBuilder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wildboar.config.ApplicationProperties;
import com.wildboar.domain.User;
import com.wildboar.security.jwt.JWTConfigurer;
import com.wildboar.security.jwt.TokenProvider;
import com.wildboar.social.linecorp.infra.utils.CommonUtils;
import com.wildboar.social.linecorp.line.api.v2.LineAPIService;
import com.wildboar.social.linecorp.line.api.v2.response.AccessToken;
import com.wildboar.social.linecorp.line.api.v2.response.IdToken;
import com.wildboar.service.LineSocialService;

/**
 * <p>user web application pages</p>
 */
@RestController
@RequestMapping("/api/line")
public class WebController {

    private static final String LINE_WEB_LOGIN_STATE = "lineWebLoginState";
    private static final String NONCE = "nonce";
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    static final String ACCESS_TOKEN = "accessToken";
    
    @Autowired
    private LineAPIService lineAPIService;
    
    @Autowired
    private LineSocialService lineSocialService;
    
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenProvider tokenProvider;
    
    @Autowired
    private ApplicationProperties applicationProperties;
    /**
     * <p>Redirect to LINE Login Page</p>
     */
    @GetMapping("/lineUrl")
    public LineLogin goToAuthPage(HttpSession httpSession){
    	logger.debug("REST request to generate LINE Url Auth Page");
        final String state = CommonUtils.getToken();
        final String nonce = CommonUtils.getToken();
        httpSession.setAttribute(LINE_WEB_LOGIN_STATE, state);
        httpSession.setAttribute(NONCE, nonce);
        final String url = lineAPIService.getLineWebLoginUrl(state, nonce, Arrays.asList("openid", "profile", "email"));
        logger.debug("LINE Url:" + url);
        LineLogin lineLogin = new LineLogin();
        lineLogin.setLineUrl(url);
        return lineLogin;
    }

    
    /**
     * <p>Redirect Page from LINE Platform</p>
     * <p>Login Type is to log in on any desktop or mobile website
     */
    @RequestMapping("/auth")
    public ModelAndView auth (
            HttpSession httpSession, Model model,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "scope", required = false) String scope,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "errorCode", required = false) String errorCode,
            @RequestParam(value = "errorMessage", required = false) String errorMessage,
            @RequestParam(value = "friendship_status_changed", required = false) String friendship_status_changed) {

        if (logger.isDebugEnabled()) {
            logger.debug("parameter code : " + code);
            logger.debug("parameter state : " + state);
            logger.debug("parameter scope : " + scope);
            logger.debug("parameter error : " + error);
            logger.debug("parameter errorCode : " + errorCode);
            logger.debug("parameter errorMessage : " + errorMessage);
            logger.debug("parameter friendship_status_changed : " + friendship_status_changed);
            
        }

        if (error != null || errorCode != null || errorMessage != null){
        	return this.createModelAndView("false");
        }

        if (!state.equals(httpSession.getAttribute(LINE_WEB_LOGIN_STATE))){
        	return this.createModelAndView("false");
        }

        httpSession.removeAttribute(LINE_WEB_LOGIN_STATE);
        AccessToken token = lineAPIService.accessToken(code);
        if (logger.isDebugEnabled()) {
            logger.debug("scope : " + token.scope);
            logger.debug("access_token : " + token.access_token);
            logger.debug("token_type : " + token.token_type);
            logger.debug("expires_in : " + token.expires_in);
            logger.debug("refresh_token : " + token.refresh_token);
            logger.debug("id_token : " + token.id_token);
        }
        httpSession.setAttribute(ACCESS_TOKEN, token);
        logger.debug("LINE success URL:" + applicationProperties.getLine().getSuccessURL());
        return this.createModelAndView("true");
    }
    
    private ModelAndView createModelAndView(String success) {
    	ModelAndView modelAndView;
    		modelAndView =  new ModelAndView(new RedirectView(URIBuilder.fromUri(applicationProperties.getLine().getSuccessURL())
                    .queryParam("success", success)
                    .build().toString(), true));
    	return modelAndView;
    }
    
    
    /**
     * <p>login success Page
     * @throws URISyntaxException 
     */

    @GetMapping("/authLine")
    @Timed
    public ResponseEntity<LineLogin> authLine(HttpSession httpSession, Model model) throws URISyntaxException {
    	String jwt = this.success(httpSession, model);
    	final LineLogin lineLogin = new LineLogin();
    	HttpHeaders httpHeaders = new HttpHeaders();
    	
    	if (jwt.isEmpty()) {
            return new ResponseEntity<>(lineLogin, httpHeaders, HttpStatus.UNAUTHORIZED);
    	} else {
    		httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);    	    	
    		lineLogin.setToken(jwt);
    		return new ResponseEntity<>(lineLogin, httpHeaders, HttpStatus.OK);    		
    	}
        		
     }
    
   /**
    * <p>login success Page
    */
    // @RequestMapping("/success")
    public String success(HttpSession httpSession, Model model) {

        AccessToken token = (AccessToken)httpSession.getAttribute(ACCESS_TOKEN);
        if (token == null){
            return "";
        }

        if (!lineAPIService.verifyIdToken(token.id_token, (String) httpSession.getAttribute(NONCE))) {
            // verify failed
            return "";
        }

        httpSession.removeAttribute(NONCE);
        IdToken idToken = lineAPIService.idToken(token.id_token);
        if (logger.isDebugEnabled()) {
            logger.debug("userId : " + idToken.sub);
            logger.debug("displayName : " + idToken.name);
            logger.debug("pictureUrl : " + idToken.picture);
            logger.debug("email : " + idToken.email);
        }
        model.addAttribute("idToken", idToken);
        String userName = idToken.name.replaceAll(" {1,}","");
        userName = userName.replaceAll("//$", "");
        userName = userName.replaceAll("//*", "");
        User user = this.createUser(userName, idToken.picture, idToken.sub, idToken.email);
        if (null != user) {
        	this.createSocialUserConnection(userName, token.id_token, idToken.sub, userName, idToken.picture);
        	String jwt = this.signIn(user);
        	return jwt;        	
        } else {
        	return "";
        }
    }
    

    private String signIn (User userParam) {
    	String jwt = ""; 
    	try {
    		 UserDetails user = userDetailsService.loadUserByUsername(userParam.getLogin());
    		 
             UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                 user,
                 null,
                 user.getAuthorities());

             SecurityContextHolder.getContext().setAuthentication(authenticationToken);
             jwt = tokenProvider.createToken(authenticationToken, false);
            
         } catch (AuthenticationException ae) {
             logger.error("Social authentication error");
             logger.error("Authentication exception trace: {}", ae);
         }
    	 return jwt;
    }

    private User createUser(String userName, String imageUrl, String providerUserId, String email) {
    	return lineSocialService.createSocialUser(userName, imageUrl, providerUserId, email);
    }
    
    private void createSocialUserConnection(String userName, String token, String providerUserId, 
    		String displayName, String imageURL) {
    	lineSocialService.createSocialConnection(userName, token, providerUserId, displayName, imageURL);
    }
    
 
    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }

}
