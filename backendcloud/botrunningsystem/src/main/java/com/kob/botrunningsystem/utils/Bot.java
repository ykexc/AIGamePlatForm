package com.kob.botrunningsystem.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Main {
    private final static int dx[] = {-1, 0, 1, 0};
    private final static int dy[] = {0, 1, 0, -1};

    private final static int[][] g = new int[13][14];
    private static int ax, ay, bx, by;
    private final static List<Cell>[] snake = new List[2];
    private static int rows = 13, cols = 14;

    static class Cell {
        int x, y;

        Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    public static Integer nextMove() {
        for (int i = 0; i < 4; i++) {
            int x = snake[0].get(snake[0].size() - 1).x + dx[i];
            int y = snake[0].get(snake[0].size() - 1).y + dy[i];
            if(x >= 0 && y >= 0 && x < rows && y < cols && g[x][y] == 0) {
                return i;
            }
        }
        return 0;
    }


    public static void main(String[] args) {
        before();
        int d = nextMove();
        System.out.print(d);
    }

    // 处理输入
    public static void before() {
        Scanner cin=new Scanner(System.in);
        String input = cin.nextLine();
        String[] strs = input.split("#");
        for (int i = 0, k = 0; i < 13; i++) {
            for (int j = 0; j < 14; j++, k++) {
                if (strs[0].charAt(k) == '1') {
                    g[i][j] = 1;
                }
            }
        }

        ax = Integer.parseInt(strs[1]);
        ay = Integer.parseInt(strs[2]);
        bx = Integer.parseInt(strs[4]);
        by = Integer.parseInt(strs[5]);

        snake[0] = getCells(ax, ay, strs[3]);
        snake[1] = getCells(bx, by, strs[6]);

        for (Cell c : snake[0]) {
            g[c.x][c.y] = 1;
        }
        for (Cell c : snake[1]) {
            g[c.x][c.y] = 1;
        }
    }

    private static boolean checkTailIncreasing(int steps) {
        if (steps <= 10) {
            return true;
        }
        return steps % 3 == 1;
    }

    private static List<Cell> getCells(int sx, int sy, String steps) {
        steps = steps.substring(1, steps.length() - 1);
        List<Cell> res = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x, y));
        for (int i = 0; i < steps.length(); i++) {
            int d = steps.charAt(i) - '0';
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if (!checkTailIncreasing(++step)) {
                res.remove(0);
            }
        }
        return res;
    }
}