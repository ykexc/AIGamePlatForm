package com.kob.backend.consumer.snake.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mqz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    private Integer id;
    private Integer botId;
    private String container;
    private String language;
    private String botCode;
    private Integer sx;
    private Integer sy;

    private List<Integer> steps;


    private boolean checkTailIncreasing(int step) {
        if (step <= 10) return true;
        return step % 3 == 1;
    }

    public List<Cell> getCells() {
        List<Cell> res = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x, y));
        for (int d : steps) {
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if (!checkTailIncreasing(++step)) {
                res.remove(0);
            }
        }
        return res;
    }

    public String getStepToString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer i : steps) stringBuilder.append(i);
        return stringBuilder.toString();
    }
}
