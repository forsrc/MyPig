package com.forsrc.common.core.dao.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.forsrc.common.core.dao.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageImpl<T> extends org.springframework.data.domain.PageImpl<T> implements Page<T> , Serializable {

    private static final long serialVersionUID = 867755909294344406L;

    public PageImpl() {
        super(new ArrayList<>());
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PageImpl(@JsonProperty("content") List<T> content,
                    @JsonProperty("number") int number,
                    @JsonProperty("size") int size,
                    @JsonProperty("totalElements") Long totalElements,
                    @JsonProperty("pageable") JsonNode pageable,
                    @JsonProperty("last") boolean last,
                    @JsonProperty("totalPages") int totalPages,
                    @JsonProperty("sort") JsonNode sort,
                    @JsonProperty("first") boolean first,
                    @JsonProperty("numberOfElements") int numberOfElements) {

        super(content, PageRequest.of(number, size), totalElements);
    }

    public PageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageImpl(List<T> content) {
        super(content);
    }
}
