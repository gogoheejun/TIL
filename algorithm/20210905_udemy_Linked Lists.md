


array가 메모리에 저장되는 모양:

![Untitled](https://user-images.githubusercontent.com/78577071/132120284-050e4e9c-3400-41ec-b724-e703c55458df.png)

링드리스트가 메모리에 저장되는 모양

![Untitled 1](https://user-images.githubusercontent.com/78577071/132120286-a2a375a0-b4b3-43ec-b360-8fba90fdd0a9.png)

특징:

- 인덱스가 없음
- 어레이는 메모리에 연속적으로 있지만, 링드리스트는 아무데나 있음
- 첫번째 아이템을 가리키는 변수(head)와 마지막아이템을 가리키는 변수(tail)이 있음
- 헤드→첫째→중간1→중간2.....→마지막→null 순으로 각각 아이템은 다음애를 pointing함

![Untitled 2](https://user-images.githubusercontent.com/78577071/132120287-109b1f9e-0dc0-408a-8160-448798ed835b.png)

# 링드리스트의 Big O

- 마지막에 추가하는경우

마지막에다가 새로운 아이템을 추가하는건 걍 원래있던 마지막 아이템이 새로 추가된 애를 포인팅하게 하고, tail도 걔를 포인팅하게 하면 되는거니까, 링드리스트의 아이템이 4개든 100개든 **O(1)**임

- 마지막 삭제하는경우

마지막꺼를 삭제하는 건 쉽지만, tail이 포인팅하는 걸 다시 앞으로 한칸 땡기는 건 불가능하므로 맨 처음부터 쭈욱 가서 null가리키는 애를 tail이 가리키도록 해야 함. 고로 **O(n)**

- 맨 앞에 추가하는경우

head만 앞으로 옮기는건데, 걍 처음에 있는거니까 O(1)

- 맨 앞에꺼 삭제하는경우

head만 하나 뒤로 옮기면 되므로 O(1)

- 중간에 추가하는 경우

맨앞부터 iterate하면서 가니까 O(n)

- 중간꺼 삭제하는 경우

얘도 마찬가지로 맨앞부터 가니까 O(n)

- 인덱스로 몇번째꺼 찾기

맨앞부터 차례대로 세면서 찾으므로 O(n)

어레이랑 비교:

![Untitled 3](https://user-images.githubusercontent.com/78577071/132120289-28f99718-877d-4ba1-888e-1083def63da3.png)

# 그래서 링크드리스트가 뭔데?

object임. 아래처럼 구성된.

```jsx
{value:4, next: null} 
```

11→3→23→7→4 로 구성된 링크드리스트를 보면 아래처럼 구성됨

```jsx
{ head:{
					value:11,
					next:{
								value:3,
								next:{
											value:23,
											next:{
														value:7,
														next:{
																	value:4,
																	next:null
																	}
															}
											}
							}
					}
}
```

# 생성자 만들기

```jsx
//노드클래스를 만들고 생성자통해서 value, next만듦
class Node{
	constructor(value){
			this.value = value
			this.next = null
	}
}

//링드리스트 생성자를 만들면서 노드객체도 생성함
class LinkedList{
    constructor(value){
        const newNode = new Node(value)
        this.head = newNode 
        this.tail = this.head
        this.length = 1
    }
}
let myLinkedList = new LinkedList(4)
```

콘솔로 myLinkedList치면,

LinkedList {head: Node, tail: Node, length: 1} 

→head: Node {value: 4, next: null}
length: 1

tail: Node {value: 4, next: null}

# push 메서드 만들기

```jsx
class LinkedList{
    constructor(value){
        const newNode = new Node(value)
        this.head = newNode 
        this.tail = this.head
        this.length = 1
    }
    push(value){
				//푸시할때 새 노드생성
        const newNode = new Node(value)
				//노드가 비어있는겨우
        if(!this.head){
            this.head = newNode
            this.tail = newNode
        }else{
				//원래 링크드리스트 마지막(tail)노드의 포인터를 새로운 애를 가리키게 함
            this.tail.next = newNode
				//글구 마지막애를 새로운애로 재정의
            this.tail = newNode
        }
				//노드 추가됐으니 길이추가
        this.length++
				//링크드리스트 자체를 리턴함
        return this
    }
}
```

# pop 메서드

맨 마지막노드를 팦시키면 tail이 가리키고 있는애도 앞으로 땡겨야함

그럼 앞을 땡기는 애를 지칭하려면 그 앞의 next랑 같은 애를 포인팅하면 됨. 근데 그 앞에거를 알라면 또 그 앞에의 next를 알아야함. 이렇게 죽죽죽위로 올라가면 head가 나타남. 고로 head부터 하나씩 iterate하면서 마지막까지 가야함

- 코드작성하기

temp랑 pre를 만들어서 temp가 하나 앞서서 노드를 포인팅하면서 가면서 젤 마지막에 가면 날리고 pre가 tail이 되도록 하는 것임

![Untitled 4](https://user-images.githubusercontent.com/78577071/132120292-33002e52-944d-4ace-a0ea-cadbfd3c1d8e.png)

```jsx
class LinkedList{
	  push(valeu){...}

    pop(){
//아무것도 없는경우
        if(!this.head) return undefined
//정상인 경우
        let temp = this.head
        let pre = this.head
//temp가 null을 포인팅할때까지(즉 마지막까지 이동할때까지)
        while(temp.next){
            pre = temp  //temp를 뒤로 하나 옮기기 전에 얘를 해야함
            temp = temp.next 
        }
        this.tail = pre  //tail설정(temp는 팦시키고 그 앞의 pre를 tail로해줘야함)
        this.tail.next = null
        this.length--
//하나만 있는겨우 팦하면 0되서
        if(this.length===0){
            this.head = null
            this.tail = null
        }
        return temp //지금 temp가 제일마지막에 팦된거니까 리턴
    }
}
```

# unshift 메서드

```jsx
class LinkedList{
	  push(valeu){...}

    pop(value){...}

		unshift(value){
		        const newNode = new Node(value)
		        if(!this.head){
		            this.head = newNode
		            this.tail = newNode
		        }else{
		            newNode.next = this.head //원래 맨앞에있는애를 뉴노드의 next가 포인팅하도록
		            this.head = newNode //뉴노드가 맨앞이니까 head로 지칭
		        }
		        this.length++
		        return this
		    }
}
```

# shift 메서드

```jsx
class LinkedList{
	  push(valeu){...}

    pop(){...}

		unshift(value){...}
	
		shift(){
	//노드없는경우
        if(!this.head) return undefined
	//정상인경우
        let temp = this.head //템프를 하나만들어서 맨앞에있는애가 두번째애를 포인팅하는 걸 없앨거임 null로
        this.head = this.head.next
        temp.next = null
        this.length--
	//노드가 원래 1개인 경우
	// 0개가됨. 근데head는 자연스럽게 null이되는데, tail은 계속 사라진 한개를 포인팅하고 있음
        if(this.length===0){
            this.tail = null
        }
    }
}
```

# get 메서드

```jsx
class LinkedList{
	  push(valeu){...}

    pop(){...}

		unshift(value){...}
	
		shift(){...}

		get(index){
        if(index<0||index >= this.length){
            return undefined
        }
        let temp = this.head
        for(let i=0; i<index; i++){
            temp = temp.next
        }
        return temp
    }
}
```

# set 메서드

```jsx
class LinkedList{
	  push(valeu){...}

    pop(){...}

		unshift(value){...}
	
		shift(){...}

		get(index){...}
		
		set(index,value){
        let temp = this.get(index) //get메서드 재사용하면 됨
        if(temp){
            temp.value = value
            return true
        }
        return false
    }
		 
}
```

# insert 메서드

```jsx
class LinkedList{
	  push(valeu){...}

    pop(){...}

		unshift(value){...}
	
		shift(){...}

		get(index){...}
		
		set(index,value){...}

		insert(index,value){
		        if(index===0) return this.unshift(value) //맨 앞에 넣을때
		        if(index===this.length) return this.push(value) //맨 뒤에 넣을때
		        if(index<0 || this.length) return false //이상한곳에 넣을때
		//가운데에 넣을때
		        const newNode = new Node(value)
		        const temp = this.get(index-1)
		        newNode.next = temp.next
		        temp.next = newNode
		        this.length++
		        
		        return true
		    }
				 
}
```

# remove 메서드

![Untitled 5](https://user-images.githubusercontent.com/78577071/132120294-c62539be-ff4a-4e49-9050-966f8ba5f527.png)

```jsx
remove(index){
        if(index===0) return this.unshift(value)
        if(index===this.length) return this.push(value)
        if(index<0 || this.length) return false

        const before = this.get(index-1)
        const temp = before.next

        before.next = temp.next
        temp.next = null
        this.length--
        return temp
    }
```

# reverse 메서드

head랑 tail을 바꾸고 방향도 바꾸는 것임

https://user-images.githubusercontent.com/78577071/132119932-8289ce24-11ab-4773-a9e6-670492c8c1a7.mp4

```jsx
reverse(){
//head랑 tail 바꾸기
        let temp = this.head
        this.head = this.tail
        this.tail = temp

        let next = temp.next //맨앞의 다음.즉 1번인덱스.(현재 temp가 0번인덱스잖어)
        let prev = null //-1번자리에다가 null넣기 위해 prev라고 함
//prev-temp-next를 오른쪽으로 하나씩 옮기면서 temp화살표 반대로 바꾸기
        for(let i=0; i<this.length; i++){
            next = temp.next
            temp.next = prev //temp의 화살표 왼쪽으로
            prev = temp //prev를 오른쪽으로 한칸
            temp = next //temp를 오른쪽으로 한칸
        }
				return this
    }
```
