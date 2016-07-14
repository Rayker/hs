package ru.ardecs.hs.hsclient.signing;

import java.security.Signature;

public interface SignatureProvider {
	Signature getSignature(long cityId);
}
