# 제네릭, 컬렉션 프레임워크

- 제네릭 제한하기
![Untitled](https://user-images.githubusercontent.com/78577071/132890939-351b7499-a2a9-44cb-953a-ce9aa22df2e4.png)

- 제네릭 메서드 활용해보기

```jsx
public class Point<T,V>{
	T x;
	V y;

	Point(T x, V y){
		this.x = x;
		this.y = y;
	}

	public T getX(){
		return x;
	}
	
	public V getY(){
		return y;
	}
}
```

```jsx
	public class GenericMethod{
		public static<T,V> double makeRectangle(Point<T,V> p1, Point<T,V> p2){
			dsfsdfsd...
			dfsdfsdfsdf....
				
			return width*height;
	}
```

```jsx
	public static void main(String[] args){
		Point<Integer, Double> p1 = new Point<Integer, Double>(0, 0.0);
		Point<Integer, Double> p2 = new Point<>(10, 10.0);
	
		double rect = GenericMethod.<Integer, Double>makeRectangle(p1,p2);
	}
```

# 컬렉션 프레임워크

컬렉션 개열은 단일개체를 관리, 맵개열은 쌍으로 된 애들을 관리함
![Untitled 1](https://user-images.githubusercontent.com/78577071/132890967-2150bca2-95c4-46e2-9386-bc5c8be25da6.png)


 개념은 대충 아니까 모르는 것만 적겠음

### Iterator

아래 두 코드는 똑같음. 보면 어케 쓰는지 알 수 있음

```jsx
public boolean removeMember(int memberId){

	for(int i=0; i<arrayList.size(); i++){
			Member member = arrayList.get(i);
			int tempId = member.get(i);
			if(memberId == tempId){
				arrayList.remove(i);
				return true;
			}
	}
}
```

```jsx
public boolean removeMember(int memberId){

	Iterator<Member> iterator = arrayList.iterator();

	while(iterator.hasNext()){
		Mmeber member = iterator.next();
		int tempId = member.getMemberId();
		if(memberId = tempId){
			arrayList.remove(member);
			return true;
		}
}
```

### HashSet 사용시 주의사항

→ add하려는 객체가 내가 만든 객체라면 equals 메서드를 재정의해줘야함.

main함수

문제발생: **101번 Lee는 출력되면 안되는데 출력이 되버림**

```jsx
public static void main(String[] args){
	MemberHashSet memberHashSet = new MemberHashSet();
	Member memberLee = new Member(101,"Lee");
	Member memberLee2 = new Member(101,"Lee2");
	
	memberHashSet.addMember(memberLee);
	memberHashSet.addMember(memberLee2);

	memberHashSet.showAll();**//이러면 101번 Lee는 출력되면 안되는데 출력이 되버림**
}
```

MemberHashSet클래스

```jsx
public class MemberHashSet{
	private HashSet<Member> hashSet;
	public MemberHashSet(){
		hashSet = new HashSet<Member>();
	}

	public void addMember(Member member){
		hashSet.add(member);
	}
	...
	...
}
```

Member클래스

문제해결: equals를 오버라이드해야함. HashSet<String>처럼 String같은 애를 쓰면 String클래스에 자체적으로 equals를 주소가 아닌 값만 비교하도고 오버라이드 해놨는데, Member클래스는 내가 만든거니까 따로 해줘야 함.

```jsx
public class Member{
	...
	...
	@Overrde
	public boolean equals(Object obj){
		if(obj instanceof Member){
			Member member = (Member) obj;
			if(this.memberId == member.memberId){
				return true;
			}
			else return false;
		}
		return false;
}
```

### TreeSet

- 이름앞에 Tree붙으면 정렬을 위한거임.
- 내부적으로 이진검색트리로 구현됨.
- 중복허용안하면서 오름차순/내림차순으로 객체정렬함
- 이진검색트리에 자료가 저장될 때 비교하면서 저장될 위치 정함
- 객체비교를 위해 Comparable이나 Comparator인터페이스를 구현해야함

- 기본자료형일 때

```jsx
TreeSet<String> tree = new TreeSet<String>();tree.add("aaa");tree.add("ccc");tree.add("bbb");//위처럼 순서없이 넣어도 tree출력하면// [aaa,bbb,ccc] 요렇게 abc순으로 저장된 걸 확인가능
```

→이게 어케가능? String소스 가보면 Comparable<String>을 구현했음. 마치 equals도 Object꺼 오버라이드한 것처럼.
![Untitled 2](https://user-images.githubusercontent.com/78577071/132890983-5dea621a-473f-44d8-ae73-aa55f9f46735.png)

- 그럼 내가 만든 객체를 TreeSet에 넣으면 나도 Comparable을 구현해줘야 TreeSet에 넣으면 비교를 해주면서 넣어줌

Member클래스

```jsx
public class Member implememnts Comparable<Member>{	...	...	@Override	public int compareTo(Member member){		//return (this.memberId - this.memberId) //양수리턴하면 오름차순		return (this.memberName.compareTo(member.memberName)); //이러면 스트링기준	}}
```

이외에 Comparator방법도 있음. 일반적으로 Comparable 더많이 사용하고, 이미 구현된 Comparable말고 다른 정렬방식 이용하고 싶을 때 Comparator 씀

# Map 인터페이스

내부적으로 hash방식임.

### hashMap

MemberHashMap클래스

```jsx
public class MemberHashMap{	private HashMap<Integer, Member> hashMap;	public MemberHashMap(){		hashMap = new HashMap<Integer, Member>();	}	public void addMember(Member member){		hashMap.put(member.getMemberId(), member);	}	public boolean removeMember(int memberId){		if(hashMap.containsKey(memberId)){			hashMap.remove(memberId);			return true;		}	}	public void showAllMemer(){		Iterator<Integer> ir = hashMap.KeySet().iterator();//키 모여있는 세트의 이터레이터		while(ir.hasNext()){			int key = ir.next();			Member member = hashMap.get(key);			System.out.println(member);		}}
```

해시맵이랑 비슷하게 hashTable이란 것도 있는데 얘도 Vector처럼 동기화 제공해주지만 멀티스레드일때 쓰는거라 단일쓰레드일땐 굳이 얘쓰면 낭비임. 단일스레드면 굳이 안그래도 되는걸 혼자 블락했다가 풀기 반복함.

### TreeMap

얘도 거의 비슷하게 쓰면 됨. 

다른 점은 TreeSet처럼 이미 구현된 클래스를 key로 사용하면 괜찮지만, 아닌 경우엔 Comparable 구현해야함
![Untitled 3](https://user-images.githubusercontent.com/78577071/132891007-700a4c55-ea9f-4e8a-8821-13f7f52b17e3.png)

