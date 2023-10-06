import {AcGameObject} from "@/assets/scripts/AcGameObject";
import {Chess} from "@/assets/scripts/gobang/Chess";

export class GameMap extends AcGameObject {

    public ctx: any
    public parent: any
    public button: any
    public rows: number
    public cols: number
    public L: number
    public chesses: Chess[]
    public gobangPkStore: any
    public userUseStore: any
    public gobangRecordStore: any

    constructor(parent: any, ctx: any, button: any, store: any, userUseStore: any, gobangRecordStore: any) {
        super()
        this.parent = parent
        this.ctx = ctx
        this.gobangPkStore = store
        this.button = button
        this.rows = 16
        this.cols = 16
        this.L = 0;
        this.chesses = [  //chess[0]为白,chess[1]为黑
            new Chess({
                id: store.aId,
            }, this),
            new Chess({
                id: store.bId,
            }, this)
        ]
        this.userUseStore = userUseStore
        this.gobangRecordStore = gobangRecordStore
    }


    checkStatus(): boolean {
        //return !(this.chesses[0].status === 'win' || this.chesses[1].status === 'win');
        return this.gobangPkStore.status === 'playing'
    }

    checkIsRecordAndFinishedStatus(): boolean {
        return this.gobangRecordStore.is_record && !this.gobangRecordStore.is_running
    }

    addListenEvents() {
        if (this.gobangRecordStore.is_record) {
            let aSteps: Array<Array<number>> = this.gobangRecordStore.a_steps,
                bSteps: Array<Array<number>> = this.gobangRecordStore.b_steps
            let loser = this.gobangRecordStore.record_loser
            //console.log(aSteps)
            //console.log(bSteps)
            let [w, b] = this.chesses
            if ("A" === loser) {
                b.status = 'win'
            } else {
                w.status = 'win'
            }
            let idx1 = 0, idx2 = 0, round = 0
            const interval: number = setInterval(() => {
                if (idx1 >= aSteps.length && idx2 >= bSteps.length) {
                    this.gobangRecordStore.updateIsRunning(false)
                    clearInterval(interval)
                } else if (round % 2 === 0) {
                    let x: number = aSteps[idx1][0], y: number = aSteps[idx1++][1]
                    w.chess[x][y] = 1
                    b.chess[x][y] = 1
                } else if (round % 2 === 1) {
                    let x: number = bSteps[idx2][0], y: number = bSteps[idx2++][1];
                    w.chess[x][y] = 2
                    b.chess[x][y] = 2
                }
                round++
            }, 400)
        } else {
            this.ctx.canvas.addEventListener('click', (e: any) => {
                if (this.checkStatus()) {
                    let rect = this.ctx.canvas.getBoundingClientRect()
                    let x = (e.clientX - rect.left) / this.L
                    let y = (e.clientY - rect.top) / this.L

                    if (x >= 0.5 && x <= this.cols - 0.5 && y >= 0.5 && y <= this.rows - 0.5) {
                        let direction = [[1, 0], [1, 1], [0, 1]]
                        let x0 = parseInt(String(x)), y0 = parseInt(String(y))
                        let xw = x0, yw = y0
                        let dis = Math.abs(x - x0) + Math.abs(y - y0)
                        for (let i = 0; i < 3; i++) {
                            x0 += direction[i][0]
                            y0 += direction[i][1]

                            if (Math.abs(x - x0) + Math.abs(y - y0) < dis) {
                                xw = x0
                                yw = y0
                                dis = Math.abs(x - x0) + Math.abs(y - y0)
                            }

                            x0 -= direction[i][0]
                            y0 -= direction[i][1]
                        }
                        if (this.gobangPkStore.round === 'A' && this.userUseStore.id === this.chesses[0].id.toString() && this.gobangPkStore.aBotId === -1) {
                            this.gobangPkStore.socket.send(JSON.stringify({
                                event: "play",
                                nx: xw,
                                ny: yw,
                                now_round: 'A'
                            }));
                            //console.log('A' + ' ' + xw + ' ' + yw)
                        }
                        if (this.gobangPkStore.round === 'B' && this.userUseStore.id === this.chesses[1].id.toString() && this.gobangPkStore.bBotId === -1) {
                            this.gobangPkStore.socket.send(JSON.stringify({
                                event: "play",
                                nx: xw,
                                ny: yw,
                                now_round: 'B'
                            }));
                            //console.log('B' + ' ' + xw + ' ' + yw)
                        }
                    }
                }
            })
        }


    }

    start() {
        this.addListenEvents()
    }

    updateSize() {
        this.L = parseInt(String(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows)))
        this.ctx.canvas.width = this.L * this.cols
        this.ctx.canvas.height = this.L * this.rows
    }

    update() {
        this.updateSize()
        this.render();
    }

    render() {
        let dd = [
            [4, 4],
            [4, 8],
            [4, 12],
            [8, 4],
            [8, 8],
            [8, 12],
            [12, 4],
            [12, 8],
            [12, 12]
        ]
        this.ctx.fillStyle = '#F1B44B'
        this.ctx.fillRect(0, 0, this.cols * this.L, this.rows * this.L)
        for (let i = 1; i < this.rows - 1; i++) {
            for (let j = 1; j < this.cols - 1; j++) {
                this.ctx.strokeRect(j * this.L, i * this.L, this.L, this.L)
            }
        }
        this.ctx.strokeRect(this.L, this.L, (this.cols - 2) * this.L, (this.rows - 2) * this.L)

        for (let d of dd) {
            this.ctx.fillStyle = 'black'
            this.ctx.beginPath()
            this.ctx.arc(d[0] * this.L, d[1] * this.L, this.L * 0.1, 0, Math.PI * 2)
            this.ctx.fill()
        }

        if (this.checkIsRecordAndFinishedStatus()) {
            let win: string
            if (this.chesses[0].status === 'win') win = '白方'
            else win = '黑方'
            this.ctx.font = 'italic Bold 36px Arial'
            this.ctx.fillStyle = 'rgba(206,79,79,0.9)'
            this.ctx.fillText(win + '胜利!', 170, 50)
        }



    }


}