package Gobang.Entity;

public class DownTemp {
    int i;
    int j;
    int color;
    int tag;
    int count;

    public DownTemp(int i,int j,int color,int tag,int count){
        this.i=i;
        this.j=j;
        this.color=color;
        this.tag=tag;
        this.count=count;

    }

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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
