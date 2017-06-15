package kr.co.tworld.freebill.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import kr.co.tworld.freebill.controller.FreeBillController;
import kr.co.tworld.freebill.domain.FreeBillMainVDTO;
import kr.co.tworld.freebill.domain.FreeBillUtil;
import kr.co.tworld.freebill.domain.StringUtil;
import kr.co.tworld.freebill.repository.CDRSRepository;
import kr.co.tworld.freebill.repository.Product;
import kr.co.tworld.freebill.repository.ProductRepository;

@Service
public class FreeBillService {
	
	private static Logger logger = LoggerFactory.getLogger(FreeBillController.class);
	
	@Value("${uri.apigateway}")
	private String URI_APIGATEWAY;
	
	@Value("${uri.freebill}")
	private String URI_FREEBILL;
	
	@Autowired
	private CDRSRepository cDRSRepository;
	
    @Autowired 
    private ProductRepository productRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Resource(name="redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;
	
	@HystrixCommand(fallbackMethod = "hystrixTestFallback")
	public ResponseEntity<String> getHystrixTest(String token) {
		return restTemplate.getForEntity(URI_APIGATEWAY + URI_FREEBILL + "/freebill/main?tokenId={tokenId}", String.class, token);
	}
	
	private ResponseEntity<String> hystrixTestFallback(String svcMgmtNum, Throwable t) {
		System.out.println("Using fallback method for getHystrixTest" + t);
        return null;
    } 
	
	/**
	 * 잔여기본통화 조회하기 (무선 메인)
	 * @param svcMgmtNum
	 * @return
	 */
    public HashMap freeBillMain(String token)
    {
        HashMap resultMap = new HashMap();
        //조회결과정보
        HashMap resultInfo = new HashMap();
        
        List freeBillList = new ArrayList();
        List freeBillEditList  = new ArrayList();
        List cDRSData = null;
        
        String prodNm = "";
        String resultCode = "0";
        String failMsg = "";
        int width = 0;
        String pricPlanId = ""; // 요금제코드
        String freeBillList_percent1       = "";
        String freeBillList_remain1        = "";
        String freeBillList_freeBillCd1    = "";
        String freeBillList_use1           = "";
        String freeBillList_freeBillNm1    = "";
        String freeBillList_percent2       = "";
        String freeBillList_remain2        = "";
        String freeBillList_freeBillCd2    = "";
        String freeBillList_use2           = "";
        String freeBillList_freeBillNm2    = "";
        String freeBillList_percent3       = "";
        String freeBillList_remain3        = "";
        String freeBillList_freeBillCd3    = "";
        String freeBillList_use3           = "";
        String freeBillList_freeBillNm3    = "";
        
        try
        {
        	//서비스관리번호 가져오기
        	String svcMgmtNum = (String) redisTemplate.opsForHash().get(token,"selected");
        	
        	logger.debug("token == " + token);
        	logger.debug("svcMgmtNum == " + svcMgmtNum);
        	
			cDRSData = cDRSRepository.getCDRSData(svcMgmtNum, "1");
			
            //데이터 편집하기
            FreeBillUtil freeBillUtil = new FreeBillUtil();
			Map pricPlanMap = freeBillUtil.editPlanFreeForMain(cDRSData);

			freeBillEditList = (List) pricPlanMap.get("FREE_PLAN");
			FreeBillMainVDTO voiceAndpictureDTO = (FreeBillMainVDTO) pricPlanMap
					.get("voiceAndpictureDTO");
			FreeBillMainVDTO smsDTO = (FreeBillMainVDTO) pricPlanMap
					.get("smsDTO");
			FreeBillMainVDTO dataDTO = (FreeBillMainVDTO) pricPlanMap
					.get("dataDTO");

			int totalCount = freeBillEditList.size();
			if (totalCount <= 0) {
				resultCode = "1";
				failMsg = "잔여기본통화 조회를 할 수 없습니다. 자세한 사항은 상세화면을 확인해주세요.";
			}

			//상품명을 가져온다.
			List<Product> list = (List<Product>) productRepository.findByProdId("NA00004120");
			for(Product product : list){
				prodNm = product.getProdName();
			}

			resultInfo.put("resultCode", resultCode);
			resultInfo.put("resultMessage", failMsg);
			resultInfo.put("prodNm", prodNm);
			resultInfo.put("prodId", "NA00004120");
			
			// 조회결과내용
			if (voiceAndpictureDTO != null) {
				String freePlanName = voiceAndpictureDTO.getSkipName();
				String useQty = voiceAndpictureDTO.getUseQty();
				String remainQty = voiceAndpictureDTO.getRemaindQty();
				String percent = voiceAndpictureDTO.getPercent();

				if (!StringUtil.isEmpty(percent)) {
					width = 100 - Integer.parseInt(percent);
				}

				if ("무제한".equals(useQty)) {
					remainQty = "무제한";
				}

				HashMap resultData = new HashMap();
				resultData.put("freeBillCd", "V");
				resultData.put("freeBillNm", freePlanName);
				resultData.put("percent", width);
				resultData.put("use", useQty);
				resultData.put("remain", remainQty);
				freeBillList.add(resultData);
				
				freeBillList_percent1       = width+"";;
		        freeBillList_remain1        = remainQty;
		        freeBillList_freeBillCd1    = "V";
		        freeBillList_use1           = useQty;
		        freeBillList_freeBillNm1    = freePlanName;
			}

			if (smsDTO != null) {
				String freePlanName = smsDTO.getSkipName();
				String useQty = smsDTO.getUseQty();
				String remainQty = smsDTO.getRemaindQty();
				String percent = smsDTO.getPercent();
				String CodeType = smsDTO.getCodeType();

				if (!StringUtil.isEmpty(percent)) {
					width = 100 - Integer.parseInt(percent);
				}

				if ("무제한".equals(useQty)) {
					remainQty = "무제한";
					if ("S".equals(CodeType)) {
						useQty = "기본제공";
						remainQty = "기본제공";
					}
				}

				HashMap resultData = new HashMap();
				resultData.put("freeBillCd", "S");
				resultData.put("freeBillNm", freePlanName);
				resultData.put("percent", width);
				resultData.put("use", useQty);
				resultData.put("remain", remainQty);
				freeBillList.add(resultData);
				
				freeBillList_percent2       = width+"";;
		        freeBillList_remain2        = remainQty;
		        freeBillList_freeBillCd2    = "S";
		        freeBillList_use2           = useQty;
		        freeBillList_freeBillNm2    = freePlanName;
			}

			if (dataDTO != null) {
				String freePlanName = dataDTO.getSkipName();
				String useQty = dataDTO.getUseQty();
				String remainQty = dataDTO.getRemaindQty();
				String percent = dataDTO.getPercent();

				if (!StringUtil.isEmpty(percent)) {
					width = 100 - Integer.parseInt(percent);
				}

				if ("무제한".equals(useQty)) {
					remainQty = "무제한";
				}

				HashMap resultData = new HashMap();
				resultData.put("freeBillCd", "D");
				resultData.put("freeBillNm", freePlanName);
				resultData.put("percent", width);
				resultData.put("use", useQty);
				resultData.put("remain", remainQty);
				freeBillList.add(resultData);
				
				freeBillList_percent3       = width+"";;
		        freeBillList_remain3        = remainQty;
		        freeBillList_freeBillCd3    = "D";
		        freeBillList_use3           = useQty;
		        freeBillList_freeBillNm3    = freePlanName;
			}

			resultMap.put("resultInfo", resultInfo);
			resultMap.put("freeBillList", freeBillList);
			resultMap.put("svcMgmtNum", svcMgmtNum);
			resultMap.put("freeBillList_percent1",freeBillList_percent1     );
			resultMap.put("freeBillList_remain1",freeBillList_remain1      );
			resultMap.put("freeBillList_freeBillCd1",freeBillList_freeBillCd1  );
			resultMap.put("freeBillList_use1",freeBillList_use1         );
			resultMap.put("freeBillList_freeBillNm1",freeBillList_freeBillNm1  );
			resultMap.put("freeBillList_percent2",freeBillList_percent2     );
			resultMap.put("freeBillList_remain2",freeBillList_remain2      );
			resultMap.put("freeBillList_freeBillCd2",freeBillList_freeBillCd2  );
			resultMap.put("freeBillList_use2",freeBillList_use2         );
			resultMap.put("freeBillList_freeBillNm2",freeBillList_freeBillNm2  );
			resultMap.put("freeBillList_percent3",freeBillList_percent3     );
			resultMap.put("freeBillList_remain3",freeBillList_remain3      );
			resultMap.put("freeBillList_freeBillCd3",freeBillList_freeBillCd3  );
			resultMap.put("freeBillList_use3",freeBillList_use3         );
			resultMap.put("freeBillList_freeBillNm3",freeBillList_freeBillNm3  );
			
			resultMap.put("cdrsConnErrYn", "N");
        }catch(Exception ex){
            System.out.println("Exception = " + ex.toString());
        }

        return resultMap;
    }
    
    /**
     * 잔여기본통화 조회하기 (무선 잔여기본통화)
     * @param svcMgmtNum
     * @return
     */
	public HashMap getFreeBillDetail(String token)
	{
        List freeBillDataList = new ArrayList(); // 무료통화조회내역
        List freeBillEditList  = new ArrayList();
        List cDRSData = null;
        HashMap resultMap = new HashMap();
        Map pricPlanMap = null; // 무료통화대상 요금제.
        Map remainMap = null; // 사용가능 금액 편집
        Map DPMap = null;
        
        String dataOverUseQty = "";
        String prodNm = "";
        String voiceAndpictureCnt = null;
        String dataCnt = null;
        String smsCnt = null;
        String etcCnt = null;  
        String failCode = "";  
        String lteDataSharingUseChk = "N";// LTE데이터 함께쓰기 요금제 사용여부
        String lteDataSharingUseQty = ""; // LTE데이터 함께쓰기 총사용량
        int use_refillCoupon = 0; // 사용가능한 데이터 리필쿠폰 갯수

        try{
        	//서비스관리번호 가져오기
        	String svcMgmtNum = (String) redisTemplate.opsForHash().get(token,"selected");
        	
        	logger.debug("token == " + token);
        	logger.debug("svcMgmtNum == " + svcMgmtNum);
        	
            //데이터 가져오기
            cDRSData = cDRSRepository.getCDRSData(svcMgmtNum, "1");
            
            //데이터 편집하기
            FreeBillUtil freeBillUtil = new FreeBillUtil();
            pricPlanMap = freeBillUtil.editPlanFree(cDRSData);
            
            freeBillEditList   = (List) pricPlanMap.get("FREE_PLAN");
            DPMap              = (HashMap)pricPlanMap.get("DP");
            dataOverUseQty     = (String)pricPlanMap.get("dataOverUseQty");
            voiceAndpictureCnt = (String) pricPlanMap.get("voiceAndpictureCnt");
            dataCnt            = (String) pricPlanMap.get("dataCnt");
            smsCnt             = (String) pricPlanMap.get("smsCnt");
            etcCnt             = (String) pricPlanMap.get("etcCnt");     
            failCode           = (String) pricPlanMap.get("failCode");  
            
            prodNm = "LTE 전국민 무한 85";
            
            resultMap.put("freeBillEditList", freeBillEditList);
            resultMap.put("voiceAndpictureCnt", voiceAndpictureCnt);
            resultMap.put("dataCnt", dataCnt);
            resultMap.put("smsCnt", smsCnt);
            resultMap.put("etcCnt", etcCnt);          
            resultMap.put("dpMap", DPMap);
            resultMap.put("dataOverUseQty", dataOverUseQty);
            resultMap.put("ngm", "T");
            resultMap.put("prodNm", prodNm);
            resultMap.put("failCode", failCode);
            resultMap.put("svcMgmtNum", svcMgmtNum);
            
        }catch(Exception ex){
            System.out.println("Exception = " + ex.toString());
        }


        return resultMap;
	}
}
