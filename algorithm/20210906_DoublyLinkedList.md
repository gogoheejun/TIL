양방향으로 포인팅하는 것임

```jsx
{
	value: 7,
	next: null,
	prev: null
}
```

# 생성자 만들기

```jsx
class Node{
    constructor(value){
        this.value = value
        this.next = null
        this.prev = null //추가됨
    }
}

class DoublyLinkedList{
    constructor(value){
        const newNode = new Node(value)
        this.head = newNode
        this.tail = newNode
        this.length = 1
    }
}
```

# push

```jsx
push(value){
        const newNode = new Node(value)
        if(!this.head){
            this.head = newNode
            this.tail = newNode
        }else{
            this.tail.next = newNode
            newNode.prev = this.tail//이게 중요
            this.tail = newNode
        }
        length++
        return this
    }
```

# pop

tail은 앞으로 한칸 땡기고 temp는 마지막으로 해서 서로 next랑 prev null로 만들면 됨

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/dfa1698b-964d-45e1-96d4-2843cbfd03e6/Untitled.png)

```jsx
pop(){
        if(this.length===0) return undefined//애초에 0개일때
         //한개밖에 없을때 지지우면 0개되니까
        let temp = this.tail
        if(this.lengt===1){
            this.head = null
            this.tail = null
        }else{
            this.tail = this.tail.prev
            this.tail.next = null
            temp.prev = null
        }
        
        this.length--
        return temp
    }
```

# unshift

```jsx
unshift(value){
        const newNode = new Node(value)
        if(this.lenght===0){
            this.head = newNode
            this.tail = newNode
        }else{
            newNode.next = this.head
            this.head.prev = newNode
            this.head = newNode
        }
        this.length++
        return this
    }
```

# shift

```jsx
shift(){
        if(this.length===0) return undefined
        let temp = this.head
        if(this.length===1){
            this.head = null
            this.tail = null
        }else{
            this.head = this.head.next
            this.head.prev = null
            temp.next = null
        }
        this.length--
        return temp
    }
```

# get

효율성을 위해서 뒤에서 거꾸로 찾는것도 해줌

```jsx
gget(index){
        if(index<0||index>=this.length){
            return undefined
        }
        let temp = this.head
//인덱스가 전반부에 있을때
        if(index < this.length/2){
            for(let i=0; i<index; i++){
                temp = temp.next
            }
//인덱스가 후반부에 있을때
        }else{
            temp = temp.tail
            for(let i = tihs.length-1; i>index ; i--){ //범위 헷갈림 잘 생각해보셈
                temp = temp.prev
            }
        }
        
        return temp

    }
```

# set

얘는 일반 링크드리스트랑 똑같. 단 get이 다른 방식으로 된 것 뿐

```jsx
set(index, value){
        let temp = this.get(index)
        if(temp){//undefined될수도 있어서
            temp.value = value
            return true
        }
        return false
    }
```

# insert

after랑 before를 생성해서 활용

```jsx
insert(index,value){
        if(index===0) return this.unshift(value)
        if(index===this.length) return this.push(value)
        if(index<0 || index > this.length) return false

        const newNode = new Node(value)
        const before = this.get(index-1)
        const after = before.next 

        before.next = newNode
        newNode.next = after
        newNode.prev = before
        after.prev = newNode

        this.length++
        return true
    }
```

# remove

temp하나가지고 다 해결

```jsx
remove(index){
        if(index===0) return this.shift()
        if(index=== this.length-1) return this.pop()
        if(index<0 || index >= this.length) return undefined

        const temp = this.get(index)
        temp.prev.next = temp.next
        temp.next.prev = temp.prev
        temp.next = null
        temp.prev = null

        this.length--
        return temp
    }
```
