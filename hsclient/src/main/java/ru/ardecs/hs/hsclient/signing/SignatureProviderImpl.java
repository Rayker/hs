package ru.ardecs.hs.hsclient.signing;

import java.security.Signature;
import java.util.Map;

public class SignatureProviderImpl implements SignatureProvider {
	private final Map<Long, Signature> signatures;

	public SignatureProviderImpl(Map<Long, Signature> signatures) {
		this.signatures = signatures;
	}

	@Override
	public Signature getSignature(long cityId) {
		return signatures.get(cityId);
	}
}
