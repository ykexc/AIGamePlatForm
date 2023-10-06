### AIGamePlatForm



**本平台是一个AI游戏对抗平台,目前已经开发两款游戏** [绕蛇](https://www.ykexc.work/snake/pk) **和**[五子棋](https://www.ykexc.work/gobang/pk) **,玩家可以操控自己的代码(bot)实现人人、人与bot、bot和bot的在线对战, 目前bot代码的语言支持Java,C++,本平台** 具体说明以及代码示例**.**

在线体验地址: https://ykexc.work



前端技术栈: Vue3、Vite、TypeScript、Pinia、vue3-ace-editor、axios、element-plus、naiveui、canvas

后端技术栈: SpringBoot、Mysql、Redis、RabbitMQ、minio、SpringSecurity、mybatis-plus、websocket、Django、Docker

由于还在开发中， 本平台可能有着各种奇怪的问题，遇到这些问题时，你可以通过下面的方式联系作者。

[728787402@qq.com](mailto:728787402@qq.com)

# 绕蛇

本游戏与传统的贪吃蛇游戏有所不同,游戏采用固定蛇的长度，两名玩家可以真人操作也可以使用bot操作，让对手撞到墙上或蛇身体上，自己即可获胜。
<img src="https://image.itbaima.net/images/76/image-20230916222451692.png" alt="image.png" style="zoom:50%;" />  

每局开始时都会有提示，告知自己位于哪一方.如果是真人匹配可以通过上下左右四个方向键来控制蛇前进的方向(需要在3s内做出响应).如果是bot不需要任何操作,观战即可。
<img src="https://image.itbaima.net/images/76/image-20230916228009989.png" alt="image.png" style="zoom:80%;" />
下面是Java和c++的演示程序

```java
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

```

```cpp
#include <iostream>
#include <vector>
#define x first
#define y second


using namespace std;

typedef pair<int, int> pii;
const int N = 20;
const int dx[]{ -1, 0, 1, 0 };
const int dy[]{ 0, 1, 0, -1 };

int ax, ay, bx, by, rows = 13, cols = 14, direction = 0;
int g[N][N];
vector<pii> snake[2];

int nextMove() {
    for(int i = 0; i < 4; i ++) {
        int x = snake[0][snake[0].size() - 1].x + dx[i];
        int y = snake[0][snake[0].size() - 1].y + dy[i];
        if(x >= 0 && y >= 0 && x < rows && y < cols && g[x][y] == 0) {
            return i;
        }
    }
    return 0;
}

bool check_tail_increasing(int steps) { // 检测当前回合蛇是否变长
    if (steps <= 10) {
        return true;
    }
    return steps % 3 == 1;
}

vector<pii> getCells(int sx, int sy, string steps) {
    steps = steps.substr(1, steps.length() - 2);
    vector<pii> res;
    int x = sx, y = sy;
    int step = 0;
    res.push_back({x, y});
    for (int i = 0; i < steps.size(); i++) {
        int d = steps[i] - '0';
        x += dx[d];
        y += dy[d];
        res.push_back({x, y});
        if (!check_tail_increasing(++step)) {
            res.erase(res.begin());
        }
    }
    return res;
}


void before()
{
    string str, split = "#";
    vector<string> s;
    cin >> str;
	if (str == "") return;
	string strs = str + split;
	size_t pos = strs.find(split);
 
	while (pos != strs.npos)
	{
		string temp = strs.substr(0, pos);
		s.push_back(temp);
		strs = strs.substr(pos + 1, strs.size());
		pos = strs.find(split);
	}
	
	for (int i = 0, k = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++, k++) {
            if (s[0][k] == '1') {
                g[i][j] = 1;
            }
        }
    }

    ax = stoi(s[1],0,10);
    ay = stoi(s[2],0,10);
    bx = stoi(s[4],0,10);
    by = stoi(s[5],0,10);
    
    snake[0] = getCells(ax, ay, s[3]);
    snake[1] = getCells(bx, by, s[6]);

    for (pii c : snake[0]) {
        g[c.x][c.y] = 1;
    }
    for (pii c : snake[1]) {
        g[c.x][c.y] = 1;
    }
}


int main() {
    before();
    direction = nextMove();
    cout << direction;
    return 0;
}
```

# 五子棋

五子棋玩法与传统玩法相同,分为黑方和白方,采用回合制玩法,玩家每回合的思考时间不能超过1min,bot代码的运行时间复杂度需控制在1s内。
<img src="https://image.itbaima.net/images/76/image-20230916224003257.png" alt="image.png" style="zoom:80%;" />
以下是Java和C++的示例代码(因五子棋的bot算法较为复杂,以下提供的只能按行列枚举走)

```java
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

class Main {


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String mySteps = in.nextLine(), opponentSteps = in.nextLine();
        String[] split = mySteps.split("\\*");  //进行转义
        String[] split1 = opponentSteps.split("\\*");
        Set<String> set1 = new HashSet<>(Arrays.asList(split));
        Set<String> set2 = new HashSet<>(Arrays.asList(split1));
        for (int x = 1; x <= 15; x++) {
            for (int y = 1; y <= 15; y++) {
                String s = x + "#" + y;
                if (!set1.contains(s) && !set2.contains(s)) {
                    System.out.println(x + "#" + y);
                    return;
                }
            }
        }
        in.close();
    }

}

```

```cpp
#include <iostream>
#include <string>
#include <sstream>
#include <unordered_set>

int main() {
    std::string mySteps, opponentSteps;
    std::getline(std::cin, mySteps);
    std::getline(std::cin, opponentSteps);

    std::unordered_set<std::string> mySet, opponentSet;
    std::istringstream iss1(mySteps), iss2(opponentSteps);
    std::string step;

    while (std::getline(iss1, step, '*')) {
        mySet.insert(step);
    }

    while (std::getline(iss2, step, '*')) {
        opponentSet.insert(step);
    }

    for (int x = 1; x <= 15; ++x) {
        for (int y = 1; y <= 15; ++y) {
            std::string position = std::to_string(x) + "#" + std::to_string(y);
            if (mySet.find(position) == mySet.end() && opponentSet.find(position) == opponentSet.end()) {
                std::cout << position << std::endl;
                return 0;
            }
        }
    }

    return 0;
}

```

