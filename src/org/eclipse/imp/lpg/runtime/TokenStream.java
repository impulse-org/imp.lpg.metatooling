package org.jikes.lpg.runtime;

public interface TokenStream
{
    int getToken();

    int getToken(int end_token);

    int getKind(int i);

    int getNext(int i);

    int getPrevious(int i);

    String getName(int i);

    int peek();

    void reset(int i);

    void reset();

    int badToken();

    int getLine(int i);

    int getColumn(int i);

    int getEndLine(int i);

    int getEndColumn(int i);

    boolean afterEol(int i);

    public String getFileName();

    void reportError(int i, String code);

	void reportError(int errorCode, String locationInfo, String tokenText);

	void reportError(int errorCode, String locationInfo, int leftToken, int rightToken, String tokenText);

	boolean isValidForParser();
}