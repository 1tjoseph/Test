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
			System.out.println("�Ű������� �����մϴ�.");
			return;
		}
		
		//1�� ���� üũ validation
		if(!emptyChk(args[0])){
			System.out.println("�Ű������� ����ֽ��ϴ�.");
			return;
		}
		if(args[0].split(",").length != 6){
			System.out.println("1���� ���ڰ� 6���� �ƴմϴ�.");
			return;
		}
		for(String chkVal : args[0].split(",")){
			if(!numChk(chkVal)){
				System.out.println("���ڰ� �ƴմϴ�.");
				return;
			}
			answer.add(Integer.parseInt(chkVal));
		}
		if(!duplicateIndexValChk(answer)){
			System.out.println("1���� ���ڰ� �ߺ��Ǵ� ���� �ֽ��ϴ�.");
			return;
		}

		//��������� ������ ������ �� validation
		if(!emptyChk(args[1])){
			System.out.println("�Ű������� ����ֽ��ϴ�.");
			return;
		}
		for(String temp : args[1].split(",")){
			String[] keyVal = temp.split(":");
			
			if(!numChk(keyVal[1])){
				System.out.println("���ڰ� �ƴմϴ�.");
				return;
			}
			
			//���� ����� ����üũ�� ���� �߰�
			
			rankCntMap.put(Integer.parseInt(keyVal[0]), Integer.parseInt(keyVal[1]));
			totalRankCnt += Integer.parseInt(keyVal[1]);
			
		}
		if(rankCntMap.size() != args[1].split(",").length){
			System.out.println("�ߺ��� ������ �ֽ��ϴ�.");
			return;
		}
		
		//��ü ������ ���� validation
		if(!emptyChk(args[2])){
			System.out.println("�Ű������� ����ֽ��ϴ�.");
			return;
		}
		if(!numChk(args[2])){
			System.out.println("���ڰ� �ƴմϴ�.");
			return;
		}
		totalCnt = Integer.parseInt(args[2]);
		if(totalCnt < totalRankCnt){
			System.out.println("��ü ������ ������ ��������� ������ ������ �պ��� ���� �� �����ϴ�.");
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
		
		
		
		
	}
	
	/**
	 * 
	 * @param answer ����
	 * @param rankCntMap ��ŷ�� ������ ���� 
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
	 * ����� ���� �޼���
	 * @param rankList	��ŷ�� ���� ���ڸ���Ʈ
	 * @param noRankList	��ŷ�� �ȵ��� ���� ����Ʈ
	 * @param answer	����
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
	 * ��ŷ ��ȯ �޼���
	 * @param answer ���� ����Ʈ
	 * @param lottoList ������ Ȯ���� ��ȣ
	 * @return ��� return
	 */
	static String lottoRank(List<Integer> answer, List<Integer> lottoList){
		int countSame = 0;
		String rankType = "";
		if(answer.containsAll(lottoList)){
			return "1��";
		}
		
		for(int num : answer){
			if(lottoList.contains(num)){
				countSame++;
			}
		}
		
		switch(countSame){
			case 5:
				rankType = "2��";
				break;
			case 4: 
				rankType = "3��";
				break;
			case 3: 
				rankType = "4��";
				break;
			case 2:
				rankType = "5��";
				break;
			case 1: 
				rankType = "6��";
				break;
			default : 
				break;
		}
		return rankType;
	}
	
	/**
	 * �ζ� ���� ���� �޼���
	 * @param answer ����
	 * @param sameNumCnt ����� ���� ���ڰ� �������
	 * @return sameNumCnt���� ������� ���� ����Ʈ�� ����� �ѱ��
	 */
	static List<Integer> makeNumber(List<Integer> answer, int sameNumCnt){
		Random ran = new Random();
		List<Integer> result = new ArrayList<Integer>();
		
		//����� ���ƾ��ϴ� ���� �̱� sameNumCnt ��ŭ
		while(result.size() != sameNumCnt){
			int popIndex = ran.nextInt(sameNumCnt);
			
			if(!result.contains(answer.get(popIndex))){
				result.add(answer.get(popIndex));
			}
		}
		
		//����� �ٸ� ���� �̱� 6-sameNumCnt ��ŭ
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
	 * @param str ����ִ��� Ȯ���� ��
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
	 * @param str ��ȯ�� ����
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
	 * �ߺ� ���� ���� size�� ������ �ߺ��� ���°����� �Ǵ�
	 * @param �ϳ��� List �ȿ� �ߺ��� ���� �ִ����� Ȯ���� List
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
	 * ����Ʈ ��Ұ� ���� ������ �ִ��� Ȯ��
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