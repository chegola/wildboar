package com.wildboar.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wildboar.domain.Authority;
import com.wildboar.domain.SocialUserConnection;
import com.wildboar.domain.User;
import com.wildboar.repository.SocialUserConnectionRepository;
import com.wildboar.repository.UserRepository;
import com.wildboar.security.AuthoritiesConstants;
import com.wildboar.web.rest.errors.BadRequestAlertException;
import com.wildboar.web.rest.errors.EmailAlreadyUsedException;

@Service
public class LineSocialService {

	private final static String DEFAULT_LANGUAGE = "th";

	private final Logger log = LoggerFactory.getLogger(LineSocialService.class);
	

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    
    private final SocialUserConnectionRepository socialUserConnectionReository;
    
    
   
    public LineSocialService(SocialUserConnectionRepository socialUserConnectionReository,
            PasswordEncoder passwordEncoder, UserRepository userRepository, UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.socialUserConnectionReository = socialUserConnectionReository;
    }
    

	public User createSocialUser(String userName, String imageUrl, String providerUserId, String email) {
        return createUserIfNotExist(DEFAULT_LANGUAGE, imageUrl, userName, providerUserId, email);
    }


	private User createUserIfNotExist(String langKey,String imageUrl, String userName, String providerUserId, String email) {
		
		final SocialUserConnection socialUserConnection = socialUserConnectionReository
														.findOneByProviderIdAndProviderUserId("LINE", providerUserId);
		if (socialUserConnection != null) {
			Optional <User> user = userRepository.findOneByLogin(socialUserConnection.getUserId());
			if (user.isPresent()) {
				boolean profileShouldBeUpdated = false;
				if (!StringUtils.equals(user.get().getImageUrl(), imageUrl) || (!StringUtils.equals(user.get().getEmail(), email))) {
					profileShouldBeUpdated = true;					
				}				
				if (profileShouldBeUpdated) {
					user.get().setImageUrl(imageUrl);
					user.get().setEmail(email);
					userRepository.save(user.get());	
				}					
				return user.get();
			} else {
				// throw new IllegalArgumentException("Can not find user in jhi_user");
				log.error("Can not find user in jhi_user");
				throw new BadRequestAlertException("Can not find user","userManagement","usernotfound");
			}
			
		}
		
        if (StringUtils.isBlank(userName)) {
            log.error("Cannot create LINE user because login are null");
            throw new BadRequestAlertException("Cannot create LINE user because login are null","userManagement","emailnull");
            // throw new IllegalArgumentException("LINE login cannot be null");
        }
        if (userRepository.findOneByLogin(providerUserId).isPresent()) {
            log.error("Cannot create LINE user because login already exist, login -> {}", providerUserId);
            throw new BadRequestAlertException("Cannot create LINE user because login are null","userManagement","duplicatelogin");
            // throw new IllegalArgumentException("LINE cannot be with an existing login");
        }
        
        if (StringUtils.isNotBlank(email)) {
        	if (userRepository.findOneByEmailIgnoreCase(email).isPresent()) {
        		log.error("Cannot create LINE user because email already exist, email -> {}", email);
        		throw new EmailAlreadyUsedException();
        		// throw new IllegalArgumentException("LINE cannot be with an existing email");
        	}        	
        }
        
        final String login = providerUserId;
        final String encryptedPassword = passwordEncoder.encode(RandomStringUtils.random(10));
        final Set<Authority> authorities = new HashSet<>(1);
        final Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
		authorities.add(authority);
		
        final User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userName);
        newUser.setLastName("");
        newUser.setEmail(email);
        newUser.setActivated(true);
        newUser.setAuthorities(authorities);
        newUser.setLangKey(langKey);
        newUser.setImageUrl(imageUrl); 
        return userRepository.save(newUser);
    }

	public void createSocialConnection(String login, String accessToken, String providerUserId, String displayName, String imageURL) {
		SocialUserConnection socialUserConnection = socialUserConnectionReository.findOneByProviderIdAndProviderUserId(
					 "LINE", providerUserId);
		
		if (socialUserConnection != null) {
			return;
		}
		
		socialUserConnection = new SocialUserConnection();
		socialUserConnection.setUserId(providerUserId);
		socialUserConnection.setAccessToken(accessToken);
		socialUserConnection.setProviderId("LINE");
		socialUserConnection.setProviderUserId(providerUserId);
		socialUserConnection.setDisplayName(displayName);
		socialUserConnection.setImageURL(imageURL);
		socialUserConnection.setRank(1L);
		
 	   socialUserConnectionReository.save(socialUserConnection);
    }
}
