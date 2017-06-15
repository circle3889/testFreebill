package kr.co.tworld.freebill.domain;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreeBillUtil implements Comparator {
    
	/**
	 * 잔여기본통화 데이타를 구성한다. (메인용)
	 * @param freeBillList
	 * @return
	 * @throws Exception
	 */
    public Map editPlanFreeForMain(List freeBillList) throws Exception    
    {
        
        Map returnMap                   = new HashMap();
        List freeBillEditList              = new ArrayList();
        StringBuffer logSb = new StringBuffer();
        String failCode = "";  //잔여기본통화 조회불가 케이스일 경우 코드
        int totalCnt = freeBillList.size();
        
        for (int i=0; i<totalCnt; i++)
        {
            List l_detail = (List) freeBillList.get(i);
            FreeBillMainVDTO freeBillMainVDTO = new FreeBillMainVDTO();
            
            //단위가 분일때 '시','분','초'를 담는다.
            HashMap hourMinSec0 = new HashMap();
            HashMap hourMinSec1 = new HashMap();
            HashMap hourMinSec2 = new HashMap();
            
            String planId              = (String) l_detail.get(0); //상품코드
            String planName        = (String) l_detail.get(1); //상품명
            String skipCode         = (String) l_detail.get(2); //공제코드
            String freePlanName  = (String) l_detail.get(3); //무료혜택명
            String totalQty           = (String) l_detail.get(4); //제공량
            String useQty            = (String) l_detail.get(5); //사용량
            String remainQty       = (String) l_detail.get(6); //잔여량            
            String unitCode         = (String) l_detail.get(7); //단위
            String couponRegiDate = (String) l_detail.get(8); //등록일 (->쿠폰등록일 등)
            String unlimitType      = (String) l_detail.get(9); //무제한종류
            String originCodeType =   (String)l_detail.get(11); //서비스구분 (음성/영상/데이터/등등..)
            String dcDedtPrty = (String)l_detail.get(12); //차감우선순위
            String codeType = null;
            
            for ( int j=0; j<l_detail.size(); j++ ) {
                logSb.append("\n## v_detail("+j+")="+((String)l_detail.get(j)));
            }
            
            /*
             * [잔여기본통화 안보여주는 경우]
                XXXXXX : 당월에 기존 요금제로 재변경되어 서비스 조회 불가
                RRRRRR  : 이월에 기존 요금제로 재변경되어 서비스 조회 불가
                CURRENT_MONTH: 당월정지이력 있는 경우
                LAST_MONTH: 전월정지이력 있는 경우
                
                [잔여기본통화 보여주는 경우]
                YYYYYY : 당월에 기존 요금제로 재변경되어 조회된 무료통화잔액은 변동될 수 있습니다
             */
            if ("LAST_MONTH".equals(planId) 
                || "CURRENT_MONTH".equals(planId)
                || "RRRRRR".equals(skipCode)
                || "XXXXXX".equals(skipCode) ){
                
                if ("LAST_MONTH".equals(planId) || "CURRENT_MONTH".equals(planId)){
                    failCode = planId;
                }else if ("RRRRRR".equals(skipCode) || "XXXXXX".equals(skipCode)){
                    failCode = skipCode;
                }
                continue;
                
            }
            
            else if("DP".equals(skipCode)){ // DP 가 뭐임?
                continue;
            }
            
            else{
                
              //공제항목을 보여줄지 여부 확인 (캐시에서 확인) - 노출할 상품정보 관리하는 곳 
//              boolean isAllowProduct = FreeBillProductMapper.getInstance().isAllowProduct(planId);
//              boolean isAllowSkipCode = FreeBillSkipCodeMapper.getInstance().isAllowSkipCode(skipCode);
//              String ctgCd = (String)FreeBillProductMapper.getInstance().getCtgCd(planId);
//              String svcProdCd = (String)FreeBillProductMapper.getInstance().getSvcProdCd(planId);
            	
                boolean isAllowProduct = true;
                boolean isAllowSkipCode = true;
                String ctgCd = "A";
                String svcProdCd = "NA00004120";   
                
              if ( isAllowProduct && isAllowSkipCode )                
                {
                    freeBillMainVDTO.setProdId(planId);
                    freeBillMainVDTO.setProdName(StringUtil.replaceToCodeFromNoot(planName));
                    
                    // 다다익선 요금제만 이월 표시
                    if (("NA00001996".equals(planId) || "NA00001686".equals(planId) || "NA00001687".equals(planId) || "NA00001997".equals(planId))  
                        && ("RD".equals(skipCode.substring(0, 2)))){
                        freeBillMainVDTO.setSkipName((StringUtil.replaceToCodeFromNoot(freePlanName)) + "(이월)");
                    }else{
                        freeBillMainVDTO.setSkipName(StringUtil.replaceToCodeFromNoot(freePlanName)); // 무료혜택명
                    }
                    
                    String cdataPercent = "0";
                    String cdata0 = "";
                    String cdata1 = "";
                    String cdata2 = "";
                    long iData0 = NumberUtil.parseLong(totalQty, 0);
                    long iData1 = NumberUtil.parseLong(useQty, 0);
                    long iData2 = NumberUtil.parseLong(remainQty, 0);
                    
                    if ("1".equals(unlimitType))
                    {
                        cdata0 = "무제한";
                        cdata1 = "무제한";
                        cdata2 = "무제한";
                        cdataPercent = "100";
                        
                        if("2".equals(originCodeType)){
                        	cdata0 = "기본제공";
                            cdata1 = "기본제공";
                            cdata2 = "기본제공";
                        }
                    }
                    else if ("B".equals(unlimitType))
                    {
                        cdata0 = "기본제공";
                        cdata1 = "기본제공";
                        cdata2 = "기본제공";
                        
                        cdataPercent = "100";
                    }
                    else if ("M".equals(unlimitType)) // 올인원 요금제 특수 케이스
                    {
                        cdata0 = "무제한";
                        if ("240".equals(unitCode)){
                            cdata1 = getMinVal(useQty);
                            hourMinSec1 = getHourMinSec(NumberUtil.parseInt(StringUtil.trim(useQty), 0));
                        }else{
                            cdata1 = getNumberFormat(useQty);
                        }
                        cdata2 = "무제한";
                        cdataPercent = "100";
                        
                        if("2".equals(originCodeType)){
                        	cdata0 = "기본제공";
                            cdata2 = "기본제공";
                        }
                    }
                    else
                    {
                        if ("240".equals(unitCode))
                        {
                            cdata0 = getMinVal(totalQty);
                            cdata1 = getMinVal(useQty);
                            cdata2 = getMinVal(remainQty);
                            
                            hourMinSec0 = getHourMinSec(NumberUtil.parseInt(StringUtil.trim(totalQty), 0));
                            hourMinSec1 = getHourMinSec(NumberUtil.parseInt(StringUtil.trim(useQty), 0));
                            hourMinSec2 = getHourMinSec(NumberUtil.parseInt(StringUtil.trim(remainQty), 0));
                        }else{
                            cdata0 = getNumberFormat(totalQty);
                            cdata1 = getNumberFormat(useQty);
                            cdata2 = getNumberFormat(remainQty);
                        }
                        
                        //잔여량퍼센트 = (잔여량 * 100 / (사용량+잔여량))
                        if(iData2 != 0){
                            long longPercent = iData2 * 100 / (iData1+iData2);
                            if(longPercent <= 1){
                                cdataPercent = "1";
                            }else{
                                cdataPercent = String.valueOf(longPercent);
                            }
                        }
                    }
                    
                    //공제항목 종류 구하기
                    codeType = getCodeType(originCodeType);
                    
                    freeBillMainVDTO.setBaseQty(cdata0);
                    freeBillMainVDTO.setUseQty(cdata1);
                    freeBillMainVDTO.setRemaindQty(cdata2);
                    freeBillMainVDTO.setCouponDate(couponRegiDate);
                    freeBillMainVDTO.setIsUnlimit(unlimitType);
                    freeBillMainVDTO.setPercent(cdataPercent);
                    freeBillMainVDTO.setSkipType(svcProdCd);
                    freeBillMainVDTO.setUnit(unitCode);
                    freeBillMainVDTO.setEditTotalQty(hourMinSec0);
                    freeBillMainVDTO.setEditUseQty(hourMinSec1);
                    freeBillMainVDTO.setEditRemaindQty(hourMinSec2);
                    freeBillMainVDTO.setSkipCode(skipCode);
                    freeBillMainVDTO.setCodeType(codeType);
                    freeBillMainVDTO.setDcDedtPrty(dcDedtPrty);
                    freeBillMainVDTO.setCtgCd(ctgCd);
                    
                    if ( !("HA".equals(skipCode) || "CH".equals(skipCode) ||"PR".equals(skipCode)) ) {
                        
                        freeBillEditList.add(freeBillMainVDTO);
                        Collections.sort(freeBillEditList, this);
                    }
                }
            }
        }
        
        
        
        
        //기본 정의 : 음성/영상, 문자, 데이터 각 카테고리 별 ‘현재 차감중인 항목’이 노출
        //현재 차감중인 항목이 복수인 경우 기본요금제의 제공량이 우선순위
        //음성/영상, 문자, 데이터 각 카테고리 내 ‘현재 차감중인 항목’이 없고 모두 무제한인 경우
        //카테고리 내 항목 간 우선순위에 따라 노출 (현재기준)
        FreeBillMainVDTO voiceAndpictureDTO = null;
        FreeBillMainVDTO smsDTO = null;
        FreeBillMainVDTO dataDTO = null;
        boolean isPutFullPercentByVP = false;            //차감되지 않았거나 무제한인 공제항목을 담았을 경우 (음성)
        boolean isPutFullPercentBySms = false;          
        boolean isPutFullPercentByData = false;
        boolean isPutSubtractionByVP = false;          //차감중인 공제항목을 담았을 경우 (음성)
        boolean isPutSubtractionBySms = false;
        boolean isPutSubtractionByData = false;
        
        for(int i=0, len=freeBillEditList.size(); i< len; i++) {
            FreeBillMainVDTO data =  (FreeBillMainVDTO)freeBillEditList.get(i);
            String percent           = data.getPercent();
            String codeType         = data.getCodeType();
            
            if(!"0".equals(percent)){ //잔여량이 0이 아닌경우
                
                if("V".equals(codeType) || "VP".equals(codeType) || "P".equals(codeType)){
                    if(voiceAndpictureDTO == null) voiceAndpictureDTO = new FreeBillMainVDTO();
                    
                    if(isPutSubtractionByVP){
                        continue;
                    }else{
                        if("100".equals(percent)){
                            if(isPutFullPercentByVP){
                                continue;
                            }else{
                                voiceAndpictureDTO = data;
                                isPutFullPercentByVP = true;
                            }
                        }else{
                            voiceAndpictureDTO = data;
                            isPutSubtractionByVP = true;
                        }
                    }
                    
                }else if("D".equals(codeType)){
                    if(dataDTO == null) dataDTO = new FreeBillMainVDTO();
                    
                    if(isPutSubtractionByData){
                        continue;
                    }else{
                        if("100".equals(percent)){
                            if(isPutFullPercentByData){
                                continue;
                            }else{
                                dataDTO = data;
                                isPutFullPercentByData = true;
                            }
                        }else{
                            dataDTO = data;
                            isPutSubtractionByData = true;
                        }
                    }
                    
                }else if("S".equals(codeType)){
                    if(smsDTO == null) smsDTO = new FreeBillMainVDTO();
                    
                    if(isPutSubtractionBySms){
                        continue;
                    }else{
                        if("100".equals(percent)){
                            if(isPutFullPercentBySms){
                                continue;
                            }else{
                                smsDTO = data;
                                isPutFullPercentBySms = true;
                            }
                            
                        }else{
                            smsDTO = data;
                            isPutSubtractionBySms = true;
                        }
                    }
                }
            }
        }
        
        returnMap.put("FREE_PLAN", freeBillEditList);
        returnMap.put("voiceAndpictureDTO", voiceAndpictureDTO);
        returnMap.put("smsDTO", smsDTO);
        returnMap.put("dataDTO", dataDTO);
        returnMap.put("failCode", failCode);
        return returnMap;
    }
    
    /**
     * 잔여기본통화 데이타를 구성한다.
     * @param freeBillDataList
     * @return
     * @throws Exception
     */
    public Map editPlanFree(List freeBillDataList) throws Exception
    {
        Map editData = new HashMap();
        List freeBillEditList              = new ArrayList();
        List l_detail = null;
        HashMap DPMap = new HashMap();
        String dataOverUseQty = ""; //LTE 요금제 사용 고객의 경우 데이터 초과사용량
        String prodNm = "";
        String failCode = "";  //잔여기본통화 조회불가 케이스일 경우 코드
        int voiceAndpictureCnt = 0; //음성영상 공제항목수
        int dataCnt = 0; //데이터 공제항목수
        int smsCnt = 0; //문자 공제항목수
        int etcCnt = 0; //기타 공제항목수
        int prePriority = 99;
        int totalCnt = freeBillDataList.size();
        
        StringBuffer logSb = new StringBuffer();

//        //한도요금제 데이터 충전금액 테스트
//        List temp = new ArrayList();
//        temp.add("NA00003458");
//        temp.add("올인원34 한도관리");
//        temp.add("DP");
//        temp.add("올인원34 공제명");
//        temp.add("500");
//        temp.add("100");
//        temp.add("400");
//        temp.add("110");
//        temp.add("0");
//        temp.add("0");
//        temp.add("N");
//        temp.add("4");
//        temp.add("31000");
//        
//        freeBillDataList.add(temp);
        
        for (int i = 0; i < totalCnt; i++)
        {
            l_detail = (List) freeBillDataList.get(i);            
            FreeBillMainVDTO freeBillVDTO = new FreeBillMainVDTO();
            
            //단위가 분일때 '시','분','초'를 담는다.
            HashMap hourMinSec0 = new HashMap();
            HashMap hourMinSec1 = new HashMap();
            HashMap hourMinSec2 = new HashMap();
            
            String planId           = (String) l_detail.get(0); //상품코드
            String planName      = (String) l_detail.get(1); //상품명
            String skipCode       = (String) l_detail.get(2); //공제코드
            String freePlanName = (String) l_detail.get(3); //무료혜택명
            String totalQty           = (String) l_detail.get(4); //제공량
            String useQty            = (String) l_detail.get(5); //사용량
            String remainQty       = (String) l_detail.get(6); //잔여량
            String unitCode       = (String) l_detail.get(7); //단위
            String couponRegiDate = (String) l_detail.get(8); //등록일 (->쿠폰등록일 등)
            String unlimitType    = (String) l_detail.get(9); //무제한종류            
            String codeType =  (String) l_detail.get(10); //서비스구분
            String originCodeType =   (String)l_detail.get(11); //서비스구분 (음성/영상/데이터/등등..)
            String dcDedtPrty = (String)l_detail.get(12); //차감우선순위
            
            for ( int j=0; j<l_detail.size(); j++ ) {
                logSb.append("\n## v_detail("+j+")="+((String)l_detail.get(j)));
            }
            
            /*
             * [잔여기본통화 안보여주는 경우]
                XXXXXX : 당월에 기존 요금제로 재변경되어 서비스 조회 불가
                RRRRRR  : 이월에 기존 요금제로 재변경되어 서비스 조회 불가
                CURRENT_MONTH: 당월정지이력 있는 경우
                LAST_MONTH: 전월정지이력 있는 경우
                
                [잔여기본통화 보여주는 경우]
                YYYYYY : 당월에 기존 요금제로 재변경되어 조회된 무료통화잔액은 변동될 수 있습니다
             */
            if ("LAST_MONTH".equals(planId) 
                || "CURRENT_MONTH".equals(planId)
                || "RRRRRR".equals(skipCode)
                || "XXXXXX".equals(skipCode) ){
                
                if ("LAST_MONTH".equals(planId) || "CURRENT_MONTH".equals(planId)){
                    failCode = planId;
                }else if ("RRRRRR".equals(skipCode) || "XXXXXX".equals(skipCode)){
                    failCode = skipCode;
                }
                continue;
                
            } else if("DP".equals(skipCode)){
                DPMap.put("DP_PLAN_ID", planId);
                DPMap.put("DP_PLAN_NM", planName);
                DPMap.put("DP_SKIP_CD", skipCode);
                DPMap.put("DP_FREE_PLAN_NM", freePlanName);
                DPMap.put("DP_TOT_QTY", totalQty);
                DPMap.put("DP_USE_QTY", useQty);
                DPMap.put("DP_REMAIN_QTY", remainQty);
                DPMap.put("DP_UNIT_CD", unitCode);
                
            } else {            
                
                //공제항목을 보여줄지 여부 확인 (캐시에서 확인)
//                boolean isAllowProduct = FreeBillProductMapper.getInstance().isAllowProduct(planId);
//                boolean isAllowSkipCode = FreeBillSkipCodeMapper.getInstance().isAllowSkipCode(skipCode);
//                String ctgCd = (String)FreeBillProductMapper.getInstance().getCtgCd(planId);
//                String svcProdCd = (String)FreeBillProductMapper.getInstance().getSvcProdCd(planId);
                boolean isAllowProduct = true;
                boolean isAllowSkipCode = true;
                String ctgCd = "A";
                String svcProdCd = "NA00004120";
                
                if ( isAllowProduct && isAllowSkipCode )
                {
                    //요금제명 세팅하기
                    //우선순위 : 기본요금제 > 옵션요금 > 할인프로그램 > 부가서비스
                    int priority = 99;
                    if(ctgCd != null){
                        if(StringUtil.contains(ctgCd, "A") || StringUtil.contains(ctgCd, "D1")){ //기본요금제
                            priority = 1;
                        }else if(StringUtil.contains(ctgCd, "B") || StringUtil.contains(ctgCd, "D2")){ //옵션요금제
                            priority = 2;
                        }else if(StringUtil.contains(ctgCd, "C")){ //할인프로그램
                            priority = 3;
                        }else if(StringUtil.contains(ctgCd, "E") || StringUtil.contains(ctgCd, "D3")){ //부가서비스
                            priority = 4;
                        }
                    }
                    
                    if(prePriority > priority){
                        prodNm = StringUtil.replaceToCodeFromNoot(planName);
                        prePriority = priority;
                    }               
                                        
                    // 다다익선 요금제만 이월 표시
                    if (("NA00001996".equals(planId) || "NA00001686".equals(planId) || "NA00001687".equals(planId) || "NA00001997".equals(planId))  
                        && ("RD".equals(skipCode.substring(0, 2)))){
                        freeBillVDTO.setSkipName((StringUtil.replaceToCodeFromNoot(freePlanName)) + "(이월)");
                    }else{
                        freeBillVDTO.setSkipName(StringUtil.replaceToCodeFromNoot(freePlanName)); // 무료혜택명
                    }
                    
                    String cdataPercent = "0";
                    String cdata0 = "";
                    String cdata1 = "";
                    String cdata2 = "";
                    long iData0 = NumberUtil.parseLong(totalQty, 0);
                    long iData1 = NumberUtil.parseLong(useQty, 0);
                    long iData2 = NumberUtil.parseLong(remainQty, 0);
                    
                    if ("1".equals(unlimitType))
                    {
                        cdata0 = "무제한";
                        cdata1 = "무제한";
                        cdata2 = "무제한";
                        cdataPercent = "100";
                        
                        if("2".equals(originCodeType)){
                        	cdata0 = "기본제공";
                            cdata1 = "기본제공";
                            cdata2 = "기본제공";
                        }
                    }
                    else if ("B".equals(unlimitType))
                    {
                        cdata0 = "기본제공";
                        cdata1 = "기본제공";
                        cdata2 = "기본제공";
                        
                        cdataPercent = "100";
                    }
                    else if ("M".equals(unlimitType)) // 올인원 요금제 특수 케이스
                    {
                        cdata0 = "무제한";
                        if ("240".equals(unitCode)){
                            cdata1 = getMinVal(useQty);
                            hourMinSec1 = getHourMinSec(NumberUtil.parseInt(StringUtil.trim(useQty), 0));
                        }else{
                            cdata1 = getNumberFormat(useQty);
                        }
                        cdata2 = "무제한";
                        cdataPercent = "100";
                        
                        if("2".equals(originCodeType)){
                        	cdata0 = "기본제공";
                            cdata2 = "기본제공";
                        }
                    }
                    else
                    {
                        if ("240".equals(unitCode))
                        {
                            cdata0 = getMinVal(totalQty);
                            cdata1 = getMinVal(useQty);
                            cdata2 = getMinVal(remainQty);
                            
                            hourMinSec0 = getHourMinSec(NumberUtil.parseInt(StringUtil.trim(totalQty), 0));
                            hourMinSec1 = getHourMinSec(NumberUtil.parseInt(StringUtil.trim(useQty), 0));
                            hourMinSec2 = getHourMinSec(NumberUtil.parseInt(StringUtil.trim(remainQty), 0));
                        }else{
                            cdata0 = getNumberFormat(totalQty);
                            cdata1 = getNumberFormat(useQty);
                            cdata2 = getNumberFormat(remainQty);                            
                        }
                        
                        //잔여량퍼센트 = (잔여량 * 100 / (사용량+잔여량))
                        if(iData2 != 0){
                            long longPercent = iData2 * 100 / (iData1+iData2);
                            if(longPercent <= 1){
                                cdataPercent = "1";
                            }else{
                                cdataPercent = String.valueOf(longPercent);
                            }
                        }
                    }                    
                    
                    //공제항목 종류 구하기 (팅요금제의 유료한도, 충전금액, 선물금액의 공제항목종류는 TING)
                    if ( "HA".equals(skipCode) || "CH".equals(skipCode) ||"PR".equals(skipCode) ) {                    
                        codeType = "TING";
                    }else{
                        codeType = getCodeType(originCodeType);
                    }

                    freeBillVDTO.setProdId(planId);
                    freeBillVDTO.setProdName(StringUtil.replaceToCodeFromNoot(planName));     
                    freeBillVDTO.setBaseQty(cdata0);
                    freeBillVDTO.setUseQty(cdata1);
                    freeBillVDTO.setRemaindQty(cdata2);
                    freeBillVDTO.setCouponDate(couponRegiDate);
                    freeBillVDTO.setIsUnlimit(unlimitType);
                    freeBillVDTO.setPercent(cdataPercent);
                    freeBillVDTO.setSkipType(svcProdCd);
                    freeBillVDTO.setUnit(unitCode);
                    freeBillVDTO.setEditTotalQty(hourMinSec0);
                    freeBillVDTO.setEditUseQty(hourMinSec1);
                    freeBillVDTO.setEditRemaindQty(hourMinSec2);
                    freeBillVDTO.setSkipCode(skipCode);
                    freeBillVDTO.setCodeType(codeType);
                    freeBillVDTO.setDcDedtPrty(dcDedtPrty);
                    freeBillVDTO.setCtgCd(ctgCd);
                    
                    //공제항목별 갯수
                    if("V".equals(codeType) || "VP".equals(codeType) || "P".equals(codeType)){
                        voiceAndpictureCnt++;
                    }else if("D".equals(codeType)){
                        dataCnt++;
                    }else if("S".equals(codeType)){
                        smsCnt++;
                    }else{
                        etcCnt++;
                    }
                    
                    freeBillEditList.add(freeBillVDTO);
                    Collections.sort(freeBillEditList, this);
                    
                }else{
                    if("LT".equals(skipCode) && !"0".equals(useQty)){
                        dataOverUseQty = useQty;
                    }
                }
            }
        }
        
        editData.put("FREE_PLAN", freeBillEditList);
        editData.put("voiceAndpictureCnt", String.valueOf(voiceAndpictureCnt));
        editData.put("dataCnt", String.valueOf(dataCnt));
        editData.put("smsCnt", String.valueOf(smsCnt));
        editData.put("etcCnt", String.valueOf(etcCnt));        
        editData.put("DP", DPMap);
        editData.put("dataOverUseQty", dataOverUseQty);
        editData.put("prodNm", prodNm);
        editData.put("failCode", failCode);
        
        return editData;
    } 
    
    /**
     * 
     * <pre>
     * 소수점 편집
     * </pre>
     * 
     * @param t1
     * @return String
     */
    private String getMinVal(String t1)
    {
        int iDosu = NumberUtil.parseInt(StringUtil.trim(t1), 0);
        return getTimeFormat(iDosu);
    }
    
    /**
     * 주어진 값에서 '시간','분','초'를 구한다.
     * @param iSec 원본값(초단위)
     * @return
     */
    private HashMap getHourMinSec(int iSec)
    {
        HashMap hm = new HashMap();
        
        if (iSec == 0){
            hm.put("hour","0");
            hm.put("minute","0");
            hm.put("second","0");
        }else{
            DecimalFormat df = new DecimalFormat("00");
            GregorianCalendar cal = new GregorianCalendar(1970, 0, 1, 0, 0, 0);
            cal.set(Calendar.SECOND, Math.abs(iSec));

            String hour    = df.format(((cal.get(Calendar.DATE) - 1) * 24) + (cal.get(Calendar.HOUR_OF_DAY)));
            String minute = df.format(cal.get(Calendar.MINUTE));
            String second = df.format(cal.get(Calendar.SECOND));
            
            hm.put("hour",hour);
            hm.put("minute",minute);
            hm.put("second",second);
        }
        
        return hm;
    }    
    
    /**
     * CDRS 도수 사용량 계산
     */
    private String getTimeFormat(int iSec)
    {
        if (iSec == 0)
            return "00시간00분00초"; // return "-"; 2009.10.27 skkwon

        boolean blnMinus = false;
        if (iSec < 0)
            blnMinus = true;

        DecimalFormat df = new DecimalFormat("00");
        GregorianCalendar cal = new GregorianCalendar(1970, 0, 1, 0, 0, 0);
        cal.set(Calendar.SECOND, Math.abs(iSec));

        StringBuffer strb = new StringBuffer();
        String sHour = df.format(((cal.get(Calendar.DATE) - 1) * 24) + (cal.get(Calendar.HOUR_OF_DAY)));
        strb.append(sHour);
        strb.append("시간");
        strb.append(df.format(cal.get(Calendar.MINUTE)));
        strb.append("분");
        strb.append(df.format(cal.get(Calendar.SECOND)));
        strb.append("초");

        if (blnMinus == true)
            return "-" + strb.toString();
        else
            return strb.toString();
    }
    
    /**
     * CDRS 도수 사용량 계산 : 음성량 분추가 //yskim 2011.04.05. 
     */
    private String getTimeMMFormat(int iSec)
    {
        if (iSec == 0)
            return iSec+"분";
        
        boolean blnMinus = false;
        if (iSec < 0)
            blnMinus = true;
        
        DecimalFormat df = new DecimalFormat("00");
        GregorianCalendar cal = new GregorianCalendar(1970, 0, 1, 0, 0, 0);
        cal.set(Calendar.SECOND, Math.abs(iSec));
        
        StringBuffer strb = new StringBuffer();
        
        int iHour = (((cal.get(Calendar.DATE) - 1) * 24) + (cal.get(Calendar.HOUR_OF_DAY))) * 60;
        int iMin = cal.get(Calendar.MINUTE) ;
        int iRemainSec = cal.get(Calendar.SECOND)* 5 / 3 ;

        if (blnMinus == true)
            strb.append("(-" );
        else
            strb.append("(" );
        
        strb.append(iHour+ iMin);
        strb.append(".");
        strb.append(df.format(iRemainSec));
        strb.append("분)");
        
        return  strb.toString();
    } 
    
    /**
     * 숫자 패턴
     * @param num : 사용량
     * @param ret_str : 잔여량
     * @return
     */
    private String getNumberFormat(String num)
    {
        String ret_str = "";
        int tmpInt = 0;
        String tmpStr = "";
        
        NumberFormat numFormat = NumberFormat.getNumberInstance();
        
        if ( num.indexOf(".")>-1 ) {
            tmpStr = num.substring(num.indexOf("."), num.length());
            tmpInt = Integer.parseInt(num.substring(0, num.indexOf(".")));
        } else
            tmpInt = Integer.parseInt(num);
        ret_str = numFormat.format(tmpInt);
               
        ret_str += tmpStr;
        
        return ret_str;
    }    
    
    /**
     *  공제항목 정렬하기
     *  1단계 : 음성/영상 > 데이터 > 문자 > 기타
     *  2단계 : 같은 codeType일 경우 차감우선순위
     */
     public int compare(Object obj1, Object obj2)
     {
         String codeType1 = ((FreeBillMainVDTO)obj1).getCodeType();
         String dcDedtPrty1 = ((FreeBillMainVDTO)obj1).getDcDedtPrty();
         String isUnlimit1 = ((FreeBillMainVDTO)obj1).getIsUnlimit();
         String priority1 = getPriority(codeType1);
         if("1".equals(isUnlimit1) || "M".equals(isUnlimit1)){ //무제한 항목은 마지막에 노출
             if("V".equals(codeType1) || "VP".equals(codeType1) || "P".equals(codeType1)){
                 priority1 = "3";
             }
             dcDedtPrty1 = "99999";
         }
         
         String codeType2 = ((FreeBillMainVDTO)obj2).getCodeType();
         String dcDedtPrty2 = ((FreeBillMainVDTO)obj2).getDcDedtPrty();
         String isUnlimit2 = ((FreeBillMainVDTO)obj2).getIsUnlimit();
         String priority2 = getPriority(codeType2);
         if("1".equals(isUnlimit2) || "M".equals(isUnlimit2)){ //무제한 항목은 마지막에 노출
             if("V".equals(codeType2) || "VP".equals(codeType2) || "P".equals(codeType2)){
                 priority2 = "3";
             }
             dcDedtPrty2 = "99999";
         }
         
         int compareValue = priority1.compareTo(priority2);
         if(compareValue == 0){
             compareValue = dcDedtPrty1.compareTo(dcDedtPrty2);
         }
         
         return compareValue;
     }
     
     /**
      *  공제항목 우선순위 정하기
      *  정렬순서 : 음성 > 영상 > 문자 > 데이터 > 기타 
      */
     private String getPriority(String codeType)
     {
         String priority = null;
         
         if("V".equals(codeType)){ //음성
             priority = "1";
         }else if("VP".equals(codeType)){ //음성|영상
             priority = "2";
         }else if("P".equals(codeType)){ //영상
             priority = "3";
         }else if("S".equals(codeType)){ //문자
             priority = "4";             
         }else if("D".equals(codeType)){ //데이터
             priority = "5";
         }else if("VSP".equals(codeType)){
             priority = "6";
         }else if("VSD".equals(codeType)){
             priority = "7";
         }else if("ETC".equals(codeType)){
             priority = "8";
         }else if("TING".equals(codeType)){
             priority = "9";
         }else{ //기타
             priority = "10";
         }
         return priority;
     }
     
     /**
      * 공제항목 종류 구하기
      * @param originCodeType
      * @return
      */
     private String getCodeType(String originCodeType)
     {
         String codeType = null;
         
         if("4".equals(originCodeType)){ //음성
             codeType = "V";
         }else if("5".equals(originCodeType)){ //음성|영상
             codeType = "VP";
         }else if("1".equals(originCodeType)){ //영상
             codeType = "P";
         }else if("8".equals(originCodeType)){ //데이터
             codeType = "D";
         }else if("2".equals(originCodeType)){ //문자
             codeType = "S";
         }else if("7".equals(originCodeType)){
             codeType = "VSP";
         }else if("14".equals(originCodeType)){
             codeType = "VSD";
         }else{ //기타
             codeType = "ETC";
         }
         return codeType;
     }    
}
