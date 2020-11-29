package huffman_decode;

import java.util.*;
import java.io.*;

//우선순위 큐에 들어갈 노드 클래스 선언
class Node{
	public char character;//문자 하나를 의미
	public int frequency;//출현 빈도수
	public Node left, right; //왼쪽노드와 오른쪽 노드
}

class FrequencyComparator implements Comparator<Node>{
	//빈도수가 낮은 것이 우선순위가 높아 먼저 빠져나올 수 있도록 함
	public int compare(Node a, Node b) {
		int frequencyA=a.frequency;
		int frequencyB=b.frequency;
		return frequencyA-frequencyB;
	}
}
//메인 클레스 정의
public class Decode{
	
	public static PriorityQueue<Node> queue; //우선순위 큐 선언
	public static HashMap <Character, String> charToCode=new HashMap <Character,String>();
	//문자에 따른 코드 값 해시 맵 할당
	
	//각각의 문자 빈도수에 따라서 트리 건축
	public static Node huffmanCoding(int n) {
		//큐에서 2개의 노드를 꺼내 빈도수를 합친 뒤, 우선순위 큐의 형태로 재삽입
		for(int i=0;i<n-1;i++) {
			Node z=new Node();
			z.right=queue.poll();
			z.left=queue.poll();
			z.frequency=z.right.frequency+z.left.frequency;
			queue.add(z);
		}
		return queue.poll();
	}
	
	public static void main(String[] args) throws IOException {
		Scanner scan=new Scanner(System.in);
		//각각의 문자에 대한 빈도수를 체크하는 변수 선언
		HashMap <Character, Integer> dictionary=new HashMap<Character, Integer>();
		String library;
		Node root=null;
		String data="";//도서목록 텍스트를 한 번에 가져와 저장
		boolean run=true;
		
		while(run) {
			System.out.println("도서관 책 목록을 가져옵니다. 도서관 이름을 입력하세요 (종료하려면 0을 누르세요) > ");
			library=scan.next();
			
			Book b=new Book();
			
			if(library.equals("논현도서관")) {
				data=b.s1;
			}else if(library.equals("도곡정보문화도서관")) {
				data=b.s2;
			}else if(library.equals("삼성도서관")) {
				data=b.s3;
			}else if(library.equals("세곡도서관")) {
				data=b.s4;
			}else if(library.equals("역삼도서관")) {
				data=b.s5;
			}else if(library.equals("0")) {
				System.out.println("프로그램 종료");
				break;
			}else {
				System.out.println("도서관을 찾을 수 없습니다. 다시 입력해주세요.");
			}
			
			
			//전체 문자열의 크기만큼 반복
	    	for(int i=0;i<data.length();i++) {
				
				//현재의 문자를 확인하여 temp에 삽입
				char temp=data.charAt(i);
				
				//현재 문자가 이미 1번 이상 들어가 있다면 크기를 1 증가
				if(dictionary.containsKey(temp))
					dictionary.put(temp, dictionary.get(temp)+1);
				
				//문자가 처음 들어가는 경우 크기를 1로 설정하여 삽입
				else
					dictionary.put(temp, 1);
	    	}
	    	
	    	//모든 노드를 우선순위 큐에 추가함으로써 트리 그룹 형성
		    
			queue=new PriorityQueue<Node>(100,new FrequencyComparator());
			int number=0;
			
			//문자와 그 빈도수가 저장된 각각의 모든 노드들을 우선순위 큐에 삽입
			for(Character c:dictionary.keySet()) {
				Node tmp=new Node();
				tmp.character=c;
				tmp.frequency=dictionary.get(c);
				queue.add(tmp); //우선순위 큐이기 때문에 삽입될 때 빈도 차이에 근거하여 우선순위를 산정 및 트리 구성
				number++;
			}
			
			//전체 노드 개수만큼 재배열하여 우선순위 큐 상에서의 노드 재배열
			root=huffmanCoding(number);
			
			//변수 길이 코드를 생성
			traversal(root, new String());
			
			//결과 출력
			String result=new String();
			for(int j=0;j<data.length();j++)
				result=result+charToCode.get(data.charAt(j))+"";
	    	
			System.out.println("------------- "+library+" 도서목록 --------------------");
			decode(data,root);
	
		}
		
	}
	
	//순회로 노드를 돌아 코드 입력
	public static void traversal(Node n, String s) {
		if(n==null)
			return;
		traversal(n.left, s+"0");
		traversal(n.right,s+"1");
		if(n.character!='\0') {
			//System.out.println(n.character+": "+s);
			charToCode.put(n.character, s);
		}
	}
	
	public static void decode(String S, Node root) {

	      StringBuilder output=new StringBuilder();
	      Node base=root;
	      
	      while(!S.isEmpty()) {
	         if(S.charAt(0)=='1') {
	            base=base.right;
	            S=S.substring(1);
	         }
	         else if(S.charAt(0)=='0'){
	            base=base.left;
	            S=S.substring(1);
	         }

	         if(base.left==null&&base.right==null) {
	            output.append(base.character);
	            base=root;
	         }
	      }
	      System.out.println(output.toString());
	   }
	
}