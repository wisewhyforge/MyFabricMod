package com.justin.justinmod;

public class JustinMathHelper
{

    public static double sigmoid(double x) {
        return Math.exp(x) / (Math.exp(x) + 1);
    }

}
