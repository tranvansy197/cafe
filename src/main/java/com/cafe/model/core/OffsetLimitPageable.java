package com.cafe.model.core;


import com.cafe.server.exception.GeneralException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetLimitPageable implements Pageable {
    private final long offset;
    private final int limit;
    private final Sort sort;

    public OffsetLimitPageable(long offset, int limit, Sort sort) {
        if (offset < 0) {
            throw new GeneralException("Page offset must not be less than zero!");
        }
        if (limit < 1) {
            throw new GeneralException("Page limit must not be less than one!");
        }
        this.offset = offset;
        this.limit = limit;
        this.sort = sort;
    }

    @Override
    public int getPageNumber() {
        return (int) (offset / limit);
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetLimitPageable(offset + limit, limit, sort);
    }

    @Override
    public Pageable previousOrFirst() {
        return new OffsetLimitPageable((Math.max(offset - limit, 0)), limit, sort);
    }

    @Override
    public Pageable first() {
        return new OffsetLimitPageable(0, limit, sort);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new OffsetLimitPageable((long) pageNumber * getPageSize(), getPageSize(), getSort());
    }

    @Override
    public boolean hasPrevious() {
        return offset > 0;
    }

}
