package org.jikes.lpg.runtime;

public class BadParseException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 3760847874103259952L;
    public int error_token;
    public BadParseException(int error_token)
    {
        this.error_token = error_token;
    }
}
