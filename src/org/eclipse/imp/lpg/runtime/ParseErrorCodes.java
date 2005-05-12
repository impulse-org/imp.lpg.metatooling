package org.jikes.lpg.runtime;

public interface ParseErrorCodes
{
    public static final int  LEX_ERROR = 0,
    			     ERROR_CODE = 1,
                             BEFORE_CODE = 2,
                             INSERTION_CODE = 3,
                             INVALID_CODE = 4,
                             SUBSTITUTION_CODE = 5,
                             DELETION_CODE = 6,
                             MERGE_CODE = 7,
                             MISPLACED_CODE = 8,
                             SCOPE_CODE = 9,
                             SECONDARY_CODE = 10,
                             EOF_CODE = 11,
                             INVALID_TOKEN_CODE = 12;

    public static final String errorMsgText[] = {
        /* LEX_ERROR */		"unexpected character ignored",
        /* ERROR_CODE */	"parsing terminated at this token",
        /* BEFORE_CODE */	"inserted before this token",
        /* INSERTION_CODE */	"expected after this token",
        /* INVALID_CODE */	"unexpected input discarded",
        /* SUBSTITUTION_CODE */	"expected instead of this token",
        /* DELETION_CODE */	"unexpected token(s) ignored",
        /* MERGE_CODE */	"formed from merged tokens",
        /* MISPLACED_CODE */	"misplaced construct(s)",
        /* SCOPE_CODE */	"inserted to complete scope",
        /* SECONDARY_CODE */	"ignored input",
        /* EOF_CODE */		"reached after this token",
        /* INVALID_TOKEN_CODE */"is invalid" };

}
        