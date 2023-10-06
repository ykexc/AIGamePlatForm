import { AcGameObject } from "../AcGameObject";
import type {GameMap} from "@/assets/scripts/snake/GameMap";

export class Wall extends AcGameObject {

    public r: number
    public c: number
    public color: string
    public gamemap: GameMap
    constructor(r: number, c: number, gamemap: GameMap) {
        super();
        this.r = r;
        this.c = c;
        this.gamemap = gamemap;
        this.color = "#B37226";
    }

    update() {
        this.render();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        ctx.fillRect(this.c * L, this.r * L, L, L);
    }
}
