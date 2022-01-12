import sys
# sys.stdin = open("input.txt", "rt")

n, m = map(int, input().split())  # 입력값을 띄어쓰기 기준으로 차례대로 매핑
a = list(map(int, input().split()))

a.sort()
lt = 0
rt = n - 1
while lt <= rt:
    mid = (lt + rt)//2
    if a[mid] == m:
        print(mid+1)
        break
    elif a[mid] > m:
        rt = mid-1
    else:
        lt = mid + 1
