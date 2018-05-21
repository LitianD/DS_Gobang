package Gobang.Entity;

public class Vex
{
    public Vex(int put_number,int whiteOrBlack,int i,int j)
    {
        this.put_number=put_number;
        this.whiteOrBlack = whiteOrBlack;
        this.i=i;
        this.j=j;
    }
    int i;
    int j;
    int put_number;
    int whiteOrBlack; //1 black, 0 white;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public int getPut_number() {
        return put_number;
    }

    public void setPut_number(int put_number) {
        this.put_number = put_number;
    }

    public int getWhiteOrBlack() {
        return whiteOrBlack;
    }

    public void setWhiteOrBlack(int whiteOrBlack) {
        this.whiteOrBlack = whiteOrBlack;
    }
}
