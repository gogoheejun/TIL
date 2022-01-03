package notes.notepad;

public class Shape {
    int x = 0;
    int y = 0;
    protected int test = 300;

    public Shape(){
        this(0,0);
        System.out.println("과연??");
    }
    public Shape(int x, int y){
        this.x = x;
        this.y = y;
        System.out.println("과연2");
    }
}

class Circle extends Shape{
    int radius;

    Circle(){
        System.out.println("자식 circle 기본생성자"+test);
    }

    Circle(int x, int y, int radius){
        super(x,y);
        this.radius = radius;
    }

    public void draw(){
        System.out.println("x는 "+x + ", y는 "+y+ ", 반지름은 "+radius);
    }
}

class Test{
    public static void main(String[] args) {
        Circle c = new Circle(200,500,1000);
        c.draw();
        System.out.println("-------------------------------");
        Circle d = new Circle();
    }
}