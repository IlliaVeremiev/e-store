package ua.illia.estore.configuration;

import com.vladmihalcea.hibernate.type.search.PostgreSQLTSVectorType;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.BooleanType;
import org.hibernate.type.LocalDateType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;

public class SqlFunctionsContributor implements MetadataBuilderContributor {

    public static final String FULL_TEXT_SEARCH = "full_text_search";
    public static final String DATE_TRUNC = "date_trunc";
    public static final String TO_TSVECTOR = "to_tsvector";
    public static final String PLAINTO_TSQUERY = "plainto_tsquery";
    public static final String FTS_MATCH = "@@";
    public static final String CONCAT = "concat";
    public static final String ARROW_GET_JSON_ITEM = "->";
    public static final String ARROW_GET_JSON_STRING = "->>";

    @Override
    public void contribute(MetadataBuilder metadataBuilder) {
        metadataBuilder.applySqlFunction(FULL_TEXT_SEARCH,
                new SQLFunctionTemplate(BooleanType.INSTANCE, "to_tsvector('russian', ?1) @@ plainto_tsquery('russian', ?2)"));
        metadataBuilder.applySqlFunction(DATE_TRUNC,
                new SQLFunctionTemplate(LocalDateType.INSTANCE, "date_trunc(?1, ?2)"));
        metadataBuilder.applySqlFunction(TO_TSVECTOR,
                new SQLFunctionTemplate(PostgreSQLTSVectorType.INSTANCE, "to_tsvector('russian', ?1)"));
        metadataBuilder.applySqlFunction(PLAINTO_TSQUERY,
                new SQLFunctionTemplate(PostgreSQLTSVectorType.INSTANCE, "plainto_tsquery('russian', ?1)"));
        metadataBuilder.applySqlFunction(FTS_MATCH,
                new SQLFunctionTemplate(BooleanType.INSTANCE, "?1 @@ ?2"));
        metadataBuilder.applySqlFunction(CONCAT,
                new StandardSQLFunction(CONCAT, StandardBasicTypes.STRING));
        metadataBuilder.applySqlFunction(ARROW_GET_JSON_ITEM,
                new SQLFunctionTemplate(StringType.INSTANCE, "?1->?2"));
        metadataBuilder.applySqlFunction(ARROW_GET_JSON_STRING,
                new SQLFunctionTemplate(StringType.INSTANCE, "?1->>?2"));
    }
}
