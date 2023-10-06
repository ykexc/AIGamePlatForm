export class Cell {
    public r: number
    public c: number
    public x: number
    public y: number

    constructor(r: number, c: number) {
        this.r = r;
        this.c = c;
        //转换位canvas坐标
        this.x = c + 0.5;
        this.y = r + 0.5;
    }
}