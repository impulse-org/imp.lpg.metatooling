package org.jikes.lpg.runtime;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.uide.parser.Ast;

public interface LpgParser {
    public Ast parse(IProgressMonitor monitor) throws BadParseException;
    public int getToken(int index);
    public Object getSym(int index);
    public void setSym1(Object ast);
}
