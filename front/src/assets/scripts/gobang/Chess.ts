import {AcGameObject} from "@/assets/scripts/AcGameObject";

export class Chess extends AcGameObject {

    public id: number
    public gamemap: any
    public chess: Array<Array<number>>
    public status: string


    constructor(info: any, gamemap: any) {
        super();
        this.id = info.id
        this.gamemap = gamemap
        this.chess = gamemap.gobangPkStore.gamemap  //1号为A白方, 2号为B为黑方
        this.status = "o"
    }


    start() {}

    setChess(x: number, y: number, v: number) {
        console.log(x, y, v)
        this.chess[x][y] = v
    }

    update() {
        this.render()
    }

    render() {
        const ctx = this.gamemap.ctx
        const L: number = this.gamemap.L
        const rows = this.gamemap.rows, cols = this.gamemap.cols;
        for (let i = 1; i < rows; i++) {
            for (let j = 1; j < cols; j++) {
                if (this.chess[i][j] == 1) {
                    ctx.fillStyle = 'white'
                    ctx.beginPath()
                    ctx.arc(i * L, j * L, L * 0.3, 0, Math.PI*2)
                    ctx.fill()
                } else if (this.chess[i][j] == 2) {
                    ctx.fillStyle = 'black'
                    ctx.beginPath()
                    ctx.arc(i * L, j * L, L * 0.3, 0, Math.PI*2)
                    ctx.fill()
                }
            }
        }
    }

}