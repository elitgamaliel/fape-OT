package com.inretailpharma.digital.ordertracker.canonical.aspect;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.inretailpharma.digital.ordertracker.canonical.properties.ExternalUserProperties;
import com.inretailpharma.digital.ordertracker.exception.AuthException;

@Aspect
@Configuration
@EnableConfigurationProperties(ExternalUserProperties.class)
@EnableAspectJAutoProxy(proxyTargetClass=true)
@ComponentScan("com.inretailpharma.digital.ordertracker.*")
public class ExternalMotorizedAspect {
	private static final Logger logger = LogManager.getLogger(ExternalMotorizedAspect.class);
	
	@Autowired
    ExternalUserProperties externalUserProperties;
 
	@Before("execution(* com.inretailpharma.digital.ordertracker.rest.TrackerRest.*(..)) && execution(public * *(..))")
	public Object verifyToken(final JoinPoint proceedingJoinPoint) throws AuthException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();		
		Object value;
		try {
			value = proceedingJoinPoint.getTarget();		
			String headerBearer = request.getHeader("Authorization");			
			if(!headerBearer.contains("Basic")) {
				throw new AuthException("seguridad-->petición no contiene Basic Auth...");
			} 			
			byte[] credentialDecodedBytes = Base64.getDecoder().decode(headerBearer.substring("Basic".length()).trim());
			String credentialDecodedString = new String(credentialDecodedBytes);
			final String[] values = credentialDecodedString.split(":", 2);
			
			logger.info("aspect:Authorization Credential..."+credentialDecodedString);
			logger.info("externalUserProperties..."+externalUserProperties); 
			
			int numeroUsuarios= externalUserProperties.getName().size();
			boolean existeUsuario=false;
			for(int i=0;i<numeroUsuarios;i++) {
				if(values[0].equals(externalUserProperties.getName().get(i)) 
						&& values[1].equals(externalUserProperties.getPassword().get(i))) {
					existeUsuario=true;
					break;
				}
			}				
			if(!existeUsuario) {
				throw new AuthException("seguridad-->credenciales incorrectas...");
			}
		} catch (Exception ex) {
			throw new AuthException(ex);
		} finally {
			logger.info("{} {} from {}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr(),request.getHeader("user-id"));
		}
		logger.info("value..." + value);
		return value;
	}
}