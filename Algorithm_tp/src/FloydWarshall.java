import java.util.*;
import java.io.*;

public class FloydWarshall //플로이드 워샬을 통한 최단거리 검색입니다.
{
	static int c =0; //도서관 인덱스를 대신해줄 변수
	static int passcnt =0; //케이스 카운터
	//static int distance =(((user[num].x) - (rib[i].x))^2 + ((user[num].y) - (rib[i].y))^2); //유저와 도서관의 거리 계산입니다. 도서관 txt가 없어서 아직 할당 안했습니다.

	static int nextcnt =0; //유효 케이스 카운터 	
	static int sumcnt =0; //유효 케이스의 수 카운터 
	
	
	rentbook rent = new rentbook();//클래스 받아옴
	riblen r = new riblen();//클래스 받아옴
	
	static class rentbook//빌릴 책들을 대한 정보를 저장해줄 클래스입니다.
	{
		List<String> rentbook = new ArrayList<String>()
		{
			{
				add("90년생이 온다");
				add("사랑하게 해줘서, 고마워");
				add("관찰의 힘");
			}
		};
		//빌릴 책들의 이름을 저장
		//이거를 이제 검색해서 클릭하면 저장되게 해야합니다...
		
		List<Integer> rentval = new ArrayList<Integer>()
		{
			{
				add(0);
				add(0);
				add(0);
			}
		};
		//빌렸는지 여부 처음 값 모두 0 빌리면 1
		
		List<Integer> sum = new ArrayList<Integer>()
		{
	
			{
					add(0);
			
			}
			
		};//책을 빌릴때 드는 이동거리의 합
		

		String path[][] = new String[100][100];//경로를 저장해줄 2차원 배열입니다.
		
		Integer path1[][] = new Integer[5][5];
		
	}

	class riblen//각 도서관끼리의 거리 정보를 저장해줄 클래스입니다. 차후 도서관.txt에 넣으면 좋을것 같습니다.
	{
		int PlugUP = 9999;//길이 막혀서 못간다는 뜻입니다. 약도에 가시적으로 추가하면 좋을 것 같습니다.
		int length[][] = { { 0, 7, PlugUP, PlugUP, 3}, { 7, 0, 4, 10, 2}, { PlugUP, 4, 0, 2, PlugUP},
				{ PlugUP, 10, 2, 0, 11 }, { 3, 2, PlugUP, 11, 0 } };
		int pass[][][] = new int[100][100][100];//지나오면서 들른 다른 도서관들을 저장합니다
	}
	
	String[] rib = {"X","y","z","r","b"};//도서관 이름 배열입니다. 임시값입니다.
	
	
	
	public void floydalgorithm()//플로이드 워샬을 수행해줄 함수입니다. 중간 들른지점까지 저장합니다.
	{	
		for (int i=0; i<5; i++)
    		for (int j=0; j<5; j++)
    			if (r.length[i][j] == 10000)
    				rent.path1[i][j] = -1;
    			else
    				rent.path1[i][j] = i;

		for (int k = 0; k < r.length.length; k++) 
		{
			for (int i = 0; i < r.length.length; i++) 
			{
				for (int j = 0; j < r.length.length; j++) 
				{
					if (r.length[i][j] > r.length[i][k] + r.length[k][j])
					{
						r.length[i][j] = r.length[i][k] + r.length[k][j];
				
							rent.path1[i][j] = rent.path1[k][j];				
					}
				}
			}
		}   
		
		for (int i = 0; i < r.length.length; i++) //플로이드 워샬 후 각각 최단거리 출력입니다.
		{
            for (int j = 0; j < r.length.length; j++) 
            {
                if (r.length[i][j] == 9999)
                    System.out.printf("%5s", "INF");
                else
                    System.out.printf("%5d", r.length[i][j]);
            }
            System.out.println("");
		}
	}
	
	
	public boolean search1(String rib, String book) throws IOException
	{
		String bookFile = rib + "books.txt";
		
		BufferedReader inputStream = null;
		try {
			inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(bookFile), "utf-8"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String temp = "tmp"; // null값으로 초기화 할 경우 검색 안되는 문제

		do {
			temp = inputStream.readLine(); // throws IOException
			if (temp == null)
				break; // 문서 마지막까지 읽었을 경우 break
			/* for checking */
			if (temp.contains(book)) 
			{
				return true;
			}

		} while (temp != null);
		
		return false;
		
	}
	

	public boolean allrent()//모두 빌렸는지 여부를 확인시켜 주는 함수입니다.
	{
		
		for(int i=0; i<rent.rentval.size(); i++)
		{
			if(rent.rentval.get(i) == 0)
			{
				return false;
			}
		}
		return true;
	}
	
	public void resetrent()//빌렸는지 여부를 리셋(모두 0으로)시켜주는 함수입니다.
	{
		
		for(int i=0; i<rent.rentval.size(); i++)
		{
			if(i != 1)
				rent.rentval.set(i,0);		
		}
	}
	
	
	
	
	public void print() throws IOException//결과 출력 함수입니다.
	{			
		rent.sum.remove((Integer)0);//처음 집어넣은 0값은 nullpointer에러를 피하기위한 임시값이므로 삭제시킵니다.
		
		int minIndex = rent.sum.indexOf(Collections.min(rent.sum));//최소값을 구한 후 인덱스 값을 받아옵니다.

		System.out.println();
		System.out.println("각 경우의 길이 : ");
		for(int i=0; i <rent.sum.size(); i++)//모든 유효케이스(완주 가능)들을 출력합니다.
		{
			System.out.println("유효 케이스[" + (i+1) +  "] : " + rent.sum.get(i));
		}
			
		System.out.println();
		System.out.println("최단거리 : " + Collections.min(rent.sum));//최단 거리 출력
						
		System.out.print("최단 이동경로 : ");
		for(int i=0; i < rent.path.length; i++)//최단 이동경로 출력
		{
			if(rent.path[minIndex][i] == null)
				break;
			else
			{
				if(rent.path[minIndex][i] != null && rent.path[minIndex][i] !=rent.path[minIndex][i+1])
					System.out.print("[" + rent.path[minIndex][i]+ "] ");
				if(i % 2 == 0)
					link(minIndex,i);
			}	
		}
		
		System.out.println();
		
		for(int i=0; i < rent.path.length; i++)//최단 이동경로 출력
		{
			for(int b=0; b<rent.rentbook.size(); b++)//그 도서관에 있는 책들을 모두 빌렸다고 추가합니다.
			{
				if(rent.path[minIndex][i] != null &&  search1(rent.path[minIndex][i], rent.rentbook.get(b)))
				{	
					System.out.println("'" + rent.rentbook.get(b) + "' 책은 " + rent.path[minIndex][i] + "도서관에서 빌리실 수 있습니다.");
					rent.rentval.set(b, 1);	
				}
				resetrent();
			}
		}
		
		System.out.println();
		for(int j=0; j < 10; j++)
		{
			for(int i=0; i < rent.path.length; i++)
			{
				if(rent.path[j][i] != null)
					System.out.print("[" + rent.path[j][i]+ "] ");
				if(i % 2 == 0 && rent.path[j][i+ 1] != null)
					link(j,i);
			}
			System.out.println();
		}
		

	}
	
	public void link(int a, int b)//플로이드 워샬을 통해 지나가게 되는 두 포인트 사이의 값들을 구하는 함수입니다. 다만 순서가 정확하지않아 수정중입니다. null로 하지못해 생긴 오류
	{
	  	
		int bidx = 0;
		int b2idx = 0;
		
		
		for(int i=0; i < rib.length; i++)
		{
			if(rib[i] == rent.path[a][b])
				bidx =  i;
			if(rib[i] == rent.path[a][b + 1])
				b2idx =  i;
		}	
    		
		for (int i=0; i<5; i++)
			rent.path1[i][i] = i;
		
    	String myPath = "";
    	
    	while (rent.path1[bidx][b2idx] != bidx) {
    		myPath = "(" + rib[rent.path1[bidx][b2idx]] + ") " + myPath;
    		b2idx = rent.path1[bidx][b2idx];
    	}
    	
    	myPath ="" + myPath;
    	System.out.print(myPath);
    	
	}

	
	public void m5(int cur) throws IOException//플로이드워샬을 바탕으로 문제를 해결해주는 함수입니다.
	{	
		
		if(c > rib.length)//c는 cur의 도서관 인덱스 번호입니다. 0~4까지 있으므로 5이면 종료합니다.
		{
			return;
		}
				
	
		for(int i=0; i < rent.rentbook.size(); i++)
		{	
						
			for(int j= 0 ; j < rib.length; j++)
			{	
				if(passcnt ==0)
				{
					rent.path[sumcnt][passcnt] = rib[cur];//현재 있는 곳을 들렀으므로 경로에 저장합니다.
					passcnt++;
				}
		
				for(int b=0; b<rent.rentbook.size(); b++)//그 도서관에 있는 책들을 모두 빌렸다고 추가합니다.
				{
					if(search1(rib[cur], rent.rentbook.get(b)))
					{	
						//System.out.println("rent " + rent.rentbook.get(b) + " by " + rib[cur]);
						rent.rentval.set(b, 1);		
					}
				}
				
				if(allrent() && c < rib.length - 1)// 모두 빌렸으면  다음 도서관 시작케이스로 넘어갑니다.
				{	
				
				//	System.out.println("All rent");
					sumcnt++;	
					passcnt =0;
					c++;
					resetrent();		
					m5(c);
					
				}
				
				if (j != cur && search1(rib[j], rent.rentbook.get(i)) && rent.rentval.get(i) == 0 ) 
					//자신을 제외한 도서관에 찾고싶은 책이 있고, 아직 책을 빌린상태가 아니라면
					{	
					
					rent.path[sumcnt][passcnt] = rib[j];
					

						if(r.length[cur][j] != r.PlugUP)//길이 안막혔으면 갈 수 있으므로
						{												
							for(int b=0; b<rent.rentbook.size(); b++)//그 도서관에 있는 책들을 모두 빌렸다고 추가합니다.
							{
								if(search1(rib[j], rent.rentbook.get(b)))
								{	
									//System.out.println("rent " + rent.rentbook.get(b) + " by " + rib[j]);
									rent.rentval.set(b, 1);		
								}
							}

					//		System.out.println("유효 케이스(완주 가능) : " + nextcnt + "번 케이스");
					//		System.out.println("유효 케이스의 누적 수 : " + (sumcnt + 1));
							if( rent.sum.get(sumcnt) == 0 || rent.sum.get(sumcnt) == null) // sum 값 인덱스가 없으면 새로운 케이스 이므로 새로 만들어 리스트에 추가합니다.
							{								
								rent.sum.add(sumcnt, r.length[cur][j]);							
							}	
							else //아니라면 기존에 있는 케이스 이므로 기존 sum 값에 더합니다.
								rent.sum.set(sumcnt, rent.sum.get(sumcnt) + r.length[cur][j]);
							
						
							
							for(int k=0; k < r.pass.length; k++)//경로에 있는 도서관에도 빌릴 수 있으므로 그 도서관에 있는 책들을 모두 빌렸다고 추가합니다.
							{
								for(int t=0; t < rent.rentbook.size(); t++)
								{
									if (search1(rib[r.pass[cur][j][k]], rent.rentbook.get(t)) &&  rent.rentval.get(t) == 0)
										rent.rentval.set(i,1);	
								}
								
							}	
							nextcnt++;
							m5(j);	
						}																									
					}
							
				}
			}
	}
			
	public static void main(String[] args) throws IOException //메인 함수 입니다.
	{
		FloydWarshall f = new FloydWarshall();
		
		f.floydalgorithm();
			
		
	/*	while(click.close)//종료 버튼을 누를때까지
		{
			Scanner sc = new Scanner(System.in);
			
			//책 검색 후 선택하는 함수
			//대여책 배열에 저장
		}
	*/

		f.m5(0);
		f.print();		
				
	}
}




	

