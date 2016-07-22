package ru.ardecs.hs.central.signing;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Signature;

@Component
@Aspect
public class SignatureProviderAspect {
	private static Logger logger = LoggerFactory.getLogger(SignatureProviderAspect.class);

	@Autowired
	private Signature publicSignature;

	@Pointcut(value = "execution(* getSignature(..))")
	public void getSignaturePointcut() {
	}

	@Pointcut("bean(signatureProvider)")
	public void signatureProviderPointcut() {
	}

	@Pointcut("getSignaturePointcut() && signatureProviderPointcut() && args(cityId)")
	public void getSignatureFromSignatureProvider(long cityId) {
	}

	@Around("getSignatureFromSignatureProvider(cityId)")
	public Object getPublicSignatureInsteadOfNull(ProceedingJoinPoint proceedingJoinPoint, long cityId) {
		logger.debug("Before invoking signatureProvider.getSignature({})", cityId);
		Signature value = null;
		try {
			value = (Signature) proceedingJoinPoint.proceed(new Object[]{cityId});
		} catch (Throwable e) {
			logger.error("Error when proceeding signatureProvider.getSignature({})", cityId, e);
			throw new RuntimeException(e);
		}
		logger.debug("After invoking signatureProvider.getSignature({}) method. Return value = {}", cityId, value);

		if (value == null) {
			logger.debug("Replace return value with public signature");
			value = publicSignature;
		}

		return value;
	}
}