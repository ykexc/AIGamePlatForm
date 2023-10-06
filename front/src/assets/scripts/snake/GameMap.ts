import {AcGameObject} from "../AcGameObject";
import {Snake} from "@/assets/scripts/snake/Snake";
import {Wall} from "@/assets/scripts/snake/Wall";
import type {Cell} from "@/assets/scripts/snake/Cell";

export class GameMap extends AcGameObject {

    public ctx: CanvasRenderingContext2D
    public parent: any
    public pkStore: any
    public recordStore: any
    public L: number
    public rows: number
    public cols: number
    public walls: any
    public snakes: Snake[]

    constructor(ctx: any, parent: any, pkStore: any, recordStore: any) {
        super();
        this.pkStore = pkStore;
        this.recordStore = recordStore
        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;

        this.rows = 13;
        this.cols = 14;
        this.walls = [];

        this.snakes = [
            new Snake({id: 0, color: "#4876EC", r: this.rows - 2, c: 1}, this),
            new Snake({id: 1, color: "#F94848", r: 1, c: this.cols - 2}, this),
        ];
    }


    creat_walls() {
        const g: Array<Array<number>> = this.pkStore.gamemap;
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c] != 0) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }
    }

    add_listening_events() {
        if (this.recordStore.is_record) {
            let k = 0;
            const a_steps: string = this.recordStore.a_steps;
            const b_steps: string = this.recordStore.b_steps;
            const [snake0, snake1] = this.snakes;
            const loser = this.recordStore.record_loser;
            const interval_id = setInterval(() => {
                if (k >= a_steps.length - 1) {
                    if (loser === "all") {
                        snake0.status = "die";
                        snake1.status = "die";
                    } else if (loser === "A") {
                        snake0.status = "die";
                    } else if (loser === "B") {
                        snake1.status = "die";
                    }
                    clearInterval(interval_id)
                } else {
                    snake0.set_direction(parseInt(a_steps[k]))
                    snake1.set_direction(parseInt(b_steps[k]))
                }
                k++;
            }, 300)
        } else {
            this.ctx.canvas.focus()
            this.ctx.canvas.addEventListener("keydown", (e: { key: string; }) => {
                let d = -1;
                if (e.key === 'ArrowUp') d = 0;
                else if (e.key === 'ArrowRight') d = 1;
                else if (e.key === 'ArrowDown') d = 2;
                else if (e.key === 'ArrowLeft') d = 3;
                if (d >= 0) {
                    this.pkStore.socket.send(JSON.stringify({
                        event: "move",
                        direction: d
                    }));
                }
            });
        }
    }

    start() {
        this.creat_walls();
        this.add_listening_events();
    }


    update_size() {
        // 计算小正方形的边长
        this.L = parseInt(String(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows)));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    check_ready() {
        // 判断两条蛇是否都准备好下一回合
        for (const snake of this.snakes) {
            if (snake.status !== "idle") return false;
            if (snake.direction === -1) return false;
        }
        return true;
    }

    next_step() { // 让两条蛇进入下一回合
        for (const snake of this.snakes) {
            snake.next_step();
        }

    }

    check_valid(cell: Cell): boolean {  // 检测目标位置是否合法：没有撞到两条蛇的身体和障碍物
        for (const wall of this.walls) {
            if (wall.r === cell.r && wall.c === cell.c)
                return false;
        }

        for (const snake of this.snakes) {
            let k = snake.cells.length;
            if (!snake.check_tail_increasing()) {  // 当蛇尾会前进的时候，蛇尾不要判断
                k--;
            }
            for (let i = 0; i < k; i++) {
                if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c)
                    return false;
            }
        }

        return true;
    }


    update(): void {
        this.update_size();
        if (this.check_ready()) {
            this.next_step();
        }
        this.render();
    }

    render(): void {
        // 取颜色
        const color_eve = "#AAD751", color_odd = "#A2D149";
        // 染色
        for (let r = 0; r < this.rows; r++)
            for (let c = 0; c < this.cols; c++) {
                if ((r + c) % 2 === 0) {
                    this.ctx.fillStyle = color_eve;
                } else {
                    this.ctx.fillStyle = color_odd;
                }
                //左上角左边，明确canvas坐标系
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            }
    }
}
