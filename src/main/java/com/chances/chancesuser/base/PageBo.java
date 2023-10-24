package com.chances.chancesuser.base;

import lombok.Data;

@Data
public abstract class PageBo {
    private Integer pageSize;
    private Integer pageNum;
}
