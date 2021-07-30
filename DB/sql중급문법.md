[sql_cheat_sheet.pdf](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/9c53f527-07da-44b5-82da-ba574acba8b3/sql_cheat_sheet.pdf)

# GROUP BY

- [https://www.hackerrank.com/challenges/earnings-of-employees/problem](https://www.hackerrank.com/challenges/earnings-of-employees/problem)

```jsx
select salary*months as earnings, Count(*) 
from employee
group by earnings
order by earnings desc
limit 1
```

# CASE

```jsx
SELECT CASE
			WHEN categoryid = 1 AND SupplierID = 1 THEN '음료'
	    WHEN categoryid = 2 THEN '조미료'
	    ELSE '기타'
		END AS 'categoryName', *
FROM Products
```

[https://www.hackerrank.com/challenges/what-type-of-triangle/problem?h_r=internal-search](https://www.hackerrank.com/challenges/what-type-of-triangle/problem?h_r=internal-search)

```jsx
SELECT CASE
        WHEN A=B and B=C THEN 'Equilateral'
        WHEN A+B <= C OR A+C <= B OR B+C<=A THEN 'Not A Triangle'
        WHEN A=B OR B=C  OR A=C THEN 'Isosceles'
        ELSE 'Scalene '
    END
FROM TRIANGLES
```

- 피보팅

[https://leetcode.com/problems/reformat-department-table/](https://leetcode.com/problems/reformat-department-table/)

```jsx
select id, 
    SUM( CASE WHEN month='Jan' THEN revenue ELSE NULL END) AS 'Jan_Revenue',
    SUM( CASE WHEN month='Feb' THEN revenue ELSE NULL END) AS 'Feb_Revenue',
    SUM( CASE WHEN month='Mar' THEN revenue ELSE NULL END) AS 'Mar_Revenue',
    SUM( CASE WHEN month='Apr' THEN revenue ELSE NULL END) AS 'Apr_Revenue',
    SUM( CASE WHEN month='May' THEN revenue ELSE NULL END) AS 'May_Revenue',
    SUM( CASE WHEN month='Jun' THEN revenue ELSE NULL END) AS 'Jun_Revenue',
    SUM( CASE WHEN month='Jul' THEN revenue ELSE NULL END) AS 'Jul_Revenue',
    SUM( CASE WHEN month='Aug' THEN revenue ELSE NULL END) AS 'Aug_Revenue',
    SUM( CASE WHEN month='Sep' THEN revenue ELSE NULL END) AS 'Sep_Revenue',
    SUM( CASE WHEN month='Oct' THEN revenue ELSE NULL END) AS 'Oct_Revenue',
    SUM( CASE WHEN month='Nov' THEN revenue ELSE NULL END) AS 'Nov_Revenue',
    SUM( CASE WHEN month='Dec' THEN revenue ELSE NULL END) AS 'Dec_Revenue'
    
 from Department
Group by id
```

# LEFT JOIN

right조인 거의 안쓰니까 걍 leftjoin만 ㄱ. 쓰는 위치만 바뀔뿐.

언제쓰냐?

![Untitled](https://user-images.githubusercontent.com/78577071/127676692-aeac5775-dfbd-48ad-8c0f-6dd7028dc792.png)


위 테이블보면, 3번 사용자는 가입은 했지만 주문은 한번도 안했어. innerjoin하면 주문을 한 1,2번만 셀렉트함. 

근데, **주문했든안했든 일단 모든 사용자의 주문기록을 보고 싶으면** 이렇게 아우터조인(left,right)을 사용하면 된다.

만약, 한번도 주문 안한 사용자들만 뽑고 싶으면? where 붙여주삼

```sql
SELECT * FROM CUSTOMERS
LEFT JOIN Orders ON Customers.CustomerID = Orders.CustomerID
WHERE OrderID IS NULL
```

---

## LEFT JOIN 문제(해커랭크)

- innerjoin문제[https://www.hackerrank.com/challenges/african-cities/problem?h_r=internal-search](https://www.hackerrank.com/challenges/african-cities/problem?h_r=internal-search)

```sql
select city.name from city 
	INNER JOIN country ON city.countrycode = country.code 
where country.continent ='Africa'
```

실수하는게 select 다음에 name이 city에도 있고, country에도 있어..그러니까 city.name이라고 해줘야 함.

- [https://www.hackerrank.com/challenges/asian-population/problem](https://www.hackerrank.com/challenges/asian-population/problem)

```sql
select sum(CITY.Population) from CITY INNER JOIN COUNTRY 
ON CITY.CountryCode = COUNTRY.Code 
WHERE COUNTRY.CONTINENT = 'Asia'
```

- [https://www.hackerrank.com/challenges/average-population-of-each-continent/problem?h_r=internal-search](https://www.hackerrank.com/challenges/average-population-of-each-continent/problem?h_r=internal-search)

```sql
select Country.continent, floor(avg(City.population)) from Country INNER JOIN CITY 
ON CITY.CountryCode = COUNTRY.Code 
Group by  Country.continent
```

---

## INNER JOIN 문제(리트코드)

- [https://leetcode.com/problems/customers-who-never-order/](https://leetcode.com/problems/customers-who-never-order/)

한번도 주문하지 않은 고객

```sql
select name as Customers from Customers
left join Orders 
on Customers.Id = Orders.CustomerId
where Orders.CustomerId is Null
```

---

# SELF JOIN

```sql
select E.Name as Employee from Employee as E
inner join Employee as Manager
ON E.managerId = Manager.ID
where E.salary > Manager.salary
```
