package kr.co.tworld.freebill.domain;

import java.io.Serializable;
import java.util.HashMap;

public class FreeBillMainVDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    //요금제코드
    private String prodId;
    //요금제명
    private String prodName;
    //요금제표시명
    private String skipName;
    //제공량
    private String baseQty;
    //사용량
    private String useQty;
    //잔여량
    private String remaindQty;
    //쿠폰등록일
    private String couponDate;
    //무제한여부
    private String isUnlimit;
    //퍼센트
    private String percent;
    //요금제종류(svcProdCd)
    private String skipType;
    //단위
    private String unit;
    //제공량(시분초)
    private HashMap editTotalQty;
    //사용량(시분초)
    private HashMap editUseQty;
    //잔여량(시분초)
    private HashMap editRemaindQty;
    //상품명(영문)
    private String prodEngNm;
    //공제명(영문)
    private String dedtItmEngNm;
    //공제코드
    private String skipCode;
    //서비스구분 (음성/영상/데이터/등등..) 
    private String codeType;
    //차감우선순위
    private String dcDedtPrty;
    //상품카테고리ID (기본요금제에 해당하는 공제항목인지 확인용)
    private String ctgCd;
    
    
    public String getCodeType()
    {
        return codeType;
    }
    public void setCodeType(String codeType)
    {
        this.codeType = codeType;
    }
    public String getDcDedtPrty()
    {
        return dcDedtPrty;
    }
    public void setDcDedtPrty(String dcDedtPrty)
    {
        this.dcDedtPrty = dcDedtPrty;
    }
    public String getCtgCd()
    {
        return ctgCd;
    }
    public void setCtgCd(String ctgCd)
    {
        this.ctgCd = ctgCd;
    }
    public String getSkipCode()
    {
        return skipCode;
    }
    public void setSkipCode(String skipCode)
    {
        this.skipCode = skipCode;
    }
    public String getProdId()
    {
        return prodId;
    }
    public void setProdId(String prodId)
    {
        this.prodId = prodId;
    }
    public String getProdName()
    {
        return prodName;
    }
    public void setProdName(String prodName)
    {
        this.prodName = prodName;
    }
    public String getSkipName()
    {
        return skipName;
    }
    public void setSkipName(String skipName)
    {
        this.skipName = skipName;
    }
    public String getBaseQty()
    {
        return baseQty;
    }
    public void setBaseQty(String baseQty)
    {
        this.baseQty = baseQty;
    }
    public String getUseQty()
    {
        return useQty;
    }
    public void setUseQty(String useQty)
    {
        this.useQty = useQty;
    }
    public String getRemaindQty()
    {
        return remaindQty;
    }
    public void setRemaindQty(String remaindQty)
    {
        this.remaindQty = remaindQty;
    }
    public String getCouponDate()
    {
        return couponDate;
    }
    public void setCouponDate(String couponDate)
    {
        this.couponDate = couponDate;
    }
    public String getIsUnlimit()
    {
        return isUnlimit;
    }
    public void setIsUnlimit(String isUnlimit)
    {
        this.isUnlimit = isUnlimit;
    }
    public String getPercent()
    {
        return percent;
    }
    public void setPercent(String percent)
    {
        this.percent = percent;
    }
    public String getSkipType()
    {
        return skipType;
    }
    public void setSkipType(String skipType)
    {
        this.skipType = skipType;
    }
    public String getUnit()
    {
        return unit;
    }
    public void setUnit(String unit)
    {
        this.unit = unit;
    }
    public HashMap getEditTotalQty()
    {
        return editTotalQty;
    }
    public void setEditTotalQty(HashMap editTotalQty)
    {
        this.editTotalQty = editTotalQty;
    }
    public HashMap getEditUseQty()
    {
        return editUseQty;
    }
    public void setEditUseQty(HashMap editUseQty)
    {
        this.editUseQty = editUseQty;
    }
    public HashMap getEditRemaindQty()
    {
        return editRemaindQty;
    }
    public void setEditRemaindQty(HashMap editRemaindQty)
    {
        this.editRemaindQty = editRemaindQty;
    }
    public String getProdEngNm()
    {
        return prodEngNm;
    }
    public void setProdEngNm(String prodEngNm)
    {
        this.prodEngNm = prodEngNm;
    }
    public String getDedtItmEngNm()
    {
        return dedtItmEngNm;
    }
    public void setDedtItmEngNm(String dedtItmEngNm)
    {
        this.dedtItmEngNm = dedtItmEngNm;
    }
}
