# 스택

스택 어레이 마지막에 추가하거나 제거하는 것은 O(1)임

맨 앞에 추가하거나 제거하는 것은 O(n)임

물론 여기선 링크드리스트 이용해서 스택 만들것임.

# 생성자

일반 링드리스트 만드는 거랑 똑같이함. 단 head대신 top을 쓰고 tail은 없음. head를 쓰는 이유는 링드리스트는 앞에서 작업하는게 추가삭제 모두 O(1)이기 때문임.

```jsx
class Node{
    constructor(value){
        this.value = value
        this.next = null
    }
}

class Stack{
    constructor(value){
        const newNode = new Node(value)
        this.top = newNode
        this.length = 1
    }
}
```

# push

unshift임.

```jsx
push(value){
        const newNode = new Node(value)
        if(this.length===0){
            this.top = newNode
        }else{
            newNode.next = this.top
            this.top = newNode
        }
        this.length++
        return this
    }
```

# pop

![Untitled](https://user-images.githubusercontent.com/78577071/132174605-0a8a2cb7-48f0-459d-87f4-a2cb129f0c3f.png)

```jsx
pop(){
        if(this.length===0) return undefined
        let temp = this.top
        this.top = this.top.next
        temp.next = null

        this.length--
        return temp
    }
```

---

# 큐

얘도 비슷하게 하면됨

# 생성자

```jsx
class Node{
    constructor(value){
        this.value = value
        this.next = null
    }
}

class Queue{
    constructor(value){
        const newNode = new Node(value)
        this.first = newNode
        this.last = newNode
        this.length = 1
    }
}
```

# enque 메서드

마지막에다가 넣는 것임. 링드리스트의 push임

```jsx
enqueue(value){
        const newNode = new Node(value)
        if(this.length === 0){
            this.first = newNode
            this.last = newNode
        }else{
            this.last.next = newNode
            this.last = newNode
        }
        this.length++
        return this
    }
```

# dequeue 메서드

맨앞에꺼 삭제하는거임. 링드리스트의 shift임

```jsx
dequeue(){
        if(this.length===0) return undefined
        let temp = this.first
        if(this.length===1){
            this.first = null
            this.last = null
        }else{
            this.first = this.first.next
            temp.next = null
        }
        this.length--
        return temp
    }
```
