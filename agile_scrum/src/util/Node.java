package util;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int numberValue;
    private List<double[][]> xacSuats;
    private double[] xacSuat;
    private String name;
    
    public Node(){
      xacSuats = new ArrayList<double[][]>();
    }
    
    public Node(double[] xacSuatIn, String name){
        this.name = name;
        xacSuat = new double[xacSuatIn.length];
        double[][] a = new double[1][xacSuatIn.length];
        for(int i = 0; i< xacSuatIn.length; i++){
            this.xacSuat[i] = xacSuatIn[i];
            a[0][i] = xacSuatIn[i];
        }
        xacSuats = new ArrayList<double[][]>();
        xacSuats.add(a);
      }

    public Node(String name, int num){
        this.name = name;
        numberValue = num;
        xacSuat = new double[num];
        xacSuats = new ArrayList<double[][]>();
    }
    /**
     * @return the numberValue
     */
    public int getNumberValue() {
        return numberValue;
    }

    /**
     * @param numberValue the numberValue to set
     */
    public void setNumberValue(int numberValue) {
        this.numberValue = numberValue;
    }

    /**
     * @return the xacSuats
     */
    public List<double[][]> getXacSuats() {
        return xacSuats;
    }

    /**
     * @param xacSuats the xacSuats to set
     */
    public void setXacSuats(List<double[][]> xacSuats) {
        this.xacSuats = xacSuats;
    }

    /**
     * @return the xacSuat
     */
    public double[] getXacSuat() {
        return xacSuat;
    }

    /**
     * @param xacSuat the xacSuat to set
     */
    public void setXacSuat(double[] xacSuat) {
        xacSuat = new double[xacSuat.length];
        for(int i = 0; i< xacSuat.length; i++){
            this.xacSuat[i] = xacSuat[i];
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public void normalize(){
        double total = 0;
        for(double d: xacSuat){
            total += d;
        }
        
        for(int i = 0; i< numberValue; i++){
            xacSuat[i] /= total;
        }
    }
    
}
