package ua.illia.estore.configuration;

import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.BooleanType;
import org.hibernate.type.Type;

import java.util.List;
import java.util.StringJoiner;

public class PostgreSQLFTSFunction implements SQLFunction {

    @Override
    public boolean hasArguments() {
        return true;
    }

    @Override
    public boolean hasParenthesesIfNoArguments() {
        return false;
    }

    @Override
    public Type getReturnType(final Type firstArgumentType, final Mapping mapping) throws QueryException {
        return new BooleanType();
    }

    @Override
    public String render(Type firstArgumentType, List args, SessionFactoryImplementor factory) throws QueryException {
        if (args == null || args.size() < 2) {
            throw new IllegalArgumentException("The function must be passed at least 2 arguments");
        }
        StringJoiner vectorsJoiner = new StringJoiner(", ");
        for (int i = 0; i < args.size() - 1; i++) {
            vectorsJoiner.add("to_tsvector(" + args.get(i) + ")");
        }
        return "concat(" + vectorsJoiner + ") @@ plainto_tsquery(" + args.get(args.size() - 1) + ")";
    }
}
