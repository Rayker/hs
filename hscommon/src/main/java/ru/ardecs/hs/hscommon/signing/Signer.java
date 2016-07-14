package ru.ardecs.hs.hscommon.signing;

public interface Signer {

	String sign(String xml) throws Exception;

	boolean validate(String xml) throws Exception;
}
