package ua.illia.estore.configuration;

import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

import java.util.List;

public class PostgreSQLOverFunction implements SQLFunction {

    @Override
    public boolean hasArguments() {
        return false;
    }

    @Override
    public boolean hasParenthesesIfNoArguments() {
        return true;
    }

    @Override
    public Type getReturnType(final Type firstArgumentType, final Mapping mapping) throws QueryException {
        return new LongType();
    }

    @Override
    public String render(Type firstArgumentType, List args, SessionFactoryImplementor factory) throws QueryException {
        return "count(*) over()";
    }
}
