package Gobang.Constant;

public class Constant {
    public static String[] states={"等待匹配中","游戏进行中"};
    public static Constant constant=new Constant();


    public static Constant getConstant(){
        if(constant==null){
            return new Constant();
        }
        else
            return constant;
    }

    public  String getStates(int i) {
        return states[i];
    }


}
