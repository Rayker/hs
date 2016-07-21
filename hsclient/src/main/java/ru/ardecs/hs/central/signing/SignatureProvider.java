package ru.ardecs.hs.central.signing;

import java.security.Signature;

public interface SignatureProvider {
	Signature getSignature(long cityId);
}
