package com.chengqianyun.eeweb2networkadmin.biz.bean.export;

import com.chengqianyun.eeweb2networkadmin.core.utils.BizConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkStyleBean {

    private int dataRowIndex;

    private int dataColIndex;

    // 1：low; 2:high
    private int markType;

    public String genKey() {
        return dataRowIndex + BizConstant.SPLIT + dataColIndex;
    }

    public boolean isLow() {
        return markType == 1;
    }

    public boolean isHigh() {
        return markType == 2;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MarkStyleBean)) {
            return false;
        }
        MarkStyleBean tmp = (MarkStyleBean) obj;
        if (this == tmp) {
            return true;
        }
        return tmp.dataColIndex == this.dataColIndex && tmp.dataRowIndex == this.dataRowIndex;
    }

    @Override
    public int hashCode() {
        return this.dataRowIndex + this.dataColIndex;
    }
}
