package org.acme.util.query;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

/**
 * QueryParameters
 * <p>
 * Deserialized form of query-parameters that can be used in GET-Requests for remote-pagination.
 * </p>
 */
@Schema(name = "QueryParameters", description = "Query parameters for pagination, sorting, and filtering")
public class QueryParameters {

    @QueryParam("page")
    @Schema(description = "Page number (starting from 0)", defaultValue = "0")
    private int page;

    @QueryParam("pageSize")
    @Schema(description = "Number of items per page", defaultValue = "10")
    private int pageSize;

    @QueryParam("sort")
    @Schema(description = "Sort order (e.g. name:asc)", defaultValue = "productId:asc")
    private String sort;

    @QueryParam("filter")
    @Schema(description = "Filter criteria (e.g. name:John,age:gt:30)", defaultValue = "product.productId.equal:productId=2,product_lang.name.like:name=Mira Eck Glas USB A")
    private String filter;

    @Context
    protected UriInfo uriInfo;

    private Sorter sorter;
    private List<Filter> filters;


    @PostConstruct
    void init() {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        this.page = Integer.parseInt(queryParams.getFirst("page"));
        this.pageSize = Integer.parseInt(queryParams.getFirst("pageSize"));

        // Parse sort parameter
        String sorterString = queryParams.getFirst("sort");
        this.sorter = Sorter.parseSorterString(sorterString);

        // Parse filter parameters
        String filterStrings = queryParams.getFirst("filter");
        this.filters = Filter.parseFilterString(filterStrings);

    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public Sorter getSorter() {
        return sorter;
    }
}