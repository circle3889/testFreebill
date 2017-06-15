package kr.co.tworld.freebill.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

@Repository
public class CDRSRepository {
    /**
     * 잔여기본통화 CDRS 데이터를 가져온다.
     * @param svcMgmtNum
     * @param channel (1: 모티 2: 온티)
     * @return
     */
    public List getCDRSData(String svcMgmtNum, String channel) throws Exception{
        
        StringBuffer buffer = new StringBuffer();
        ArrayList list = new ArrayList();
        String[] keys = new String[]{"PROD_ID","PROD_NM","SKIP_ID","SKIP_NM","TOTAL","USE","REMAIN","UNIT","REG_DT","UNLIMIT","NO_USE","TYPE","ORDER"};
        
        try{
          //0.5초 지연
          this.sleep(500); 
          
          String rcvMsg = "{\"message\":\"무료통화 내역 조회\",\"result\":[{\"UNLIMIT\":\"1\",\"SKIP_ID\":\"DD188\",\"TYPE\":\"0\",\"ORDER\":\"11200\",\"TOTAL\":\"0\",\"PROD_ID\":\"NA00001724\",\"PROD_NM\":\"기본형 프리존(T-Map)\",\"USE\":\"0\",\"REG_DT\":\"\",\"REMAIN\":\"0\",\"SKIP_NM\":\"교통정보 무제한               \",\"NO_USE\":\"\",\"UNIT\":\"310\"},{\"UNLIMIT\":\"1\",\"SKIP_ID\":\"DD189\",\"TYPE\":\"0\",\"ORDER\":\"11200\",\"TOTAL\":\"0\",\"PROD_ID\":\"NA00001724\",\"PROD_NM\":\"기본형 프리존(T-Map)\",\"USE\":\"0\",\"REG_DT\":\"\",\"REMAIN\":\"0\",\"SKIP_NM\":\"네이트드라이브 기타정보료 무  \",\"NO_USE\":\"\",\"UNIT\":\"310\"},{\"UNLIMIT\":\"1\",\"SKIP_ID\":\"DD429\",\"TYPE\":\"0\",\"ORDER\":\"11200\",\"TOTAL\":\"0\",\"PROD_ID\":\"NA00001724\",\"PROD_NM\":\"기본형 프리존(T-Map)\",\"USE\":\"0\",\"REG_DT\":\"\",\"REMAIN\":\"0\",\"SKIP_NM\":\"음성길안내 무제한             \",\"NO_USE\":\"\",\"UNIT\":\"310\"},{\"UNLIMIT\":\"1\",\"SKIP_ID\":\"DD430\",\"TYPE\":\"0\",\"ORDER\":\"11200\",\"TOTAL\":\"0\",\"PROD_ID\":\"NA00001724\",\"PROD_NM\":\"기본형 프리존(T-Map)\",\"USE\":\"0\",\"REG_DT\":\"\",\"REMAIN\":\"0\",\"SKIP_NM\":\"문자길안내 무제한             \",\"NO_USE\":\"\",\"UNIT\":\"310\"},{\"UNLIMIT\":\"1\",\"SKIP_ID\":\"DD431\",\"TYPE\":\"8\",\"ORDER\":\"11600\",\"TOTAL\":\"0\",\"PROD_ID\":\"NA00001724\",\"PROD_NM\":\"기본형 프리존(T-Map)\",\"USE\":\"0\",\"REG_DT\":\"\",\"REMAIN\":\"0\",\"SKIP_NM\":\"T MAP 데이터통화 무료         \",\"NO_USE\":\"\",\"UNIT\":\"140\"},{\"UNLIMIT\":\"1\",\"SKIP_ID\":\"DD432\",\"TYPE\":\"0\",\"ORDER\":\"11200\",\"TOTAL\":\"0\",\"PROD_ID\":\"NA00001724\",\"PROD_NM\":\"기본형 프리존(T-Map)\",\"USE\":\"0\",\"REG_DT\":\"\",\"REMAIN\":\"0\",\"SKIP_NM\":\"서킷데이터 공제               \",\"NO_USE\":\"\",\"UNIT\":\"240\"},{\"UNLIMIT\":\"1\",\"SKIP_ID\":\"DD249\",\"TYPE\":\"8\",\"ORDER\":\"11600\",\"TOTAL\":\"0\",\"PROD_ID\":\"NA00001906\",\"PROD_NM\":\"모바일 그룹웨어(Premium)\",\"USE\":\"0\",\"REG_DT\":\"\",\"REMAIN\":\"0\",\"SKIP_NM\":\"모바일그룹웨어 패킷무료       \",\"NO_USE\":\"\",\"UNIT\":\"140\"},{\"UNLIMIT\":\"1\",\"SKIP_ID\":\"DD287\",\"TYPE\":\"0\",\"ORDER\":\"11200\",\"TOTAL\":\"0\",\"PROD_ID\":\"NA00001906\",\"PROD_NM\":\"모바일 그룹웨어(Premium)\",\"USE\":\"0\",\"REG_DT\":\"\",\"REMAIN\":\"0\",\"SKIP_NM\":\"모바일그룹웨어 서킷무료       \",\"NO_USE\":\"\",\"UNIT\":\"240\"},{\"UNLIMIT\":\"B\",\"SKIP_ID\":\"DD0Z5\",\"TYPE\":\"2\",\"ORDER\":\"11000\",\"TOTAL\":\"0\",\"PROD_ID\":\"NA00004775\",\"PROD_NM\":\"band 데이터 퍼펙트\",\"USE\":\"0\",\"REG_DT\":\"\",\"REMAIN\":\"0\",\"SKIP_NM\":\"메신저 기본제공\",\"NO_USE\":\"\",\"UNIT\":\"310\"},{\"UNLIMIT\":\"0\",\"SKIP_ID\":\"DD0Z7\",\"TYPE\":\"8\",\"ORDER\":\"30980\",\"TOTAL\":\"11534336\",\"PROD_ID\":\"NA00004775\",\"PROD_NM\":\"band 데이터 퍼펙트\",\"USE\":\"500000\",\"REG_DT\":\"\",\"REMAIN\":\"653436\",\"SKIP_NM\":\"데이터통화 11GB 무료          \",\"NO_USE\":\"\",\"UNIT\":\"140\"},{\"UNLIMIT\":\"1\",\"SKIP_ID\":\"DD0Z8\",\"TYPE\":\"8\",\"ORDER\":\"31000\",\"TOTAL\":\"0\",\"PROD_ID\":\"NA00004775\",\"PROD_NM\":\"band 데이터 퍼펙트\",\"USE\":\"0\",\"REG_DT\":\"\",\"REMAIN\":\"0\",\"SKIP_NM\":\"데이터무제한-일2GB 후 속도제어\",\"NO_USE\":\"\",\"UNIT\":\"140\"},{\"UNLIMIT\":\"M\",\"SKIP_ID\":\"DD0ZB\",\"TYPE\":\"4\",\"ORDER\":\"19500\",\"TOTAL\":\"9999999\",\"PROD_ID\":\"NA00004775\",\"PROD_NM\":\"band 데이터 퍼펙트\",\"USE\":\"0\",\"REG_DT\":\"\",\"REMAIN\":\"9999999\",\"SKIP_NM\":\"SKT 고객간 음성 무제한        \",\"NO_USE\":\"\",\"UNIT\":\"240\"},{\"UNLIMIT\":\"M\",\"SKIP_ID\":\"DD0ZC\",\"TYPE\":\"4\",\"ORDER\":\"19500\",\"TOTAL\":\"9999999\",\"PROD_ID\":\"NA00004775\",\"PROD_NM\":\"band 데이터 퍼펙트\",\"USE\":\"0\",\"REG_DT\":\"\",\"REMAIN\":\"9999999\",\"SKIP_NM\":\"집전화·이동전화 음성 무제한  \",\"NO_USE\":\"\",\"UNIT\":\"240\"},{\"UNLIMIT\":\"0\",\"SKIP_ID\":\"DD0ZD\",\"TYPE\":\"5\",\"ORDER\":\"31000\",\"TOTAL\":\"18000\",\"PROD_ID\":\"NA00004775\",\"PROD_NM\":\"band 데이터 퍼펙트\",\"USE\":\"0\",\"REG_DT\":\"\",\"REMAIN\":\"18000\",\"SKIP_NM\":\"영상, 부가전화 300분          \",\"NO_USE\":\"\",\"UNIT\":\"240\"},{\"UNLIMIT\":\"0\",\"SKIP_ID\":\"DDM57\",\"TYPE\":\"4\",\"ORDER\":\"30809\",\"TOTAL\":\"300000\",\"PROD_ID\":\"NH00000087\",\"PROD_NM\":\"온가족프리_혜택2_가족무료\",\"USE\":\"0\",\"REG_DT\":\"\",\"REMAIN\":\"300000\",\"SKIP_NM\":\"온가족 가족간 음성통화 무료   \",\"NO_USE\":\"\",\"UNIT\":\"240\"},{\"UNLIMIT\":\"B\",\"SKIP_ID\":\"DDM58\",\"TYPE\":\"2\",\"ORDER\":\"20000\",\"TOTAL\":\"0\",\"PROD_ID\":\"NH00000087\",\"PROD_NM\":\"온가족프리_혜택2_가족무료\",\"USE\":\"0\",\"REG_DT\":\"\",\"REMAIN\":\"0\",\"SKIP_NM\":\"온가족 가족간 SMS 무료        \",\"NO_USE\":\"\",\"UNIT\":\"310\"},{\"UNLIMIT\":\"0\",\"SKIP_ID\":\"DDR38\",\"TYPE\":\"8\",\"ORDER\":\"12050\",\"TOTAL\":\"512000\",\"PROD_ID\":\"NH00000100\",\"PROD_NM\":\"온가족프리_혜택1_500MB\",\"USE\":\"0\",\"REG_DT\":\"\",\"REMAIN\":\"512000\",\"SKIP_NM\":\"온가족프리 데이터500MB        \",\"NO_USE\":\"\",\"UNIT\":\"140\"}],\"status\":\"1\",\"count\":\"17\"}";
          HashMap map = this.convertJSONToMap(rcvMsg);
          
          String message = (String) map.get("message");
          String status = (String) map.get("status"); //-1:에러, 1:조회성공
          String count = (String) map.get("count");
          
          if("1".equals(status)){
              list = changeArrayTolist((String) map.get("result"), keys);
          } 
          
        }catch(Exception ex){
            System.out.println("Exception = " + ex.toString());
        }
        
        return list;
    }
    
    /**
     * JSON --> MAP 으로 변환
     * @param jsonString
     * @return
     * @throws Exception
     */
    private HashMap<String, String> convertJSONToMap(String jsonString) throws Exception {
        
        JSONObject jsonObject = null;
        HashMap<String, String> hm = new HashMap<String, String>();
        
        jsonObject= new JSONObject(jsonString);
        Iterator<String> itr = jsonObject.keys();

        String key = "";
        String value = "";
        while(itr.hasNext()) {
            key = itr.next();
            value = jsonObject.get(key).toString();

            hm.put(key, value);                
        }
        
        return hm;
    }
    
    /**
     * Array -> list 형태로 변환한다.
     * @param data 원본데이터
     * @param keys 추출할 키값들
     * @return
     * @throws Exception
     */
    private ArrayList changeArrayTolist(String data, String[] keys) throws Exception{
        
        ArrayList list = new ArrayList();
        
        JSONArray jsonArry = new JSONArray(data);
        ArrayList subList = null;
        
        for(int i=0, len=jsonArry.length(); i<len; i++){
            subList = new ArrayList();
            JSONObject obj = jsonArry.getJSONObject(i);
            
            for(int j=0, size=keys.length; j<size; j++){
                subList.add(obj.get(keys[j]));
            }
            list.add(subList);
        }
        
        return list;
    }
    
    private void sleep(int time){
        try {
          Thread.sleep(time);
        } catch (InterruptedException e) { }
    }
    
}
