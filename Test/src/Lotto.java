import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;



public class Lotto implements Cloneable{
	static String sort;
	static int divide;
	
	public static void main(String[] args) {
		int totalCnt;
		int totalRankCnt = 0;
		List<Integer> answer = new ArrayList<Integer>();
		Map<Integer, Integer> rankCntMap = new HashMap<>();
		
		if(args.length < 3){
			System.out.println("매개변수가 부족합니다.");
			return;
		}
		
		//1등 숫자 체크 validation
		if(!emptyChk(args[0])){
			System.out.println("매개변수가 비어있습니다.");
			return;
		}
		if(args[0].split(",").length != 6){
			System.out.println("1등의 숫자가 6개가 아닙니다.");
			return;
		}
		for(String chkVal : args[0].split(",")){
			if(!numChk(chkVal)){
				System.out.println("숫자가 아닙니다.");
				return;
			}
			answer.add(Integer.parseInt(chkVal));
		}
		if(!duplicateIndexValChk(answer)){
			System.out.println("1등의 숫자가 중복되는 값이 있습니다.");
			return;
		}

		//각등수마다 생성할 숫자의 수 validation
		if(!emptyChk(args[1])){
			System.out.println("매개변수가 비어있습니다.");
			return;
		}
		for(String temp : args[1].split(",")){
			String[] keyVal = temp.split(":");
			
			if(!numChk(keyVal[1])){
				System.out.println("숫자가 아닙니다.");
				return;
			}
			
			//추후 등수별 갯수체크는 여기 추가
			
			rankCntMap.put(Integer.parseInt(keyVal[0]), Integer.parseInt(keyVal[1]));
			totalRankCnt += Integer.parseInt(keyVal[1]);
			
		}
		if(rankCntMap.size() != args[1].split(",").length){
			System.out.println("중복된 순위가 있습니다.");
			return;
		}
		
		//전체 생성될 개수 validation
		if(!emptyChk(args[2])){
			System.out.println("매개변수가 비어있습니다.");
			return;
		}
		if(!numChk(args[2])){
			System.out.println("숫자가 아닙니다.");
			return;
		}
		totalCnt = Integer.parseInt(args[2]);
		if(totalCnt < totalRankCnt){
			System.out.println("전체 생성될 개수가 각등수마다 생성될 숫자의 합보다 작을 수 없습니다.");
			return;
		}
		
		rankCntMap.put(0, totalCnt - totalRankCnt);
		
		if(args.length > 3){
			sort = args[3];
		}else{
			sort = "FALSE";
		}
		divide = totalCnt/totalRankCnt;
		lottoMaker(answer,rankCntMap);
		

		//testtest
		
	}
	
	/**
	 * 
	 * @param answer 정답
	 * @param rankCntMap 랭킹별 생성될 갯수 
	 */
	static void lottoMaker(List<Integer> answer, Map<Integer,Integer> rankCntMap){
		
		Set<Integer> key = rankCntMap.keySet();
		List<ArrayList<Integer>> rankResult = new ArrayList<ArrayList<Integer>>();
		List<ArrayList<Integer>> noRankResult = new ArrayList<ArrayList<Integer>>();
		List<Integer> addList = new ArrayList<Integer>();
		int rankCnt;
		
		for(int mapkey : key){
			rankCnt = rankCntMap.get(mapkey);
			int i  = 0;
			while(i < rankCnt){
				int sameNumCnt = answer.size() - (mapkey-1);
				if(mapkey == 0){ sameNumCnt = mapkey; }
				
				addList = makeNumber(answer, sameNumCnt);
				if(duplicateListChk(rankResult,addList) && duplicateListChk(noRankResult,addList)){
					if(mapkey != 0){
						rankResult.add((ArrayList<Integer>) addList);
					}else{
						noRankResult.add((ArrayList<Integer>) addList);
					}
					i++;
				}
			}
		}
		printLotto(rankResult, noRankResult, answer);
	}
	
	/**
	 * 출력을 위한 메서드
	 * @param rankList	랭킹에 들어가는 숫자리스트
	 * @param noRankList	랭킹에 안들어가는 숫자 리스트
	 * @param answer	정답
	 */
	static void printLotto(List<ArrayList<Integer>> rankList, List<ArrayList<Integer>> noRankList, List<Integer> answer){
		if(divide == 0){
			divide = 2;
		}
		int totalSize = rankList.size() + noRankList.size();
		int rankIndex = 0;
		int noRankIndex = 0;
		if(sort.equals("TRUE")){
			for(int i = 0 ; i <= totalSize ; i++){
				if(rankList.size() > noRankList.size()){
					if(i % (divide+1) ==0 && noRankIndex < noRankList.size()){
						System.out.println(noRankList.get(noRankIndex));
						noRankIndex++;
					}else if(noRankIndex < noRankList.size()){
						System.out.print(rankList.get(rankIndex));
						System.out.println("->"+lottoRank(answer,rankList.get(rankIndex)));
						rankIndex++;
					}
				}else{
					if(i % (divide+1) ==0 && rankIndex < rankList.size()){
						System.out.print(rankList.get(rankIndex));
						System.out.println("->"+lottoRank(answer,rankList.get(rankIndex)));
						rankIndex++;
					}else if(noRankIndex < noRankList.size()){
						System.out.println(noRankList.get(noRankIndex));
						noRankIndex++;
					}
				}
			}
		}else{
			for(List<Integer> rank : rankList){
				System.out.print(rank);
				System.out.println("->"+lottoRank(answer,rank));
			}
			
			for(List<Integer> noRank : noRankList){
				System.out.println(noRank);
			}
		}
	}
	
	/**
	 * 랭킹 반환 메서드
	 * @param answer 정답 리스트
	 * @param lottoList 순위를 확인할 번호
	 * @return 등수 return
	 */
	static String lottoRank(List<Integer> answer, List<Integer> lottoList){
		int countSame = 0;
		String rankType = "";
		if(answer.containsAll(lottoList)){
			return "1등";
		}
		
		for(int num : answer){
			if(lottoList.contains(num)){
				countSame++;
			}
		}
		
		switch(countSame){
			case 5:
				rankType = "2등";
				break;
			case 4: 
				rankType = "3등";
				break;
			case 3: 
				rankType = "4등";
				break;
			case 2:
				rankType = "5등";
				break;
			case 1: 
				rankType = "6등";
				break;
			default : 
				break;
		}
		return rankType;
	}
	
	/**
	 * 로또 숫자 생성 메서드
	 * @param answer 정답
	 * @param sameNumCnt 정답과 같은 숫자가 몇개오는지
	 * @return sameNumCnt개의 정답수와 같은 리스트를 만들어 넘긴다
	 */
	static List<Integer> makeNumber(List<Integer> answer, int sameNumCnt){
		Random ran = new Random();
		List<Integer> result = new ArrayList<Integer>();
		
		//정답과 같아야하는 숫자 뽑기 sameNumCnt 만큼
		while(result.size() != sameNumCnt){
			int popIndex = ran.nextInt(sameNumCnt);
			
			if(!result.contains(answer.get(popIndex))){
				result.add(answer.get(popIndex));
			}
		}
		
		//정답과 다른 숫자 뽑기 6-sameNumCnt 만큼
		while(result.size() != 6){
			int addNum = ran.nextInt(45)+1;
			if(!answer.contains(addNum) && !result.contains(addNum)){
				result.add(addNum);
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param str 비어있는지 확인할 값
	 * @return boolean
	 */   
	static boolean emptyChk(String str){
		if(str == null || str.isEmpty()){
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param str 변환할 숫자
	 * @return boolean
	 */
	static boolean numChk(String str){
		try{
			Integer.parseInt(str);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	
	/**
	 * 중복 제거 이후 size가 같으면 중복이 없는것으로 판단
	 * @param 하나의 List 안에 중복된 값이 있는지를 확인할 List
	 * @return boolean
	 */
	static boolean duplicateIndexValChk(List<Integer> list){
		HashSet<Integer> chkList = new HashSet<Integer>(list); 
		if(chkList.size() != list.size()){
			return false;
		}
		return true;
	}
	
	/**
	 * 리스트 요소가 전부 같은게 있는지 확인
	 * @param list
	 * @param compareList
	 * @return boolean
	 */
	static boolean duplicateListChk(List<ArrayList<Integer>> list, List<Integer> compareList){
		for(List<Integer> a : list){
			if(a.containsAll(compareList)){
				return false;
			}
		}
		return true;
	}
}
