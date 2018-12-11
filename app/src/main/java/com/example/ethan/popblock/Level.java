package com.example.ethan.popstar;

public class Level {
    private long score;
    private long level;
    private long requiredScore;

    //默认的等级开始
    public Level() {
        if (MainActivity.diff==0){
            score = 0;
            level = 1;
        }
        else if(MainActivity.diff==1){
            score = 0;
            level = 10;
        }
        else if(MainActivity.diff==2){
            score = 0;
            level = 20;
        }
        requiredScore = Algorithm.calcRequiredScore(level);
    }

    public void next() {
        level++;
        requiredScore = Algorithm.calcRequiredScore(level);
    }

    //累积分数
    public void gainScore(long score) {
        this.score += score;
    }

    public long getScore() {
        return score;
    }

    //分数是否可以通关
    public boolean hasEnoughScore() {
        return score >= requiredScore;
    }

    public long getLevel() {
        return level;
    }

    public long getRequiredScore() {
        return requiredScore;
    }
}
