이미 자바스크립트에는 객체로 빌트인 해시가 있음. 

특징:

1. One way임. 

    → lala를 해시에 돌리면 2가 나왔어. 2에서 다시 lala로 못만들어

2. Deterministc임

    → lala는 항상 2가 됨. 몇번을 해도 똑같

고로, set('lala',1200)을 해시로 돌리면 2번메모리에 ['lala',1200]이 곧장 저장됨. 메모리에 직빵으로 가는것임

# Collisions

같은 메모리에 ['lala',1200], ['haha',900]이 동시에 저장되려고 해. 두게 다 받아주는 걸 Seperate Chaining 이라고 하고, 그걸 허용안하고 빈 메모리 찾을때까지 옮겨서 빈메모리에 넣는걸 Linear Probing이라고 함.

여기선 Seperate Chaining으로 할것임

# 생성자 만들기

지금 디폴트로 0~6번에다가 저장한다고 가정하자. _hash함수는 직접쓰는게 아니라 안에서 부를때 쓸것임.

```jsx
class HashTable{
    constructor(size = 7){//defaut 7
        this.dataMap = new Array(size)
    }
    _hash(key){
        let hash = 0
        for(let i =0; i<key.length; i++){
            hash = (hash + key.charCodeAt(i)*23)& this.dataMap.length
        }
        return hash
    }
}
```

# set메서드

```jsx
set(key,value){
        let index = this._hash(key)
        if(!this.dataMap[index]){
            this.dataMap[index] = [] //아무것도 없다면 빈배열 만들어줌
        }
        this.dataMap[index].push([key,value])
        return this
    }
```

![Untitled](https://user-images.githubusercontent.com/78577071/132250136-21365b44-7ade-44f4-940e-74d8db65bbed.png)

# get메서드

와우 내부원리가 이런거였구나

만약 dataMap의 4번에 [['bolts',1400],['washers',50]] 이렇게 있는 경우를 생각하고 코드봐보삼

```jsx
get(key){
        let index = this._hash(key)
        if(this.dataMap[index]){
            for(let i=0; i<this.dataMap[index].length; i++){
                if(this.dataMap[index][i][0] === key){
                    return this.dataMap[i][1] 
                }
            }
        }
        return undefined //빈배열일 경우
    }
```

# keys메서드

키들 다 가져오기

```jsx
keys(){
        let allKeys=[]
        for(let i=0; i<this.dataMap.length; i++){
            if(this.dataMap[i]){
                for(let j=0; j<this.dataMap[i].length;j++){
                    allKeys.push(this.dataMap[i][j][0])
                }
            }
        }
        return allKeys
    }
```

# 해시의 빅O

set하는건 해시에 바로 접근하니까 O(1)임.

get은? collsion발생하면 worst케이스일땐 반복문 끝에 있으니까 O(n)같겠지만 실제 자바스크립트의 빌트인 해시테이블인 오브젝트는 훨씬 랜덤하고 에피션트하기 때문에 O(1)임.

→ 배열 두개가 있어. 겹치는게 있는지 찾는문제: 해시를 안쓰면 다음처럼 O(n2)가 됨.

```jsx
function itemInCommon(arr1, arr2) {
     for(let i = 0; i < arr1.length; i++) {
         for(let j = 0; j < arr2.length; j++) {
             if (arr1[i] === arr2[j]) return true
         }
     }
     return false
 }
 
 let array1 = [1, 3, 5]
 let array2 = [2, 4, 5]
 
 
 itemInCommon(array1, array2)
```

그러나 해시쓰면 O(2n), 어차피 상수삭제하니까 즉 O(n)이 됨

```jsx
function itemInCommon(arr1, arr2) {
     let obj = {}
     for (let i = 0; i < arr1.length; i++) {
         obj[arr1[i]] = true
     }
     for (let j = 0; j < arr2.length; j++) {
       if (obj[arr2[j]]) return true
     }
     return false
}
   
   
let array1 = [1, 3, 5]
let array2 = [2, 4, 5]
   
itemInCommon(array1, array2)
```
